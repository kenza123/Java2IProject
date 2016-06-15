/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import model.TypeBox;

public class TypeBoxDto implements Serializable {
    private int achat;
    private int utilise;
    private double cout;
    private TypeBox typeBox;

    public TypeBoxDto() {
    }

    public int getAchat() {
        return achat;
    }

    public void setAchat(int achat) {
        this.achat = achat;
    }

    public int getUtilise() {
        return utilise;
    }

    public void setUtilise(int utilise) {
        this.utilise = utilise;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public TypeBox getTypeBox() {
        return typeBox;
    }

    public void setTypeBox(TypeBox typeBox) {
        this.typeBox = typeBox;
    }

    
    
    
    public void calculerCout() {
        this.cout = this.achat * this.typeBox.getPrixbox();
    }
}
