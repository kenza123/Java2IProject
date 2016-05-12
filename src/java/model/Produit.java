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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aBennouna
 */
@Entity
@Table(name = "PRODUIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produit.findAll", query = "SELECT p FROM Produit p"),
    @NamedQuery(name = "Produit.findById", query = "SELECT p FROM Produit p WHERE p.id = :id"),
    @NamedQuery(name = "Produit.findByDateArriveeBox", query = "SELECT p FROM Produit p WHERE p.dateArriveeBox = :dateArriveeBox")})
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DATE_ARRIVEE_BOX")
    @Temporal(TemporalType.DATE)
    private Date dateArriveeBox;
    @JoinColumn(name = "ID_BOX", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private BoxAchete idBox;
    @JoinColumn(name = "ID_PRODUIT_COMMANDE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ProduitCommande idProduitCommande;
    @OneToMany(mappedBy = "idProduit")
    private Collection<LigneProduction> ligneProductionCollection;
    @OneToMany(mappedBy = "idProduit")
    private Collection<Pile> pileCollection;

    public Produit() {
    }

    public Produit(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateArriveeBox() {
        return dateArriveeBox;
    }

    public void setDateArriveeBox(Date dateArriveeBox) {
        this.dateArriveeBox = dateArriveeBox;
    }

    public BoxAchete getIdBox() {
        return idBox;
    }

    public void setIdBox(BoxAchete idBox) {
        this.idBox = idBox;
    }

    public ProduitCommande getIdProduitCommande() {
        return idProduitCommande;
    }

    public void setIdProduitCommande(ProduitCommande idProduitCommande) {
        this.idProduitCommande = idProduitCommande;
    }

    @XmlTransient
    public Collection<LigneProduction> getLigneProductionCollection() {
        return ligneProductionCollection;
    }

    public void setLigneProductionCollection(Collection<LigneProduction> ligneProductionCollection) {
        this.ligneProductionCollection = ligneProductionCollection;
    }

    @XmlTransient
    public Collection<Pile> getPileCollection() {
        return pileCollection;
    }

    public void setPileCollection(Collection<Pile> pileCollection) {
        this.pileCollection = pileCollection;
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
        if (!(object instanceof Produit)) {
            return false;
        }
        Produit other = (Produit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Produit[ id=" + id + " ]";
    }
    
}
