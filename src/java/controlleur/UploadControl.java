/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.servlet.http.Part;
import metier.InstanceUploader;
import metier.TrivialSolution;

/**
 *
 * @author aBennouna
 */
@Named(value = "uploadControl")
@SessionScoped
public class UploadControl implements Serializable {

    /**
     * Creates a new instance of GangsterControl
     */
    
    private Part file;
    private String nom;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public UploadControl() {
        
    }
    
    public String uploadFile() {
        InstanceUploader instanceUploader = new InstanceUploader();
        instanceUploader.upload(file);
        TrivialSolution trivialSolution = new TrivialSolution();
        trivialSolution.execute();
        return "stats";
    }  
     
}
