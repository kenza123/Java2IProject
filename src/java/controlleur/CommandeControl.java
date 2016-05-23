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
@Named(value = "commandeControl")
@SessionScoped
public class CommandeControl implements Serializable {
    private Commande commande;
    private String nextPage;

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
    
    
    /**
     * Creates a new instance of CommandeControl
     */
    public CommandeControl() {
    }
    
    public Collection<Commande> getCommandeList(){
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoCommande jdc = jdf.getCommandeDao();
        return jdc.findAll();
    }
    
    public String goToCommande (Commande commande) {
        this.commande = commande;
        return "commande";
    }
    
    
    
}
