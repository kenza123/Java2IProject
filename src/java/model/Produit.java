/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Objects;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "PRODUIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produit.deleteAll", query = "DELETE FROM Produit p"),
    @NamedQuery(name = "Produit.findLastProductInLine", query = "SELECT p FROM Produit p WHERE p.nblignes = :line ORDER BY p.dateDebutProd DESC"),
    @NamedQuery(name = "Produit.findByIdLineProduct", query = "SELECT p FROM Produit p WHERE p.nblignes = :line ORDER BY p.dateDebutProd ASC"),
    @NamedQuery(name = "Produit.findAll", query = "SELECT p FROM Produit p"),
    @NamedQuery(name = "Produit.findById", query = "SELECT p FROM Produit p WHERE p.id = :id"),
    @NamedQuery(name = "Produit.findByPile", query = "SELECT p FROM Produit p WHERE p.idPile = :pile"),
    @NamedQuery(name = "Produit.findByDateDebutProd", query = "SELECT p FROM Produit p WHERE p.dateDebutProd = :dateDebutProd")})
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DATE_DEBUT_PROD")
    private Integer dateDebutProd;
    @JoinColumn(name = "NBLIGNES", referencedColumnName = "ID")
    @ManyToOne
    private LigneProduction nblignes;
    @JoinColumn(name = "ID_PRODUIT_COMMANDE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ProduitCommande idProduitCommande;
    @JoinColumn(name = "ID_PILE", referencedColumnName = "ID")
    @ManyToOne
    private Pile idPile;

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

    public Integer getDateDebutProd() {
        return dateDebutProd;
    }

    public void setDateDebutProd(Integer dateDebutProd) {
        this.dateDebutProd = dateDebutProd;
    }

    public LigneProduction getNblignes() {
        return nblignes;
    }

    public void setNblignes(LigneProduction nblignes) {
        this.nblignes = nblignes;
    }

    public ProduitCommande getIdProduitCommande() {
        return idProduitCommande;
    }

    public void setIdProduitCommande(ProduitCommande idProduitCommande) {
        this.idProduitCommande = idProduitCommande;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Produit other = (Produit) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

   

    @Override
    public String toString() {
        return "Produit{" + "id=" + id + ", dateDebutProd=" + dateDebutProd + '}';
    }

    public Pile getIdPile() {
        return idPile;
    }

    public void setIdPile(Pile idPile) {
        this.idPile = idPile;
    }

    
}