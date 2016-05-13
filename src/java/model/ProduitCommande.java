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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aBennouna
 */
@Entity
@Table(name = "PRODUIT_COMMANDE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProduitCommande.findAll", query = "SELECT p FROM ProduitCommande p"),
    @NamedQuery(name = "ProduitCommande.deleteAll", query = "DELETE FROM ProduitCommande"),
    @NamedQuery(name = "ProduitCommande.findById", query = "SELECT p FROM ProduitCommande p WHERE p.id = :id"),
    @NamedQuery(name = "ProduitCommande.findByNbUnites", query = "SELECT p FROM ProduitCommande p WHERE p.nbUnites = :nbUnites")})
public class ProduitCommande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NB_UNITES")
    private Integer nbUnites;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProduitCommande")
    private Collection<Produit> produitCollection;
    @JoinColumn(name = "ID_COMMANDE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Commande idCommande;
    @JoinColumn(name = "ID_TYPE_PRODUIT", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TypeProduit idTypeProduit;

    public ProduitCommande() {
    }

    public ProduitCommande(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNbUnites() {
        return nbUnites;
    }

    public void setNbUnites(Integer nbUnites) {
        this.nbUnites = nbUnites;
    }

    @XmlTransient
    public Collection<Produit> getProduitCollection() {
        return produitCollection;
    }

    public void setProduitCollection(Collection<Produit> produitCollection) {
        this.produitCollection = produitCollection;
    }

    public Commande getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(Commande idCommande) {
        this.idCommande = idCommande;
    }

    public TypeProduit getIdTypeProduit() {
        return idTypeProduit;
    }

    public void setIdTypeProduit(TypeProduit idTypeProduit) {
        this.idTypeProduit = idTypeProduit;
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
        if (!(object instanceof ProduitCommande)) {
            return false;
        }
        ProduitCommande other = (ProduitCommande) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ProduitCommande[ id=" + id + " ]";
    }
    
}
