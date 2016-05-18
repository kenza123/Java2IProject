/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import javax.persistence.EntityTransaction;
import model.TypeProduit;

/**
 *
 * @author aBennouna
 */
public class JpaDaoTypeProduit extends JpaDao<TypeProduit> implements TypeProduitDao {

    private static JpaDaoTypeProduit instance;
    
    private JpaDaoTypeProduit() {
        
    }
    
    protected static JpaDaoTypeProduit getInstance() {
        if(instance == null)
            instance = new JpaDaoTypeProduit();
        return instance;
    }

    @Override
    public TypeProduit find(long id) {
        return em.find(TypeProduit.class, Integer.valueOf((int) id));
    }

    @Override
    public Collection<TypeProduit> findAll() {        
        return em.createNamedQuery("TypeProduit.findAll").getResultList();
    }

    @Override
    public void deleteAll() {
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createNamedQuery("TypeProduit.deleteAll").executeUpdate();
        et.commit();
    }

    @Override
    public void close() {
        em.close();
    }

    @Override
    public TypeProduit findById(String id) {
        return (TypeProduit)
                em.createNamedQuery("TypeProduit.findById")
                        .setParameter("id", id).getResultList().get(0);
    }
    
}
