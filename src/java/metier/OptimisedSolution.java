/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import dao.BoxAcheteDao;
import dao.CommandeDao;
import dao.DaoFactory;
import dao.JpaDaoFactory;
import dao.LigneProductionDao;
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
import model.LigneProduction;
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
    private LinkedHashMap<Integer, Integer> ligneProductions;
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
    }

    public void execute() {
        Collection<Commande> commandes = commandeDao.findAll();
        
        while (!commandes.isEmpty()) {
            ite++;
            Commande commande = findUrgentCommande(commandes);
            produireCommande(commande);
            commandes.remove(commande);
        }
        produitDao.findAll().stream().forEach((produit) -> {
            System.out.println("produit " + produit .toString());
            System.out.println("ligne de prod" + produit.getNblignes().toString());
        });
        eval();
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
                    + produitCommande.getNbUnites() * typeProduit.getTProduction();
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
        commande.setDenvoireel(dateActuelleBox);
        commandeDao.update(commande);
    }

    private void produireEtStockerProduitCommande(ProduitCommande produitCommande) {
        initialiserLigneProduction();
        LigneProduction ligneProduction = choisirLigneProduction();
        TypeProduit typeProduit = produitCommande.getIdTypeProduit();
        
        if (typeProduit != null) {
            dateActuelleProduction = ligneProductions.get(ligneProduction.getId()) 
                    + typeProduit.getTSetup();
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
        TypeBox typeBox = trouverTypeBox(produit);
        BoxAchete boxAchete = acheterBox(typeBox);

        boxAchete.getProduitCollection().add(produit);
        boxAchete.setIdCommande(produit.getIdProduitCommande().getIdCommande());
        boxAcheteDao.update(boxAchete);

        typeBox.getBoxAcheteCollection().add(boxAchete);
        typeBoxDao.update(typeBox);

        produit.setIdBox(boxAchete);
        produit.getIdProduitCommande().getIdCommande().getBoxAcheteCollection().add(boxAchete);
        produitDao.update(produit);
    }
    
    private TypeBox trouverTypeBox(Produit produit) {
        if (produit.getIdProduitCommande() != null && produit.getIdProduitCommande().getIdTypeProduit() != null){
            TypeProduit typeProduit = produit.getIdProduitCommande().getIdTypeProduit();
            return typeBoxDao.findFirstByDimensions(typeProduit.getLongueur(), typeProduit.getHauteur());
        } 
        return null;
    }

    private BoxAchete acheterBox(TypeBox typeBox) {
        BoxAchete boxAchete = new BoxAchete();
        boxAchete.setIdTypeBox(typeBox);
        boxAchete.setNumBox(boxAcheteDao.countBoxesByTypeBox(typeBox)+1);
        boxAcheteDao.create(boxAchete);
        return boxAchete;
    }

    public void eval(){
        eval = 0;
        typeBoxDao.findAll().stream().forEach((typeBox) -> {
            eval = eval
                    + typeBox.getPrixbox()
                    * boxAcheteDao.countBoxesByTypeBox(typeBox);
        });
        commandeDao.findAll().stream().forEach((commande) -> {
            eval = eval +
                    commande.getPenalite()
                    * abs(commande.getDenvoireel()-commande.getDenvoiprevue());
        });
        System.out.println("magic eval " + eval);
    }
}
