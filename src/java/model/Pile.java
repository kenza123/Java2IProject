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
@Table(name = "PILE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pile.deleteAll", query = "DELETE FROM Pile p"),
    @NamedQuery(name = "Pile.findAll", query = "SELECT p FROM Pile p"),
    @NamedQuery(name = "Pile.findById", query = "SELECT p FROM Pile p WHERE p.id = :id"),
    @NamedQuery(name = "Pile.findByLongueurPile", query = "SELECT p FROM Pile p WHERE p.longueurPile = :longueurPile"),
    @NamedQuery(name = "Pile.findByLargeurPile", query = "SELECT p FROM Pile p WHERE p.largeurPile = :largeurPile")})
public class Pile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "LONGUEUR_PILE")
    private Integer longueurPile;
    @Column(name = "LARGEUR_PILE")
    private Integer largeurPile;
    @JoinColumn(name = "ID_BOX_ACHETE", referencedColumnName = "ID")
    @ManyToOne
    private BoxAchete idBoxAchete;
    @OneToMany(mappedBy = "idPile")
    private Collection<Produit> produitCollection;

    public Pile() {
        produitCollection = new ArrayList();
    }

    public Pile(Integer id) {
        this.id = id;
        produitCollection = new ArrayList();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLongueurPile() {
        return longueurPile;
    }

    public void setLongueurPile(Integer longueurPile) {
        this.longueurPile = longueurPile;
    }

    public Integer getLargeurPile() {
        return largeurPile;
    }

    public void setLargeurPile(Integer largeurPile) {
        this.largeurPile = largeurPile;
    }

    public BoxAchete getIdBoxAchete() {
        return idBoxAchete;
    }

    public void setIdBoxAchete(BoxAchete idBoxAchete) {
        this.idBoxAchete = idBoxAchete;
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
        if (!(object instanceof Pile)) {
            return false;
        }
        Pile other = (Pile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pile{" + "id=" + id + ", longueurPile=" + longueurPile + ", largeurPile=" + largeurPile + '}';
    }
    
    @XmlTransient
    public Collection<Produit> getProduitCollection() {
        return produitCollection;
    }

    public void setProduitCollection(Collection<Produit> produitCollection) {
        this.produitCollection = produitCollection;
    }
    
}
