/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import dao.DaoFactory;
import dao.JpaDaoBoxAchete;
import dao.JpaDaoCommandeBox;
import dao.JpaDaoFactory;
import dao.JpaDaoTypeBox;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import model.BoxAchete;
import model.CommandeBox;
import model.Pile;
import model.Produit;
import model.ProduitCommande;
import model.TypeBox;
/**
 *
 * @author ohilmi
 */
@Named(value = "boxControl")
@SessionScoped
public class BoxControl implements Serializable{
    private BoxAchete box;

    public BoxAchete getBox() {
        return box;
    }

    public void setBox(BoxAchete box) {
        this.box = box;
    }

    public BoxControl() {
    }

    public BoxControl(BoxAchete box) {
        this.box = box;
    }
    
    public List<BoxAchete> getBoxAchetesList(TypeBox typeBox){
        List<BoxAchete> boxAchetes = new ArrayList<>();
        JpaDaoFactory jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoBoxAchete jpaDaoBoxAchete  = jpaDaoFactory.getBoxAcheteDao();
        boxAchetes.addAll(jpaDaoBoxAchete.findBoxesByTypeBox(typeBox));
        return boxAchetes;
    }
    
    public List<TypeBox> getBoxList(){
        List<TypeBox> typeBoxes = new ArrayList<>();
        JpaDaoFactory jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoTypeBox jpaDaoTypeBox  = jpaDaoFactory.getTypeBoxDao();
        typeBoxes.addAll(jpaDaoTypeBox.findAll());
        return typeBoxes;
    }
    
    public List<Produit> getProducts(BoxAchete boxAchete) {
        List<Produit> produits = new ArrayList<>();
        for(Pile pile : boxAchete.getPileCollection()) {
            for(Produit produit : pile.getProduitCollection()) {
                if(!produits.contains(produit)) {
                    produits.add(produit);
                }
            }
        }
        return produits;
    }
    
    public int getBoxLength(BoxAchete boxAchete) {
        List<Produit> produits = getProducts(boxAchete);
        List<Integer> dateDebutsProds = new ArrayList<>();
        for(Produit produit : produits) {
            dateDebutsProds.add(produit.getDateDebutProd() 
                    + produit.getIdProduitCommande().getIdTypeProduit().getTProduction());
        }
        return Collections.max(dateDebutsProds);
    }
    
}
