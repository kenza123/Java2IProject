/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import dao.DaoFactory;
import dao.JpaDaoBoxAchete;
import dao.JpaDaoCommande;
import dao.JpaDaoFactory;
import dao.JpaDaoTypeBox;
import java.util.Collection;
import java.util.List;
import javax.inject.Named;
import model.Commande;
import model.TypeBox;
import dto.TypeBoxDto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author kenzakhamaily
 */
@Named(value = "statsControl")
@SessionScoped
public class StatsControl implements Serializable {
    private Collection<Commande> commandes;
    private Collection<TypeBoxDto> boxes;
    private String tri;

    public String getTri() {
        return tri;
    }

    public void setTri(String tri) {
        this.tri = tri;
    }
    
    

    /**
     * Creates a new instance of statsControl
     */
    public StatsControl() {
        this.tri = "";
    }

    public Collection<Commande> getCommandes() {
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoCommande jdc = jdf.getCommandeDao();
        List<Commande> allCommandes = new ArrayList<>();
        allCommandes.addAll(jdc.findAll());
        switch (this.tri) {
            case "id":
                commandes = jdc.findAllOrderBYId();
                break;
            case "penalite":
                commandes = jdc.findAllOrderBYPenalite();
                break;
            case "stock":
                commandes = jdc.findAllOrderBYStock();
                break;
            case "envoiprevue":
                commandes = jdc.findAllOrderByDenvoiprevue();
                break;
            case "envoireelle":
                commandes = jdc.findAllOrderByDenvoireelle();
                break;
            case "ecart" :
                Collections.sort(allCommandes, (Commande c1, Commande c2) -> Double.compare(c2.getEcart(), c1.getEcart()));
                commandes = allCommandes;
                break;
            case "cout" :
                Collections.sort(allCommandes, (Commande c1, Commande c2) -> Double.compare(c2.getCout(), c1.getCout()));
                commandes = allCommandes;
                break;
            default:
                commandes = allCommandes;
                break;
        }
        return commandes;
    }

    public Collection<TypeBoxDto> getBoxes() {
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoTypeBox jdtb = jdf.getTypeBoxDao();
        JpaDaoBoxAchete jdba = jdf.getBoxAcheteDao();
        Collection<TypeBox> typeBoxs = jdtb.findAll();
        boxes = new ArrayList<>();
        for (TypeBox box : typeBoxs) {
            TypeBoxDto b = new TypeBoxDto();
            b.setTypeBox(box);
            b.setAchat(jdba.countBoxesById(box));
            b.setUtilise(jdba.countBoxesById(box));
            b.calculerCout();
            boxes.add(b);
        }
        return boxes;
    }
    
    
}
