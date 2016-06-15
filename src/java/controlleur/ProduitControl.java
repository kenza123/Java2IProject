/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import dao.DaoFactory;
import dao.JpaDaoFactory;
import dao.JpaDaoProduit;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import model.LigneProduction;
import model.Produit;

@Named(value = "produitControl")
@SessionScoped
public class ProduitControl implements Serializable {
    private List<Produit> produits;
    /**
     * Creates a new instance of ProduitControl
     */
    public ProduitControl() {
    }
    
    public List<Produit> getProduits(LigneProduction ligne) {
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoProduit jdp = jdf.getProduitDao();
        produits = jdp.findByIdLineProduct(ligne);
        for(Produit produit : produits) {
            if(produit.getIdProduitCommande().getIdTypeProduit().getHauteur() 
                    > produit.getIdPile().getIdBoxAchete().getIdTypeBox().getHbox()) {
                System.out.println(produit + " Hauteur du produit trop grande pour la box");
            }
            if(produit.getIdProduitCommande().getIdTypeProduit().getLongueur()
                    > produit.getIdPile().getIdBoxAchete().getIdTypeBox().getLbox()) {
                System.out.println(produit + " Longueur du produit trop grande pour la box");
            }
        }
        return produits;
    }
    
}
