/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import javax.persistence.EntityTransaction;
import model.BoxAchete;

/**
 *
 * @author aBennouna
 */
public class JpaDaoBoxAchete extends JpaDao<BoxAchete> implements BoxAcheteDao {

    private static JpaDaoBoxAchete instance;
    
    private JpaDaoBoxAchete() {
        
    }
    
    protected static JpaDaoBoxAchete getInstance() {
        if(instance == null)
            instance = new JpaDaoBoxAchete();
        return instance;
    }

    @Override
    public BoxAchete find(long id) {
        return em.find(BoxAchete.class, Integer.valueOf((int) id));
    }

    @Override
    public Collection<BoxAchete> findAll() {        
        return em.createNamedQuery("BoxAchete.findAll").getResultList();
    }

    @Override
    public void deleteAll() {
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createNamedQuery("BoxAchete.deleteAll").executeUpdate();
        et.commit();
    }
    
    public int countBoxesById(String idTypeBox){
        return (int) em.createNamedQuery("BoxAchete.countBoxesById")
                .setParameter("idTypeBox", idTypeBox)
                .getResultList().get(0);
    }

    @Override
    public void close() {
        em.close();
    }
    
}
