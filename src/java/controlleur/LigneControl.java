/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import dao.DaoFactory;
import dao.JpaDaoFactory;
import dao.JpaDaoLigneProduction;
import dao.JpaDaoProduit;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Collection;
import model.LigneProduction;
import model.Produit;

@Named(value = "ligneControl")
@SessionScoped
public class LigneControl implements Serializable{
    private LigneProduction ligne;
    private int taille;

    public LigneProduction getLigne() {
        return ligne;
    }

    public void setLigne(LigneProduction ligne) {
        this.ligne = ligne;
    }

    public LigneControl() {
    }

    public LigneControl(LigneProduction ligne) {
        this.ligne = ligne;
    }

    public Collection<LigneProduction> getLigneList(){
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoLigneProduction jdlp = jdf.getLigneProductionDao();
        return jdlp.findAll();
    }
    
    public int getTaille(LigneProduction ligne) {
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoProduit jdp = jdf.getProduitDao();
        Produit p = jdp.findLastProductInLine(ligne);
        if (p!= null) {
           taille = p.getDateDebutProd() + p.getIdProduitCommande().getIdTypeProduit().getTProduction();
         } else {
            taille = 0;
        }
        return taille;
    }
    
}
