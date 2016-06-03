/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.BoxAchete;
import model.Commande;
import model.Produit;

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
    private List<BoxAchete> boxAchetes;

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }
    
    public CommandeControl() {
    }

    public List<BoxAchete> getBoxAchetes() {
        return boxAchetes;
    }

    public void setBoxAchetes(List<BoxAchete> boxAchetes) {
        this.boxAchetes = boxAchetes;
    }
    
    public List<Produit> getProduitsAchetes(BoxAchete boxAchete) {
        List<Produit> produits = new ArrayList();
        boxAchete.getPileCollection().stream().forEach((pile) -> {
        produits.addAll(pile.getProduitCollection());
        });
        return produits;
    }
    
    
    
    public String showPage(Commande commande) {
        this.commande = commande;
        this.boxAchetes = new ArrayList<>();
        commande.getCommandeBoxCollection().stream().forEach((commandeBox)->{
            this.boxAchetes.add(commandeBox.getIdBoxAchete());
        });
        return "commande";
    }
    
}
