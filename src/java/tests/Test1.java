/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import model.TypeBox;

/**
 *
 * @author aBennouna
 */
public class Test1 {
       public static void main(String[] args) { 
        final EntityManagerFactory emf = 
                Persistence.createEntityManagerFactory("Java_2I_ProjectPU");
        final EntityManager em = emf.createEntityManager();
        try
        {
            final EntityTransaction et = em.getTransaction(); 
            try
            {
                et.begin();
                TypeBox serv1 = new TypeBox("Cardiologie", 2, 3, 3);
                em.persist(serv1);
                et.commit();
            }   
            catch (Exception ex) 
            { 
                et.rollback();
            }
        } 
        finally 
        {
            if(em != null && em.isOpen())
            {
                em.close();
            } 
            if(emf != null && emf.isOpen())
            {  
                emf.close();
            }
        }
   }
}
