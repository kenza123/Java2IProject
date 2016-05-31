/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import javax.persistence.EntityTransaction;
import model.Commande;

/**
 *
 * @author aBennouna
 */
public class JpaDaoCommande extends JpaDao<Commande> implements CommandeDao {

    private static JpaDaoCommande instance;
    
    private JpaDaoCommande() {
        
    }
    
    protected static JpaDaoCommande getInstance() {
        if(instance == null)
            instance = new JpaDaoCommande();
        return instance;
    }

    @Override
    public Commande find(long id) {
        return em.find(Commande.class, Integer.valueOf((int) id));
    }

    @Override
    public Collection<Commande> findAll() {        
        return em.createNamedQuery("Commande.findAll").getResultList();
    }

    @Override
    public void deleteAll() {
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createNamedQuery("Commande.deleteAll").executeUpdate();
        et.commit();
    }

    @Override
    public void close() {
        em.close();
    }

    @Override
    public Collection<Commande> findAllOrderByDenvoiprevue() {
        return em.createNamedQuery("Commande.findAllOrderBYDenvoiprevue").getResultList();
    }
    
    @Override
    public Collection<Commande> findAllOrderByDenvoireelle() {
        return em.createNamedQuery("Commande.findAllOrderBYDenvoireelle").getResultList();
    }
    
    @Override
    public Collection<Commande> findAllOrderBYPenalite() {
        return em.createNamedQuery("Commande.findAllOrderBYPenalite").getResultList();
    }
    
    @Override
    public Collection<Commande> findAllOrderBYStock() {
        return em.createNamedQuery("Commande.findAllOrderBYStock").getResultList();
    }
    
    @Override
    public Collection<Commande> findAllOrderBYId() {
        return em.createNamedQuery("Commande.findAllOrderBYId").getResultList();
    }
    
    @Override
    public Commande findCommandeByName(String name) {
        return (Commande)em.createNamedQuery("Commande.findById").setParameter("id", name).getResultList().iterator().next();
    }
    
}
