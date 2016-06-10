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
public class JpaDaoTypeBox extends JpaDao<TypeBox> implements TypeBoxDao {

    private static JpaDaoTypeBox instance;
    
    private JpaDaoTypeBox() {
        
    }

    public static void setInstance(JpaDaoTypeBox instance) {
        JpaDaoTypeBox.instance = instance;
    }
    
    protected static JpaDaoTypeBox getInstance() {
        if(instance == null)
            instance = new JpaDaoTypeBox();
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
    
    @Override
    public TypeBox findFirstByDimensions(int lbox, int hbox) {
        return (TypeBox) em.createNamedQuery("TypeBox.findFirstByDimensions")
                .setParameter("lbox", lbox)
                .setParameter("hbox", hbox)
                .getResultList().get(0);
    }
}
