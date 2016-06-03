/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import javax.persistence.EntityTransaction;
import model.CommandeBox;

/**
 *
 * @author aBennouna
 */
public class JpaDaoCommandeBox extends JpaDao<CommandeBox> implements CommandeBoxDao {

    private static JpaDaoCommandeBox instance;
    
    private JpaDaoCommandeBox() {
        
    }
    
    protected static JpaDaoCommandeBox getInstance() {
        if(instance == null)
            instance = new JpaDaoCommandeBox();
        return instance;
    }

    @Override
    public CommandeBox find(long id) {
        return em.find(CommandeBox.class, Integer.valueOf((int) id));
    }

    @Override
    public Collection<CommandeBox> findAll() {        
        return em.createNamedQuery("CommandeBox.findAll").getResultList();
    }

    @Override
    public void deleteAll() {
        EntityTransaction et = em.getTransaction();
        
        et.begin();
        em.createNamedQuery("CommandeBox.deleteAll").executeUpdate();
        et.commit();
    }

    @Override
    public void close() {
        em.close();
    }
    
}
