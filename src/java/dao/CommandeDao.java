/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import model.Commande;

public interface CommandeDao extends Dao<Commande> {
    
     /**
     * Trouver les commandes ordonnées par date d'envoi prevue
     * @return Commandes ordonnées
     */
    public Collection<Commande> findAllOrderByDenvoiprevue();
    
    /**
     * Trouver les commandes ordonnées par date d'envoi relle
     * @return Commandes ordonnées
     */
    public Collection<Commande> findAllOrderByDenvoireelle();
    
    /**
     * Trouver la commande avec son nom
     * @param name Nom de commande
     * @return Commande
     */
    public Commande findCommandeByName(String name);
    
}
