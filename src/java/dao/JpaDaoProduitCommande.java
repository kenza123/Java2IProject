/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import javax.persistence.EntityTransaction;
import model.ProduitCommande;

/**
 *
 * @author aBennouna
 */
public class JpaDaoProduitCommande extends JpaDao<ProduitCommande> implements ProduitCommandeDao {

    private static JpaDaoProduitCommande instance;
    
    private JpaDaoProduitCommande() {
        
    }
    
    protected static JpaDaoProduitCommande getInstance() {
        if(instance == null)
            instance = new JpaDaoProduitCommande();
        return instance;
    }

    @Override
    public ProduitCommande find(long id) {
        return em.find(ProduitCommande.class, Integer.valueOf((int) id));
    }

    @Override
    public Collection<ProduitCommande> findAll() {        
        return em.createNamedQuery("ProduitCommande.findAll").getResultList();
    }

    @Override
    public void deleteAll() {
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createNamedQuery("ProduitCommande.deleteAll").executeUpdate();
        et.commit();
    }

    @Override
    public void close() {
        em.close();
    }
    
}
