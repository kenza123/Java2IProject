/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import dao.DaoFactory;
import dao.JpaDaoFactory;
import dao.TypeProduitDao;
import model.TypeProduit;


/**
 *
 * @author aBennouna
 */
public class Test1 {
       public static void main(String[] args) { 
            JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
            TypeProduitDao jpt = jdf.getTypeProduitDao();
           
            TypeProduit p = new TypeProduit();
            p.setId("BOX01");
            p.setHauteur(2);
            p.setLongueur(3);
            p.setNbempilemax(2);
            jpt.create(p);
           
       }
   }
