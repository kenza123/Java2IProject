/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityTransaction;
import model.LigneProduction;
import model.Pile;
import model.Produit;

public class JpaDaoProduit extends JpaDao<Produit> implements ProduitDao {

    private static JpaDaoProduit instance;
    
    private JpaDaoProduit() {
        
    }

    public static void setInstance(JpaDaoProduit instance) {
        JpaDaoProduit.instance = instance;
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
    
    @Override
    public Produit findLastProductInLine(LigneProduction line){
        Vector<Produit> produits = (Vector<Produit>)
                em.createNamedQuery("Produit.findLastProductInLine")
                .setParameter("line", line).getResultList();
        
        return (produits.isEmpty()) ? null : produits.get(0);
   }
    
   @Override
   public List<Produit> findByIdLineProduct(LigneProduction line){
       return em.createNamedQuery("Produit.findByIdLineProduct").setParameter("line", line).getResultList();
   }
    
}
