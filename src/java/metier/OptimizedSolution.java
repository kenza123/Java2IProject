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
import dao.PileDao;
import dao.ProduitCommandeDao;
import dao.ProduitDao;
import dao.TypeBoxDao;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BoxAchete;
import model.Commande;
import model.LigneProduction;
import model.Pile;
import model.Produit;
import model.ProduitCommande;
import model.TypeBox;
import model.TypeProduit;

public class OptimizedSolution {

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
    
    public OptimizedSolution() {
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
    }

    /**
     * Fonction permettant d'executer le traitement métier
     */
    public void execute() {
        Collection<Commande> commandes = commandeDao.findAll();
        
        while (!commandes.isEmpty()) {
            Commande commande = findUrgentCommande(commandes);
            produireEtStockerCommande(commande);
            commandes.remove(commande);
        }
    }

    /**
     * Trouver la commande la plus prioritaire
     * @param commandes Commandes
     * @return Commande la plus prioritaire
     */
    public Commande findUrgentCommande(Collection<Commande> commandes) {
        Double evalProductionMax = -Double.MAX_VALUE;
        Commande commandeMax = new Commande();
        for (Commande commande : commandes) {
            Double evalProduction = commande.getPenalite()
                    * (dEnvoieEstimee(commande) - commande.getDenvoiprevue());
            if (evalProduction > evalProductionMax) {
                try {
                    commandeMax = (Commande) commande.clone();
                    evalProductionMax = evalProduction;
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(OptimizedSolution.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return commandeMax;
    }

    /**
     * Trouver la date d'envoi estimée
     * @param commande Commande
     * @return  Date envoi estimée
     */
    private Integer dEnvoieEstimee(Commande commande) {
        return commande.getStockmin() + dateFinProductionEstimee(commande);
    }

    /**
     * Touver la date de fin de production estimee
     * @param commande Commande
     * @return Date de fin de production estimée
     */
    private Integer dateFinProductionEstimee(Commande commande) {
        initialiserLigneProduction();

        commande.getProduitCommandeCollection().stream().forEach((produitCommande) -> {
            Integer ligneProductionId
                    = Collections.min(ligneProductions.entrySet(), Map.Entry.comparingByValue()).getKey();
            TypeProduit typeProduit = produitCommande.getIdTypeProduit();
            Integer timeEndProduitCommande = typeProduit.getTSetup()
                    + (produitCommande.getNbUnites() * typeProduit.getTProduction());
            Integer timeFree = ligneProductions.get(ligneProductionId);
            ligneProductions.put(ligneProductionId, timeFree + timeEndProduitCommande);
        });
        return Collections.max(ligneProductions.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    /**
     * Mets a jour la map "ligneProductions" composée de l'id des lignes de production 
     * et de la date de libération de la ligne selon la derniere production 
     * effectuée dans cette ligne
     */
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

    /**
     * Fonction permettant de poduire et de stocker la commande
     * @param commande Commande
     */
    private void produireEtStockerCommande(Commande commande) {
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

    /**
     * Fonction permettant de produire et stocker le produit
     * @param produitCommande Produit de la Commande
     */
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

    /**
     * Fonction permettant de produire le produit
     * @param produitCommande ProduitCommande
     * @param ligneProduction Ligne de Production
     * @return Produit
     */
    public Produit produireProduit(ProduitCommande produitCommande, LigneProduction ligneProduction) {
        Produit produit = new Produit();
        produit.setIdProduitCommande(produitCommande);
        produit.setDateDebutProd(dateActuelleProduction);
        produit.setNblignes(ligneProduction);
        produitDao.create(produit);
        return produit;
    }

    /**
     * Fonction permettant de choisir la ligne de production
     * @return LigneProduction
     */
    private LigneProduction choisirLigneProduction() {
        return ligneProductionDao
                .find(Collections.min(ligneProductions.entrySet(),
                        Map.Entry.comparingByValue()).getKey());
    }

    /**
     * Fonction permettant de stocker le produit
     * @param produit Produit
     */
    private void stockerProduit(Produit produit) {
        TypeBox typeBox = trouverTypeBox(produit);
        BoxAchete boxAchete = acheterBox(typeBox);
        Pile pile = empiler(produit, boxAchete);
        
        boxAchete.getPileCollection().add(pile);
        boxAcheteDao.update(boxAchete);

        typeBox.getBoxAcheteCollection().add(boxAchete);
        typeBoxDao.update(typeBox);

        produit.setIdPile(pile);
        produitDao.update(produit);
    }
    
    /**
     * Fonction permettant de trouver le type de box approprié au produit
     * @param produit Produit
     * @return TypeBox
     */
    private TypeBox trouverTypeBox(Produit produit) {
        if (produit.getIdProduitCommande() != null 
                && produit.getIdProduitCommande().getIdTypeProduit() != null){
            TypeProduit typeProduit = produit.getIdProduitCommande().getIdTypeProduit();
            return typeBoxDao.findFirstByDimensions(typeProduit.getLongueur(), typeProduit.getHauteur());
        } 
        return null;
    }

    /**
     * Fonction permettant d'acheter le box
     * @param typeBox TypeBox
     * @return BoxAchete
     */
    private BoxAchete acheterBox(TypeBox typeBox) {
        BoxAchete boxAchete = new BoxAchete();
        boxAchete.setIdTypeBox(typeBox);
        boxAchete.setNumBox(boxAcheteDao.countBoxes(typeBox)+1);
        boxAcheteDao.create(boxAchete);
        return boxAchete;
    }

    /**
     * Fonction permettant d'empiler
     * @param produit Produit
     * @param boxAchete BoxAchete
     * @return Pile
     */
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
        
    /**
     * Liberer les boxs de la commande
     * @param commande Commande
     */    
    private void libererBoxes(Commande commande) {
        commande.getProduitCommandeCollection().stream().forEach((produitCommande)->{
            produitCommande.getProduitCollection().stream().forEach((produit)->{
                BoxAchete boxAchete = produit.getIdPile().getIdBoxAchete();
                boxAchete.setLibre(0);
                boxAchete.setDLibre(dateActuelleBox);
                boxAcheteDao.update(boxAchete);
            });
        });
    }
    
    /**
     * Fonction permettant de verifier si la ligne de production a besoin
     * d'un temps de setup
     * @param ligneProduction LigneProduction
     * @param typeProduit TypeProduit
     * @return True si elle a besoin d'un temps de setup, sinon False
     */
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
