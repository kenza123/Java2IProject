/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import model.Commande;

/**
 *
 * @author aBennouna
 */
public interface CommandeDao extends Dao<Commande> {
    
    public Collection<Commande> findAllOrderByDenvoiprevue();
    public Collection<Commande> findAllOrderByDenvoireelle();
    public Commande findCommandeByName(String name);
    
}
