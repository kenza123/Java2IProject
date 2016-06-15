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
import model.BoxAchete;
import model.Commande;
import model.LigneProduction;
import model.Pile;
import model.Produit;
import model.ProduitCommande;
import model.TypeBox;
import model.TypeProduit;

public class TrivialSolution {

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

    public TrivialSolution() {
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
     * 
     */
    public void execute() {
        Collection<Commande> commandes = commandeDao.findAllOrderByDenvoiprevue();
        commandes.stream().forEach((commande) -> {
            commande.getProduitCommandeCollection().stream().forEach((produitCommande) -> {
                produireProduitCommande(produitCommande);
            });
            dateActuelleBox += commande.getStockmin();
            if (dateActuelleBox < commande.getDenvoiprevue()) {
                dateActuelleBox = commande.getDenvoiprevue();
            }
            libererBoxes(commande);
            commande.setDenvoireel(dateActuelleBox);
            commandeDao.update(commande);
        });
    }
   
    /**
     * 
     * @param produitCommande 
     */
    public void produireProduitCommande(ProduitCommande produitCommande){
        LigneProduction ligneProduction = choisirLigneProduction();
        TypeProduit typeProduit = produitCommande.getIdTypeProduit();
        if (typeProduit != null) {
            //verif if necesite setup time
            dateActuelleProduction += typeProduit.getTSetup();
            for (int i = 0; i < produitCommande.getNbUnites(); i++) {
                Produit produit = produireProduit(produitCommande, ligneProduction);

                ligneProduction.getProduitCollection().add(produit);
                ligneProductionDao.update(ligneProduction);

                produitCommande.getProduitCollection().add(produit);
                produitCommandeDao.update(produitCommande);

                dateActuelleProduction += typeProduit.getTProduction();

                dateActuelleBox = dateActuelleProduction;
                stockerProduit(produit);
            }
        }
    }

    /**
     * 
     * @param produitCommande
     * @param ligneProduction
     * @return 
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
     * 
     * @return 
     */
    private LigneProduction choisirLigneProduction() {
        return ligneProductionDao.findAll().iterator().next();
    }

    /**
     * 
     * @param produit 
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
     * 
     * @param produit
     * @return 
     */
    private TypeBox trouverTypeBox(Produit produit) {
        if (produit.getIdProduitCommande() != null && produit.getIdProduitCommande().getIdTypeProduit() != null) {
            TypeProduit typeProduit = produit.getIdProduitCommande().getIdTypeProduit();
            return typeBoxDao.findFirstByDimensions(typeProduit.getLongueur(), typeProduit.getHauteur());
        }
        return null;
    }

    /**
     * 
     * @param typeBox
     * @return 
     */
    private BoxAchete acheterBox(TypeBox typeBox) {
        BoxAchete boxAchete = new BoxAchete();
        boxAchete.setIdTypeBox(typeBox);
        boxAchete.setNumBox(boxAcheteDao.countBoxes(typeBox) + 1);
        boxAchete.setLibre(1);
        boxAcheteDao.create(boxAchete);
        return boxAchete;
    }

    /**
     * 
     * @param produit
     * @param boxAchete
     * @return 
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
     * 
     * @param commande 
     */
    private void libererBoxes(Commande commande) {
        commande.getProduitCommandeCollection().stream().forEach((produitCommande)->{
            produitCommande.getProduitCollection().stream().forEach((produit)->{
                BoxAchete boxAchete = produit.getIdPile().getIdBoxAchete();
                boxAchete.setLibre(0);
                boxAcheteDao.update(boxAchete);
            });
        });
    }
    
}
