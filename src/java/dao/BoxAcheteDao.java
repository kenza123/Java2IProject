/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import model.BoxAchete;
import model.TypeBox;

public interface BoxAcheteDao extends Dao<BoxAchete> {
    
    /**
     * Trouver le nombre de boxs achetes du meme type
     * @param typeBox Type de Box
     * @return Nombre de boxs du type donné en entrée
     */
    public int countBoxes(TypeBox typeBox);
    
    /**
     * Trouver toutes les box ordonnées par idTypeBox
     * @return Collections de boxs achetes
     */
    public Collection<BoxAchete> findAllOrdered();
    
    /**
     * Trouver les boxs achetes du meme type
     * @param typeBox Type de Box
     * @return Collection de Boxs Achetes du meme type
     */
    public Collection<BoxAchete> findBoxesByTypeBox(TypeBox typeBox);
    
}
