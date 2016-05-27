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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aBennouna
 */
@Entity
@Table(name = "BOX_ACHETE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BoxAchete.deleteAll", query = "DELETE FROM BoxAchete b"),
    @NamedQuery(name = "BoxAchete.countBoxesById", query = "SELECT COUNT(b) FROM BoxAchete b WHERE b.idTypeBox= :idTypeBox"),
    @NamedQuery(name = "BoxAchete.findAll", query = "SELECT b FROM BoxAchete b"),
    @NamedQuery(name = "BoxAchete.findById", query = "SELECT b FROM BoxAchete b WHERE b.id = :id"),
    @NamedQuery(name = "BoxAchete.findByNumBox", query = "SELECT b FROM BoxAchete b WHERE b.numBox = :numBox")})
public class BoxAchete implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUM_BOX")
    private int numBox;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBox")
    private Collection<Produit> produitCollection;
    @JoinColumn(name = "ID_TYPE_BOX", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TypeBox idTypeBox;
    @OneToMany(mappedBy = "idBoxAchete")
    private Collection<Pile> pileCollection;

    public BoxAchete() {
    }

    public BoxAchete(Integer id) {
        this.id = id;
    }

    public BoxAchete(Integer id, int numBox) {
        this.id = id;
        this.numBox = numBox;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumBox() {
        return numBox;
    }

    public void setNumBox(int numBox) {
        this.numBox = numBox;
    }

    @XmlTransient
    public Collection<Produit> getProduitCollection() {
        return produitCollection;
    }

    public void setProduitCollection(Collection<Produit> produitCollection) {
        this.produitCollection = produitCollection;
    }

    public TypeBox getIdTypeBox() {
        return idTypeBox;
    }

    public void setIdTypeBox(TypeBox idTypeBox) {
        this.idTypeBox = idTypeBox;
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
        if (!(object instanceof BoxAchete)) {
            return false;
        }
        BoxAchete other = (BoxAchete) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.BoxAchete[ id=" + id + " ]";
    }
    
}
