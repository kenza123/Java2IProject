/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "TYPE_BOX")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypeBox.deleteAll", query = "DELETE FROM TypeBox t"),
    @NamedQuery(name = "TypeBox.findFirstByDimensions", 
            query = "SELECT t FROM TypeBox t WHERE t.hbox >= :hbox AND t.lbox >= :lbox ORDER BY t.hbox"),
    @NamedQuery(name = "TypeBox.findAll", query = "SELECT t FROM TypeBox t"),
    @NamedQuery(name = "TypeBox.findById", query = "SELECT t FROM TypeBox t WHERE t.id = :id"),
    @NamedQuery(name = "TypeBox.findByLbox", query = "SELECT t FROM TypeBox t WHERE t.lbox = :lbox"),
    @NamedQuery(name = "TypeBox.findByHbox", query = "SELECT t FROM TypeBox t WHERE t.hbox = :hbox"),
    @NamedQuery(name = "TypeBox.findByPrixbox", query = "SELECT t FROM TypeBox t WHERE t.prixbox = :prixbox")})
public class TypeBox implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LBOX")
    private int lbox;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HBOX")
    private int hbox;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRIXBOX")
    private double prixbox;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTypeBox")
    private Collection<BoxAchete> boxAcheteCollection;

    public TypeBox() {
        boxAcheteCollection = new ArrayList();
    }

    public TypeBox(String id) {
        this.id = id;
        boxAcheteCollection = new ArrayList();
    }

    public TypeBox(String id, int lbox, int hbox, double prixbox) {
        this.id = id;
        this.lbox = lbox;
        this.hbox = hbox;
        this.prixbox = prixbox;
        boxAcheteCollection = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLbox() {
        return lbox;
    }

    public void setLbox(int lbox) {
        this.lbox = lbox;
    }

    public int getHbox() {
        return hbox;
    }
    

    public void setHbox(int hbox) {
        this.hbox = hbox;
    }

    public double getPrixbox() {
        return prixbox;
    }

    public void setPrixbox(double prixbox) {
        this.prixbox = prixbox;
    }

    @XmlTransient
    public Collection<BoxAchete> getBoxAcheteCollection() {
        return boxAcheteCollection;
    }

    public void setBoxAcheteCollection(Collection<BoxAchete> boxAcheteCollection) {
        this.boxAcheteCollection = boxAcheteCollection;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final TypeBox other = (TypeBox) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "TypeBox{" + "id=" + id + ", lbox=" + lbox + ", hbox=" + hbox + ", prixbox=" + prixbox + '}';
    }
}
