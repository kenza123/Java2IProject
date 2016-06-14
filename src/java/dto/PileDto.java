/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import model.Pile;

/**
 *
 * @author aBennouna
 */
public class PileDto {
    private int xValue;
    private Pile pile;

    public PileDto(int xValue, Pile pile) {
        this.xValue = xValue;
        this.pile = pile;
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
}
