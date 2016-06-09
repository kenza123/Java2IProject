/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import javax.persistence.EntityTransaction;
import model.LigneProduction;

/**
 *
 * @author aBennouna
 */
public class JpaDaoLigneProduction extends JpaDao<LigneProduction> implements LigneProductionDao {

    private static JpaDaoLigneProduction instance;
    
    private JpaDaoLigneProduction() {
        
    }
    
    public static void setInstance(JpaDaoLigneProduction instance) {
        JpaDaoLigneProduction.instance = instance;
    }
    
    protected static JpaDaoLigneProduction getInstance() {
        if(instance == null)
            instance = new JpaDaoLigneProduction();
        return instance;
    }

    @Override
    public LigneProduction find(long id) {
        return em.find(LigneProduction.class, Integer.valueOf((int) id));
    }

    @Override
    public Collection<LigneProduction> findAll() {        
        return em.createNamedQuery("LigneProduction.findAll").getResultList();
    }

    @Override
    public void deleteAll() {
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createNamedQuery("LigneProduction.deleteAll").executeUpdate();
        et.commit();
    }

    @Override
    public void close() {
        em.close();
    }
    
}
