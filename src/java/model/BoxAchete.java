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
    @NamedQuery(name = "BoxAchete.findAll", query = "SELECT b FROM BoxAchete b"),
    @NamedQuery(name = "BoxAchete.findAllOrdered", query = "SELECT b FROM BoxAchete b ORDER BY b.idTypeBox"),
    @NamedQuery(name = "BoxAchete.countBoxes", query = "SELECT COUNT(b) FROM BoxAchete b WHERE b.idTypeBox=:typeBox"),
    @NamedQuery(name = "BoxAchete.findBoxes", query = "SELECT b FROM BoxAchete b WHERE b.idTypeBox=:typeBox ORDER BY b.dLibre"),
    @NamedQuery(name = "BoxAchete.findById", query = "SELECT b FROM BoxAchete b WHERE b.id = :id"),
    @NamedQuery(name = "BoxAchete.findByNumBox", query = "SELECT b FROM BoxAchete b WHERE b.numBox = :numBox")})
public class BoxAchete implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "D_LIBRE")
    private int dLibre;

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
    @JoinColumn(name = "ID_TYPE_BOX", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TypeBox idTypeBox;
    @OneToMany(mappedBy = "idBoxAchete")
    private Collection<Pile> pileCollection;
    @Column(name = "LIBRE")
    private Integer libre;

    public BoxAchete() {
        pileCollection = new ArrayList();
        libre = 0;
    }

    public BoxAchete(Integer id) {
        this.id = id;
        pileCollection = new ArrayList();
        libre = 0;
    }

    public BoxAchete(Integer id, int numBox) {
        this.id = id;
        this.numBox = numBox;
        pileCollection = new ArrayList();
        libre = 0;
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
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final BoxAchete other = (BoxAchete) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "BoxAchete{" + "id=" + id + ", numBox=" + numBox + '}';
    }

    public Integer getLibre() {
        return libre;
    }

    public void setLibre(Integer libre) {
        this.libre = libre;
    }

    public int getDLibre() {
        return dLibre;
    }

    public void setDLibre(int dLibre) {
        this.dLibre = dLibre;
    }

}
