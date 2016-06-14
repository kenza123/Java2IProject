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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import model.BoxAchete;
import model.Commande;
import model.LigneProduction;
import model.Pile;
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
        LigneControl ligneControl = new LigneControl();
        for(LigneProduction ligneProduction : ligneControl.getLigneList()) {
            ProduitControl produitControl = new ProduitControl();
            for(Produit produit : produitControl.getProduits(ligneProduction)) {
                if(Objects.equals(produit.getIdPile().getIdBoxAchete().getId(), boxAchete.getId()) && this.productExist(produit)) {
                    produits.add(produit);
                }
            }
        }
        return produits;
    }
    
    public String showPage(Commande commande) {
        this.commande = commande;
        this.boxAchetes = new ArrayList<>();
        Collection<BoxAchete> boxes = new ArrayList<>();
        commande.getProduitCommandeCollection().stream().forEach((produitCommande)->{
            produitCommande.getProduitCollection().stream().forEach((produit)->{
                if(!boxes.contains(produit.getIdPile().getIdBoxAchete())) {
                    boxes.add(produit.getIdPile().getIdBoxAchete()); 
                }
             });
        });
        
        boxes.stream().forEach((box)->{
            System.out.println("box :" + box.toString());
            System.out.println(box.getIdTypeBox().toString());
            box.getPileCollection().stream().forEach((pile) -> {
                List <Produit> produits = new ArrayList<>();
                produits.addAll(pile.getProduitCollection());
                Commande commandePile = produits.get(0).getIdProduitCommande().getIdCommande();
                if(commandePile.equals(commande)) {
                    System.out.println(pile.toString());
                }
            });
        });
        this.boxAchetes.addAll(boxes);
        return "commande?faces-redirect=true";
    }
    
    public String getDebutProd(BoxAchete boxAchete) {
        List<Integer> debutProds = new ArrayList<>();
        for(Produit produit : this.getProduitsAchetes(boxAchete)) {
            debutProds.add(produit.getDateDebutProd());
        }
        Collections.sort(debutProds);
        return debutProds.get(0).toString();
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
