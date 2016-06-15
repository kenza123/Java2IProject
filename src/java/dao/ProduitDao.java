/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import java.util.List;
import model.LigneProduction;
import model.Pile;
import model.Produit;

public interface ProduitDao extends Dao<Produit> {
    
    /**
     * Fonction permettant d'afficher le dernier produit de la ligne de production
     * @param line Ligne de production
     * @return Produit
     */
    public Produit findLastProductInLine(LigneProduction line);
    
    /**
     * Fonction permettant d'afficher les produits de la ligne de production
     * @param line Ligne de production
     * @return Produits
     */
    public List<Produit> findByIdLineProduct(LigneProduction line);
    
}
