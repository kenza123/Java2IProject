/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import javax.persistence.EntityTransaction;
import model.TypeBox;

/**
 *
 * @author aBennouna
 */
public class JpaDaoTypeProduit extends JpaDao<TypeBox> implements TypeBoxDao {

    private static JpaDaoTypeProduit instance;
    
    private JpaDaoTypeProduit() {
        
    }
    
    protected static JpaDaoTypeProduit getInstance() {
        if(instance == null)
            instance = new JpaDaoTypeProduit();
        return instance;
    }

    @Override
    public TypeBox find(long id) {
        return em.find(TypeBox.class, Integer.valueOf((int) id));
    }

    @Override
    public Collection<TypeBox> findAll() {        
        return em.createNamedQuery("TypeBox.findAll").getResultList();
    }

    @Override
    public void deleteAll() {
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createNamedQuery("TypeBox.deleteAll").executeUpdate();
        et.commit();
    }

    @Override
    public void close() {
        em.close();
    }
    
}
