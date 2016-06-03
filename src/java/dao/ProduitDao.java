/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.LigneProduction;
import model.Produit;

/**
 *
 * @author aBennouna
 */
public interface ProduitDao extends Dao<Produit> {
    public Produit findLastProductInLine(LigneProduction line);
    
    public List<Produit> findByIdLineProduct(LigneProduction line);
    
}
