/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import model.Commande;
import model.ProduitCommande;

/**
 *
 * @author aBennouna
 */
public interface ProduitCommandeDao extends Dao<ProduitCommande> {
    
    public Collection<ProduitCommande> findProductsOfCommande(Commande c);
    
}
