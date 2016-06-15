/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.Objects;
import model.Pile;

/**
 *
 * @author aBennouna
 */
public class PileDto implements Comparable, Serializable {
    private int xValue;
    private Pile pile;
    private int debutProd;

    public PileDto(int xValue, Pile pile) {
        this.xValue = xValue;
        this.pile = pile;
    }
    
    public PileDto(int xValue, Pile pile, int debutProd) {
        this.xValue = xValue;
        this.pile = pile;
        this.debutProd = debutProd;
    }

    public int getxValue() {
        return xValue;
    }

    public void setxValue(int xValue) {
        this.xValue = xValue;
    }

    public Pile getPile() {
        return pile;
    }

    public void setPile(Pile pile) {
        this.pile = pile;
    }

    public int getDebutProd() {
        return debutProd;
    }

    public void setDebutProd(int debutProd) {
        this.debutProd = debutProd;
    }
    
    @Override
    public int compareTo(Object o) {
        int compare=((PileDto)o).getDebutProd();
        return this.debutProd-compare;
    }

    @Override
    public String toString() {
        return "PileDto{" + "xValue=" + xValue + ", pile=" + pile + ", debutProd=" + debutProd + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.pile);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PileDto other = (PileDto) obj;
        if (!Objects.equals(this.pile, other.pile)) {
            return false;
        }
        return true;
    }
    
    
    
}
