/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author aBennouna
 * @param <T>
 */
public abstract class JpaDao<T> implements Dao<T> {

    protected EntityManager em;
    protected EntityManagerFactory emf; 
    
    protected final static String PERSISTENCE_UNIT = "Java_2I_ProjectPU";
    
    public JpaDao() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        em = emf.createEntityManager();
    }
    
    @Override
    public abstract T find(long id);
            
    @Override
    public boolean create(T t) {
        EntityTransaction et = em.getTransaction();
        
        try {
            et.begin();
            em.persist(t);
            et.commit();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(T t) {
        EntityTransaction et = em.getTransaction();
        
        try {
            et.begin();
            em.merge(t);
            et.commit();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(T t) {
        EntityTransaction et = em.getTransaction();
        
        try {
            et.begin();
            em.remove(t);
            et.commit();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void close (T t) {
        if(em != null && em.isOpen()) {
            em.close();
        } 
        if(emf != null && emf.isOpen()) {  
            emf.close();
        }
    }
    
}
