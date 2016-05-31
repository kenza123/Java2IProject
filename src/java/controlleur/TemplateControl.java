/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import dao.DaoFactory;
import dao.JpaDaoCommande;
import dao.JpaDaoFactory;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import model.Commande;

/**
 *
 * @author kenzakhamaily
 */
@Named(value = "templateControl")
@SessionScoped
public class TemplateControl implements Serializable {
   
    
    
    /**
     * Creates a new instance of CommandeControl
     */
    public TemplateControl() {
    }
    
    public Collection<Commande> getCommandeList(){
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoCommande jdc = jdf.getCommandeDao();
        return jdc.findAll();
    }
    
    public String goToStats(){
        return "stats";
    }
    
    public String goToTests(){
        return "test";
    }
    
    public String goToGantt(){
        return "gantt";
    }
    
    
    
}
