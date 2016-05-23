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
    @NamedQuery(name = "Produit.deleteAll", query = "DELETE FROM Produit p"),
    @NamedQuery(name = "Produit.findAll", query = "SELECT p FROM Produit p"),
    @NamedQuery(name = "Produit.findById", query = "SELECT p FROM Produit p WHERE p.id = :id"),
    @NamedQuery(name = "Produit.findByDateDebutProd", query = "SELECT p FROM Produit p WHERE p.dateDebutProd = :dateDebutProd")})
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DATE_DEBUT_PROD")
    @Temporal(TemporalType.DATE)
    private Date dateDebutProd;
    @JoinColumn(name = "ID_BOX", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private BoxAchete idBox;
    @JoinColumn(name = "ID_LIGNE_PROD", referencedColumnName = "ID")
    @ManyToOne
    private LigneProduction idLigneProd;
    @JoinColumn(name = "ID_PRODUIT_COMMANDE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ProduitCommande idProduitCommande;
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

    public Date getDateDebutProd() {
        return dateDebutProd;
    }

    public void setDateDebutProd(Date dateDebutProd) {
        this.dateDebutProd = dateDebutProd;
    }

    public BoxAchete getIdBox() {
        return idBox;
    }

    public void setIdBox(BoxAchete idBox) {
        this.idBox = idBox;
    }

    public LigneProduction getIdLigneProd() {
        return idLigneProd;
    }

    public void setIdLigneProd(LigneProduction idLigneProd) {
        this.idLigneProd = idLigneProd;
    }

    public ProduitCommande getIdProduitCommande() {
        return idProduitCommande;
    }

    public void setIdProduitCommande(ProduitCommande idProduitCommande) {
        this.idProduitCommande = idProduitCommande;
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
