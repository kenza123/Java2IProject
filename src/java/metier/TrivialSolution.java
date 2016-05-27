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
import java.util.ArrayList;
import java.util.Collection;
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

    public TrivialSolution() {
        dateActuelleProduction = 0;
        dateActuelleBox=0;
        jpaDaoFactory = 
            (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        commandeDao = jpaDaoFactory.getCommandeDao();
        produitCommandeDao = jpaDaoFactory.getProduitCommandeDao();
        ligneProductionDao = jpaDaoFactory.getLigneProductionDao();
        produitDao = jpaDaoFactory.getProduitDao();
        typeBoxDao = jpaDaoFactory.getTypeBoxDao();
        boxAcheteDao = jpaDaoFactory.getBoxAcheteDao();
    }
    
    public void execute(){
        Collection<Commande> commandes = commandeDao.findAllOrderByDenvoiprevue();
        commandes.stream().forEach((commande) -> {
            commande.getProduitCommandeCollection().stream().forEach((produitCommande) -> {
                produireProduitCommande(produitCommande);
                dateActuelleBox = dateActuelleProduction;
                StockerProduitCommande(produitCommande);
            });
            dateActuelleBox =+ commande.getStockmin();
            if (dateActuelleBox < commande.getDenvoiprevue()){
                dateActuelleBox = commande.getDenvoiprevue();
            }
            libererBoxes(commande);
            commande.setDenvoireel(dateActuelleBox);
            commandeDao.update(commande);
        });
    }
   
    public void produireProduitCommande(ProduitCommande produitCommande){
        LigneProduction ligneProduction = ChoisirLigneProduction();
        TypeProduit typeProduit = produitCommande.getIdTypeProduit();
        if (typeProduit != null){
            dateActuelleProduction += typeProduit.getTSetup();
            for(int i = 0; i< produitCommande.getNbUnites(); i++){
                Produit produit = produireProduit(produitCommande, ligneProduction);
                
                ligneProduction.getProduitCollection().add(produit);
                ligneProductionDao.update(ligneProduction);
                
                produitCommande.getProduitCollection().add(produit);
                produitCommandeDao.update(produitCommande);
                
                dateActuelleProduction += typeProduit.getTProduction();
            }
        }
    }

    public Produit produireProduit(ProduitCommande produitCommande, LigneProduction ligneProduction){
        Produit produit = new Produit();
        produit.setIdProduitCommande(produitCommande);
        produit.setDateDebutProd(dateActuelleProduction);
        produit.setNblignes(ligneProduction);
        produitDao.create(produit);
        return produit;
    }
    
    private LigneProduction ChoisirLigneProduction() {
        return ligneProductionDao.findAll().iterator().next();
    }

    private void StockerProduitCommande(ProduitCommande produitCommande) {
        produitCommande.getProduitCollection().stream().forEach((produit) -> {
            TypeBox typeBox = trouverTypeBox(produit);
            BoxAchete boxAchete = acheterBox(typeBox);
            
            boxAchete.getProduitCollection().add(produit);
            boxAchete.setIdCommande(produitCommande.getIdCommande());
            boxAcheteDao.update(boxAchete);
            
            typeBox.getBoxAcheteCollection().add(boxAchete);
            typeBoxDao.update(typeBox);
            
            produit.setIdBox(boxAchete);
            produitDao.update(produit);
            
            produitCommande.getIdCommande().getBoxAcheteCollection().add(boxAchete);
            produitCommandeDao.update(produitCommande);
        });
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

    private void libererBoxes(Commande commande) {
        commande.getBoxAcheteCollection().stream().forEach((boxAchete) -> {
            boxAchete.setIdCommande(null);
            boxAchete.getProduitCollection().stream().forEach((produit) -> {
                produit.setIdBox(null);
                produitDao.update(produit);
            });
            boxAchete.setProduitCollection(new ArrayList());
            boxAcheteDao.update(boxAchete);
        });
        commande.setBoxAcheteCollection(new ArrayList());
        commandeDao.update(commande);
    }
}
  