/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import dao.BoxAcheteDao;
import dao.CommandeBoxDao;
import dao.CommandeDao;
import dao.DaoFactory;
import dao.JpaDaoFactory;
import dao.LigneProductionDao;
import dao.PileDao;
import dao.ProduitCommandeDao;
import dao.ProduitDao;
import dao.TypeBoxDao;
import static java.lang.Math.abs;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BoxAchete;
import model.Commande;
import model.CommandeBox;
import model.LigneProduction;
import model.Pile;
import model.Produit;
import model.ProduitCommande;
import model.TypeBox;
import model.TypeProduit;

/**
 *
 * @author ghitakhamaily
 */
public class OptimisedSolution {

    private Integer dateActuelleProduction;
    private Integer dateActuelleBox;
    private final JpaDaoFactory jpaDaoFactory;
    private final CommandeDao commandeDao;
    private final LigneProductionDao ligneProductionDao;
    private final ProduitDao produitDao;
    private final ProduitCommandeDao produitCommandeDao;
    private final TypeBoxDao typeBoxDao;
    private final BoxAcheteDao boxAcheteDao;
    private final PileDao pileDao;
    private LinkedHashMap<Integer, Integer> ligneProductions;
    private final CommandeBoxDao commandeBoxDao;
    private double eval;
    int ite = 0;
    
    public OptimisedSolution() {
        dateActuelleProduction = 0;
        dateActuelleBox = 0;
        jpaDaoFactory
                = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        commandeDao = jpaDaoFactory.getCommandeDao();
        produitCommandeDao = jpaDaoFactory.getProduitCommandeDao();
        ligneProductionDao = jpaDaoFactory.getLigneProductionDao();
        produitDao = jpaDaoFactory.getProduitDao();
        typeBoxDao = jpaDaoFactory.getTypeBoxDao();
        boxAcheteDao = jpaDaoFactory.getBoxAcheteDao();
        pileDao = jpaDaoFactory.getPileDao();
        commandeBoxDao = jpaDaoFactory.getCommandeBoxDao();
    }

    public void execute() {
        Collection<Commande> commandes = commandeDao.findAll();
        
        while (!commandes.isEmpty()) {
            ite++;
            Commande commande = findUrgentCommande(commandes);
            produireCommande(commande);
            commandes.remove(commande);
        }
        /*produitDao.findAll().stream().forEach((produit) -> {
            System.out.println("produit " + produit .toString());
            System.out.println("ligne de prod" + produit.getNblignes().toString());
        });
        eval();*/
    }

    public Commande findUrgentCommande(Collection<Commande> commandes) {
        Double evalProductionMax = -Double.MAX_VALUE;
        Commande commandeMax = new Commande();
        for (Commande commande : commandes) {
            Double evalProduction = commande.getPenalite()
                    * (dEnvoieEstimee(commande) - commande.getDenvoiprevue());
            if (evalProduction >= evalProductionMax) {
                try {
                    commandeMax = (Commande) commande.clone();
                    evalProductionMax = evalProduction;
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(OptimisedSolution.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return commandeMax;
    }

    private Integer dEnvoieEstimee(Commande commande) {
        return commande.getStockmin() + dateFinProductionEstimee(commande);
    }

    private Integer dateFinProductionEstimee(Commande commande) {
        initialiserLigneProduction();

        commande.getProduitCommandeCollection().stream().forEach((produitCommande) -> {
            Integer ligneProductionId
                    = Collections.min(ligneProductions.entrySet(), Map.Entry.comparingByValue()).getKey();
            TypeProduit typeProduit = produitCommande.getIdTypeProduit();
            Integer time = typeProduit.getTSetup()
                    + (produitCommande.getNbUnites() * typeProduit.getTProduction());
            ligneProductions.put(ligneProductionId, ligneProductions.get(ligneProductionId) + time);
        });
        return Collections.max(ligneProductions.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private void initialiserLigneProduction() {
        ligneProductions = new LinkedHashMap<>();
        ligneProductionDao.findAll().stream().forEach((ligneProduction) -> {
            Produit produit = produitDao.findLastProductInLine(ligneProduction);
            Integer time = 0;
            if(produit != null){
                time = produit.getDateDebutProd() 
                        + produit.getIdProduitCommande().getIdTypeProduit().getTProduction();
            } 
            ligneProductions.put(ligneProduction.getId(), time);
        });
    }

    private void produireCommande(Commande commande) {
        commande.getProduitCommandeCollection().stream().forEach((produitCommande) -> {
            produireEtStockerProduitCommande(produitCommande);
        });
        Integer dateFinProduction = Collections.max(ligneProductions.entrySet(), 
                Map.Entry.comparingByValue()).getValue();
        dateActuelleBox = dateFinProduction + commande.getStockmin();
        if (dateActuelleBox < commande.getDenvoiprevue()) {
            dateActuelleBox = commande.getDenvoiprevue();
        }
        libererBoxes(commande);
        commande.setDenvoireel(dateActuelleBox);
        commandeDao.update(commande);
    }

    private void produireEtStockerProduitCommande(ProduitCommande produitCommande) {
        initialiserLigneProduction();
        LigneProduction ligneProduction = choisirLigneProduction();
        TypeProduit typeProduit = produitCommande.getIdTypeProduit();
        
        if (typeProduit != null) {
            dateActuelleProduction = ligneProductions.get(ligneProduction.getId()) ;
            if(ligneProductionNeedsSetUp(ligneProduction, typeProduit))
                dateActuelleProduction += typeProduit.getTSetup();
            for (int i = 0; i < produitCommande.getNbUnites(); i++) {
                Produit produit = produireProduit(produitCommande, ligneProduction);
                
                ligneProduction.getProduitCollection().add(produit);
                ligneProductionDao.update(ligneProduction);

                produitCommande.getProduitCollection().add(produit);
                produitCommandeDao.update(produitCommande);
                
                dateActuelleProduction += typeProduit.getTProduction();
                
                stockerProduit(produit);
            }
            ligneProductions.put(ligneProduction.getId(), dateActuelleProduction);
        }
    }

    public Produit produireProduit(ProduitCommande produitCommande, LigneProduction ligneProduction) {
        Produit produit = new Produit();
        produit.setIdProduitCommande(produitCommande);
        produit.setDateDebutProd(dateActuelleProduction);
        produit.setNblignes(ligneProduction);
        produitDao.create(produit);
        return produit;
    }

    private LigneProduction choisirLigneProduction() {
        return ligneProductionDao
                .find(Collections.min(ligneProductions.entrySet(),
                        Map.Entry.comparingByValue()).getKey());
    }

    private void stockerProduit(Produit produit) {
        Commande commande= produit.getIdProduitCommande().getIdCommande();
        TypeBox typeBox = trouverTypeBox(produit);
        BoxAchete boxAchete = acheterBox(typeBox);
        Pile pile = empiler(produit, boxAchete);
        CommandeBox commandeBox = relierBoxCommande(commande, boxAchete);
        
        boxAchete.getPileCollection().add(pile);
        boxAchete.getCommandeBoxCollection().add(commandeBox);
        boxAcheteDao.update(boxAchete);

        typeBox.getBoxAcheteCollection().add(boxAchete);
        typeBoxDao.update(typeBox);

        produit.setIdPile(pile);
        produitDao.update(produit);
        
        commande.getCommandeBoxCollection().add(commandeBox);
        commandeDao.update(commande);
    }
    
    private TypeBox trouverTypeBox(Produit produit) {
        if (produit.getIdProduitCommande() != null 
                && produit.getIdProduitCommande().getIdTypeProduit() != null){
            TypeProduit typeProduit = produit.getIdProduitCommande().getIdTypeProduit();
            return typeBoxDao.findFirstByDimensions(typeProduit.getLongueur(), typeProduit.getHauteur());
        } 
        return null;
    }

    private BoxAchete acheterBox(TypeBox typeBox) {
        BoxAchete boxAchete = new BoxAchete();
        boxAchete.setIdTypeBox(typeBox);
        boxAchete.setNumBox(boxAcheteDao.countBoxes(typeBox)+1);
        boxAcheteDao.create(boxAchete);
        return boxAchete;
    }

    private Pile empiler(Produit produit, BoxAchete boxAchete) {
        TypeProduit typeProduit = produit.getIdProduitCommande().getIdTypeProduit();
        Pile pile = new Pile();
        pile.getProduitCollection().add(produit);
        pile.setLargeurPile(typeProduit.getHauteur());
        pile.setLongueurPile(typeProduit.getLongueur());
        pile.setIdBoxAchete(boxAchete);
        pileDao.create(pile);
        return pile;
    }

    private CommandeBox relierBoxCommande(Commande commande, BoxAchete boxAchete){
        CommandeBox commandeBox = new CommandeBox();
        commandeBox.setIdBoxAchete(boxAchete);
        commandeBox.setIdCommande(commande);
        return commandeBox;
    }
            
    private void libererBoxes(Commande commande) {
        commande.getCommandeBoxCollection().stream().forEach((commandeBox) -> {
            BoxAchete boxAchete = commandeBox.getIdBoxAchete();
            boxAchete.setLibre(0);
            boxAcheteDao.update(boxAchete);
        });
    }
    
    public void eval(){
        eval = 0;
        typeBoxDao.findAll().stream().forEach((typeBox) -> {
            eval = eval
                    + typeBox.getPrixbox()
                    * boxAcheteDao.countBoxes(typeBox);
        });
        commandeDao.findAll().stream().forEach((commande) -> {
            eval = eval +
                    commande.getPenalite()
                    * abs(commande.getDenvoireel()-commande.getDenvoiprevue());
        });
        System.out.println("magic eval " + eval);
    }

    private boolean ligneProductionNeedsSetUp(LigneProduction ligneProduction, TypeProduit typeProduit) {
        Produit produit = produitDao.findLastProductInLine(ligneProduction);
        if(produit != null){
            TypeProduit oldTypeProduit = produit.getIdProduitCommande().getIdTypeProduit();
            if(oldTypeProduit.getId().equals(typeProduit.getId()))
                return false;
        }
        return true;
    }
}
