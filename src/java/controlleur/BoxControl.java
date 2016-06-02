/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import dao.DaoFactory;
import dao.JpaDaoBoxAchete;
import dao.JpaDaoFactory;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import model.BoxAchete;
/**
 *
 * @author ohilmi
 */
@Named(value = "boxControl")
@SessionScoped
public class BoxControl implements Serializable{
    private BoxAchete box;

    public BoxAchete getBox() {
        return box;
    }

    public void setBox(BoxAchete box) {
        this.box = box;
    }

    public BoxControl() {
    }

    public BoxControl(BoxAchete box) {
        this.box = box;
    }
    
    public Collection<BoxAchete> getBoxList(){
        JpaDaoFactory jdf = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        JpaDaoBoxAchete jdba = jdf.getBoxAcheteDao();
        return jdba.findAll();
    }
    
}
