/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import javax.persistence.EntityTransaction;
import model.Pile;

/**
 *
 * @author aBennouna
 */
public class JpaDaoPile extends JpaDao<Pile> implements PileDao {

    private static JpaDaoPile instance;
    
    private JpaDaoPile() {
        
    }

    public static void setInstance(JpaDaoPile instance) {
        JpaDaoPile.instance = instance;
    }
    
    
    
    protected static JpaDaoPile getInstance() {
        if(instance == null)
            instance = new JpaDaoPile();
        return instance;
    }

    @Override
    public Pile find(long id) {
        return em.find(Pile.class, Integer.valueOf((int) id));
    }

    @Override
    public Collection<Pile> findAll() {        
        return em.createNamedQuery("Pile.findAll").getResultList();
    }

    @Override
    public void deleteAll() {
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createNamedQuery("Pile.deleteAll").executeUpdate();
        et.commit();
    }

    @Override
    public void close() {
        em.close();
    }
    
}
