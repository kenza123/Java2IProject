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
    
}
