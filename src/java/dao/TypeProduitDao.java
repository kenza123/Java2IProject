/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.TypeProduit;

public interface TypeProduitDao extends Dao<TypeProduit> {
    
    /**
     * Trouver le type produit par son Id
     * @param id Id type produit
     * @return TypeProduit
     */
    public TypeProduit findById(String id);
    
}
