/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.TypeProduit;

/**
 *
 * @author aBennouna
 */
public interface TypeProduitDao extends Dao<TypeProduit> {
    
    public TypeProduit findById(String id);
    
}
