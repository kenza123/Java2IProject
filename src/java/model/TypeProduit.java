/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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

/**
 *
 * @author aBennouna
 */
@Entity
@Table(name = "TYPE_PRODUIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypeProduit.deleteAll", query = "DELETE FROM TypeProduit t"),
    @NamedQuery(name = "TypeProduit.findAll", query = "SELECT t FROM TypeProduit t"),
    @NamedQuery(name = "TypeProduit.findById", query = "SELECT t FROM TypeProduit t WHERE t.id = :id"),
    @NamedQuery(name = "TypeProduit.findByTSetup", query = "SELECT t FROM TypeProduit t WHERE t.tSetup = :tSetup"),
    @NamedQuery(name = "TypeProduit.findByTProduction", query = "SELECT t FROM TypeProduit t WHERE t.tProduction = :tProduction"),
    @NamedQuery(name = "TypeProduit.findByHauteur", query = "SELECT t FROM TypeProduit t WHERE t.hauteur = :hauteur"),
    @NamedQuery(name = "TypeProduit.findByLongueur", query = "SELECT t FROM TypeProduit t WHERE t.longueur = :longueur"),
    @NamedQuery(name = "TypeProduit.findByNbempilemax", query = "SELECT t FROM TypeProduit t WHERE t.nbempilemax = :nbempilemax")})
public class TypeProduit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private String id;
    @Column(name = "T_SETUP")
    private Integer tSetup;
    @Column(name = "T_PRODUCTION")
    private Integer tProduction;
    @Column(name = "HAUTEUR")
    private Integer hauteur;
    @Column(name = "LONGUEUR")
    private Integer longueur;
    @Column(name = "NBEMPILEMAX")
    private Integer nbempilemax;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTypeProduit")
    private Collection<ProduitCommande> produitCommandeCollection;
    @Size(max = 45)
    @Column(name = "COLOR")
    private String color;

    public TypeProduit() {
        produitCommandeCollection = new ArrayList();
    }

    public TypeProduit(String id) {
        this.id = id;
        produitCommandeCollection = new ArrayList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTSetup() {
        return tSetup;
    }

    public void setTSetup(Integer tSetup) {
        this.tSetup = tSetup;
    }

    public Integer getTProduction() {
        return tProduction;
    }

    public void setTProduction(Integer tProduction) {
        this.tProduction = tProduction;
    }

    public Integer getHauteur() {
        return hauteur;
    }

    public void setHauteur(Integer hauteur) {
        this.hauteur = hauteur;
    }

    public Integer getLongueur() {
        return longueur;
    }

    public void setLongueur(Integer longueur) {
        this.longueur = longueur;
    }

    public Integer getNbempilemax() {
        return nbempilemax;
    }

    public void setNbempilemax(Integer nbempilemax) {
        this.nbempilemax = nbempilemax;
    }

    @XmlTransient
    public Collection<ProduitCommande> getProduitCommandeCollection() {
        return produitCommandeCollection;
    }

    public void setProduitCommandeCollection(Collection<ProduitCommande> produitCommandeCollection) {
        this.produitCommandeCollection = produitCommandeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeProduit)) {
            return false;
        }
        TypeProduit other = (TypeProduit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TypeProduit{" + "id=" + id + ", tSetup=" + tSetup + 
                ", tProduction=" + tProduction + ", hauteur=" + hauteur + 
                ", longueur=" + longueur + ", nbempilemax=" + nbempilemax + '}';
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    
}
