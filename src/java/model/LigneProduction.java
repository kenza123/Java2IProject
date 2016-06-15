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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "LIGNE_PRODUCTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LigneProduction.deleteAll", query = "DELETE FROM LigneProduction l"),
    @NamedQuery(name = "LigneProduction.findAll", query = "SELECT l FROM LigneProduction l"),
    @NamedQuery(name = "LigneProduction.findById", query = "SELECT l FROM LigneProduction l WHERE l.id = :id"),
    @NamedQuery(name = "LigneProduction.findByNblignes", query = "SELECT l FROM LigneProduction l WHERE l.nblignes = :nblignes")})
public class LigneProduction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NBLIGNES")
    private int nblignes;
    @OneToMany(mappedBy = "nblignes")
    private Collection<Produit> produitCollection;

    public LigneProduction() {
        produitCollection = new ArrayList();
    }

    public LigneProduction(Integer id) {
        this.id = id;
        produitCollection = new ArrayList();
    }

    public LigneProduction(Integer id, int nblignes) {
        this.id = id;
        this.nblignes = nblignes;
        produitCollection = new ArrayList();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNblignes() {
        return nblignes;
    }

    public void setNblignes(int nblignes) {
        this.nblignes = nblignes;
    }

    @XmlTransient
    public Collection<Produit> getProduitCollection() {
        return produitCollection;
    }

    public void setProduitCollection(Collection<Produit> produitCollection) {
        this.produitCollection = produitCollection;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final LigneProduction other = (LigneProduction) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "LigneProduction{" + "id=" + id + ", nblignes=" + nblignes + '}';
    }

}
