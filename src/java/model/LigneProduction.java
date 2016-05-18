/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aBennouna
 */
@Entity
@Table(name = "LIGNE_PRODUCTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LigneProduction.deleteAll", query = "DELETE FROM LigneProduction l"),
    @NamedQuery(name = "LigneProduction.findAll", query = "SELECT l FROM LigneProduction l"),
    @NamedQuery(name = "LigneProduction.findById", query = "SELECT l FROM LigneProduction l WHERE l.id = :id"),
    @NamedQuery(name = "LigneProduction.findByNblignes", query = "SELECT l FROM LigneProduction l WHERE l.nblignes = :nblignes"),
    @NamedQuery(name = "LigneProduction.findByDateDispo", query = "SELECT l FROM LigneProduction l WHERE l.dateDispo = :dateDispo")})
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
    @Column(name = "DATE_DISPO")
    @Temporal(TemporalType.DATE)
    private Date dateDispo;
    @OneToMany(mappedBy = "numLigneProd")
    private Collection<Produit> produitCollection;
    @JoinColumn(name = "ID_PRODUIT", referencedColumnName = "ID")
    @ManyToOne
    private Produit idProduit;

    public LigneProduction() {
    }

    public LigneProduction(Integer id) {
        this.id = id;
    }

    public LigneProduction(Integer id, int nblignes) {
        this.id = id;
        this.nblignes = nblignes;
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

    public Date getDateDispo() {
        return dateDispo;
    }

    public void setDateDispo(Date dateDispo) {
        this.dateDispo = dateDispo;
    }

    @XmlTransient
    public Collection<Produit> getProduitCollection() {
        return produitCollection;
    }

    public void setProduitCollection(Collection<Produit> produitCollection) {
        this.produitCollection = produitCollection;
    }

    public Produit getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Produit idProduit) {
        this.idProduit = idProduit;
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
        if (!(object instanceof LigneProduction)) {
            return false;
        }
        LigneProduction other = (LigneProduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.LigneProduction[ id=" + id + " ]";
    }
    
}
