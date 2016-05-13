/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import javax.persistence.EntityTransaction;
import model.Produit;

/**
 *
 * @author aBennouna
 */
public class JpaDaoProduit extends JpaDao<Produit> implements ProduitDao {

    private static JpaDaoProduit instance;
    
    private JpaDaoProduit() {
        
    }
    
    protected static JpaDaoProduit getInstance() {
        if(instance == null)
            instance = new JpaDaoProduit();
        return instance;
    }

    @Override
    public Produit find(long id) {
        return em.find(Produit.class, Integer.valueOf((int) id));
    }

    @Override
    public Collection<Produit> findAll() {        
        return em.createNamedQuery("Produit.findAll").getResultList();
    }

    @Override
    public void deleteAll() {
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createNamedQuery("Produit.deleteAll").executeUpdate();
        et.commit();
    }

    @Override
    public void close() {
        em.close();
    }
    
}
