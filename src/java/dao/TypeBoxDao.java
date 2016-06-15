/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.TypeBox;

public interface TypeBoxDao extends Dao<TypeBox> {
    
    /**
     * Trouver une box avec ses dimensions
     * @param lbox Longueur produit
     * @param hbox Hauteur produit
     * @return Box adequat
     */
    public TypeBox findFirstByDimensions(int lbox, int hbox);
    
}
