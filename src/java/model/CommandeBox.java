/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
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

/**
 *
 * @author ghitakhamaily
 */
@Entity
@Table(name = "COMMANDE_BOX")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommandeBox.deleteAll", query = "DELETE FROM CommandeBox c"),
    @NamedQuery(name = "CommandeBox.findAll", query = "SELECT c FROM CommandeBox c"),
    @NamedQuery(name = "CommandeBox.findById", query = "SELECT c FROM CommandeBox c WHERE c.id = :id")})
public class CommandeBox implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "ID_BOX_ACHETE", referencedColumnName = "ID")
    @ManyToOne
    private BoxAchete idBoxAchete;
    @JoinColumn(name = "ID_COMMANDE", referencedColumnName = "ID")
    @ManyToOne
    private Commande idCommande;

    public CommandeBox() {
    }

    public CommandeBox(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BoxAchete getIdBoxAchete() {
        return idBoxAchete;
    }

    public void setIdBoxAchete(BoxAchete idBoxAchete) {
        this.idBoxAchete = idBoxAchete;
    }

    public Commande getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(Commande idCommande) {
        this.idCommande = idCommande;
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
        if (!(object instanceof CommandeBox)) {
            return false;
        }
        CommandeBox other = (CommandeBox) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CommandeBox[ id=" + id + " ]";
    }
    
}
