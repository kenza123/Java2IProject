/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
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
@Table(name = "COMMANDE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commande.deleteAll", query = "DELETE FROM Commande"),
    @NamedQuery(name = "Commande.findAll", query = "SELECT c FROM Commande c"),
    @NamedQuery(name = "Commande.findById", query = "SELECT c FROM Commande c WHERE c.id = :id"),
    @NamedQuery(name = "Commande.findByStockmin", query = "SELECT c FROM Commande c WHERE c.stockmin = :stockmin"),
    @NamedQuery(name = "Commande.findByDenvoiprevue", query = "SELECT c FROM Commande c WHERE c.denvoiprevue = :denvoiprevue"),
    @NamedQuery(name = "Commande.findByPenalite", query = "SELECT c FROM Commande c WHERE c.penalite = :penalite")})
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private String id;
    @Column(name = "STOCKMIN")
    private Integer stockmin;
    @Column(name = "DENVOIPREVUE")
    private Integer denvoiprevue;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PENALITE")
    private Double penalite;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCommande")
    private Collection<BoxAchete> boxAcheteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCommande")
    private Collection<ProduitCommande> produitCommandeCollection;

    public Commande() {
    }

    public Commande(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStockmin() {
        return stockmin;
    }

    public void setStockmin(Integer stockmin) {
        this.stockmin = stockmin;
    }

    public Integer getDenvoiprevue() {
        return denvoiprevue;
    }

    public void setDenvoiprevue(Integer denvoiprevue) {
        this.denvoiprevue = denvoiprevue;
    }

    public Double getPenalite() {
        return penalite;
    }

    public void setPenalite(Double penalite) {
        this.penalite = penalite;
    }

    @XmlTransient
    public Collection<BoxAchete> getBoxAcheteCollection() {
        return boxAcheteCollection;
    }

    public void setBoxAcheteCollection(Collection<BoxAchete> boxAcheteCollection) {
        this.boxAcheteCollection = boxAcheteCollection;
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
        if (!(object instanceof Commande)) {
            return false;
        }
        Commande other = (Commande) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Commande[ id=" + id + " ]";
    }
    
}
