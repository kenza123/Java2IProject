/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;


import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Scanner;
import javax.servlet.http.Part;

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
    
    Part file;
    String nom;

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
    
    public void uploadFile() {
        System.out.println("OKOKOKOKOKOKOKOKOKOK");
        System.out.println(file.getSubmittedFileName());
        try {
            System.out.println(new Scanner(file.getInputStream())
            .useDelimiter("\\A").next());
        } catch (IOException e) {
            // Error handling
        }
        System.out.println("OKOKOKOKOKOKOKOKOKOK");
    }
    
}
