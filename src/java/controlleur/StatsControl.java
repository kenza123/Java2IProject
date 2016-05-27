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
import javax.enterprise.context.Dependent;
import model.Commande;
import model.TypeBox;
import dto.Box;
import java.util.ArrayList;

/**
 *
 * @author kenzakhamaily
 */
@Named(value = "statsControl")
@Dependent
public class StatsControl {
    private Collection<Commande> commandes;
    private Collection<Box> boxes = new ArrayList<Box>();

    /**
     * Creates a new instance of statsControl
     */
    public StatsControl() {
    }

    public Collection<Commande> getCommandes() {
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoBoxAchete jdba = jdf.getBoxAcheteDao();
        JpaDaoCommande jdc = jdf.getCommandeDao();
        commandes = jdc.findAll();
        return commandes;
    }

    public Collection<Box> getBoxes() {
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoTypeBox jdtb = jdf.getTypeBoxDao();
        JpaDaoBoxAchete jdba = jdf.getBoxAcheteDao();
        Collection<TypeBox> typeBoxs = jdtb.findAll();
        for (TypeBox box : typeBoxs) {
            Box b = new Box();
            b.setTypeBox(box);
            b.setAchat(jdba.countBoxesById(box));
            b.setUtilise(jdba.countBoxesById(box));
            b.calculerCout();
            boxes.add(b);
        }
        return boxes;
    }
    
    
}
