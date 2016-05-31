/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import model.Commande;

/**
 *
 * @author aBennouna
 */
@Named(value = "commandeControl")
@SessionScoped
public class CommandeControl implements Serializable {

    /**
     * Creates a new instance of NewJSFManagedBean
     */
    private Commande commande;

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
    
    public CommandeControl() {
    }
    
    public String showPage(Commande commande) {
        this.commande = commande;
        return "commande";
    }
    
}
