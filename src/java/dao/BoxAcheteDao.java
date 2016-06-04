/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import model.BoxAchete;
import model.TypeBox;

/**
 *
 * @author aBennouna
 */
public interface BoxAcheteDao extends Dao<BoxAchete> {
    public int countBoxesByTypeBox(TypeBox typeBox);
    public int countBoxesById(TypeBox typeBox);
    public Collection<BoxAchete> findAllOrdered();
    public Collection<BoxAchete> findBoxesByTypeBox(TypeBox typeBox);
    
}
