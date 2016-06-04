/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import dao.DaoFactory;
import dao.JpaDaoFactory;
import dao.JpaDaoProduitCommande;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.BoxAchete;
import model.Commande;
import model.CommandeBox;
import model.Produit;
import model.ProduitCommande;

/**
 *
 * @author aBennouna
 */
@Named(value = "commandeControl")
@SessionScoped
public class CommandeControl implements Serializable {

    /**
     * Creates a new instance of NewJSFManagedBean
     */
    private Commande commande;
    private List<BoxAchete> boxAchetes;

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
    
    public CommandeControl() {
    }

    public List<BoxAchete> getBoxAchetes() {
        return boxAchetes;
    }

    public void setBoxAchetes(List<BoxAchete> boxAchetes) {
        this.boxAchetes = boxAchetes;
    }
    
    public List<Produit> getProduitsAchetes(BoxAchete boxAchete) {
        List<Produit> produits = new ArrayList();
        boxAchete.getPileCollection().stream().forEach((pile) -> {
            pile.getProduitCollection().stream().forEach((produit) -> {
                if(!produits.contains(produit)) {
                    produits.add(produit);
                }
            });
        });
        return produits;
    }
    
    public String showPage(Commande commande) {
        this.commande = commande;
        this.boxAchetes = new ArrayList<>();
        Collection<BoxAchete> boxes = new ArrayList<>();
        commande.getCommandeBoxCollection().stream().forEach((commandeBox)->{
            System.out.println(commandeBox.getIdBoxAchete().getIdTypeBox().getId() + "_" + commandeBox.getIdBoxAchete().getNumBox());
            if(!boxes.contains(commandeBox.getIdBoxAchete())) {
                boxes.add(commandeBox.getIdBoxAchete()); 
            }
        });
        this.boxAchetes.addAll(boxes);
        return "commande?faces-redirect=true";
    }
    
    public String getDebutProd(BoxAchete boxAchete) {
        return "check commandeControl.getDebutProd()";
    }
    
    public List<Produit> getProducts() {
        List<Produit> produits = new ArrayList<>();
        JpaDaoFactory jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoProduitCommande jpaDaoProduitCommande  = jpaDaoFactory.getProduitCommandeDao();
        for(ProduitCommande produitCommande :jpaDaoProduitCommande.findProductsOfCommande(commande)) {
            for(Produit produit : produitCommande.getProduitCollection()) {
                if(!produits.contains(produit)) {
                    produits.add(produit);
                }
            }
        }
        return produits;
    }
    
    
    public Boolean productExist(Produit produit) {
        return getProducts().contains(produit);
    }
    
}
