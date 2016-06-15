/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import model.Commande;
import model.ProduitCommande;

public interface ProduitCommandeDao extends Dao<ProduitCommande> {
    
    /**
     * Fonction permettant d'avoir les produits de la commande
     * @param c Commande
     * @return Produits de la commande fournie
     */
    public Collection<ProduitCommande> findProductsOfCommande(Commande c);
    
}
