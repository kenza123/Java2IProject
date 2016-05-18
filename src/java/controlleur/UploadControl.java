/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;


import dao.CommandeDao;
import dao.DaoFactory;
import dao.JpaDaoFactory;
import dao.LigneProductionDao;
import dao.ProduitCommandeDao;
import dao.TypeBoxDao;
import dao.TypeProduitDao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Part;
import model.Commande;
import model.LigneProduction;
import model.ProduitCommande;
import model.TypeProduit;
import model.TypeBox;

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
    int nbTypesProduit;
    int nbCommandes;
    int nbTypesBox;
    List<String> TypesProduitValues = new ArrayList();
    int nbLignesProd;

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
        resetDB();
        System.out.println(file.getSubmittedFileName());
        
        try {
            
            BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line = null;
            
            while((line = in.readLine()) != null) {
                line = eraseSpaces(line);
                String [] lineTab = line.split(" ");
                String firstWord = lineTab[0];
                if (isNumber(firstWord)) {
                    insertToLigneProduction(line);
                } else if(firstWord.startsWith("P")) {
                    insertToTypeProduit(line);
                } else if(firstWord.startsWith("C")) {
                    insertToCommande(line);
                } else if(firstWord.startsWith("B")) {
                    insertToTypeBox(line);
                }
            }
        } catch (IOException e) {
            // Error handling
        }
        System.out.println("OKOKOKOKOKOKOKOKOKOK");
    }  
    
    public void resetDB() {
        JpaDaoFactory jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        TypeProduitDao typeProduitDao = jpaDaoFactory.getTypeProduitDao();
        TypeBoxDao typeBoxDao = jpaDaoFactory.getTypeBoxDao();
        LigneProductionDao ligneProductionDao = jpaDaoFactory.getLigneProductionDao();
        CommandeDao commandeDao = jpaDaoFactory.getCommandeDao();
        ProduitCommandeDao produitCommandeDao = jpaDaoFactory.getProduitCommandeDao();
        
        produitCommandeDao.deleteAll();
        typeProduitDao.deleteAll();
        typeBoxDao.deleteAll();
        ligneProductionDao.deleteAll();
        commandeDao.deleteAll();
    }
    
    public void insertToTypeProduit(String line) {
        
        JpaDaoFactory jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        TypeProduitDao typeProduitDao = jpaDaoFactory.getTypeProduitDao();
        
        
        String [] typeProduitTab = line.split(" ");
        
        TypeProduit typeProduit = new TypeProduit();
        
        typeProduit.setId(typeProduitTab[0]);
        typeProduit.setTSetup(Integer.decode(typeProduitTab[1]));
        typeProduit.setTProduction(Integer.decode(typeProduitTab[2]));
        typeProduit.setHauteur(Integer.decode(typeProduitTab[3]));
        typeProduit.setLongueur(Integer.decode(typeProduitTab[4]));
        typeProduit.setNbempilemax(Integer.decode(typeProduitTab[5]));
        
        typeProduitDao.create(typeProduit);
        
        TypesProduitValues.add(typeProduitTab[0]);
        
    }
     
    public void insertToTypeBox(String line) {
        
        JpaDaoFactory jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        TypeBoxDao typeBoxDao = jpaDaoFactory.getTypeBoxDao();
        
        String [] typeBoxTab = line.split(" ");
        
        TypeBox typeBox = new TypeBox();
        
        typeBox.setId(typeBoxTab[0]);
        typeBox.setLbox(Integer.decode(typeBoxTab[1]));
        typeBox.setHbox(Integer.decode(typeBoxTab[2]));
        typeBox.setPrixbox(Double.valueOf(typeBoxTab[3]));
        
        typeBoxDao.create(typeBox);
        
    }
    
    public void insertToLigneProduction(String line) {
        
        JpaDaoFactory jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        LigneProductionDao ligneProductionDao = jpaDaoFactory.getLigneProductionDao();
        
        nbTypesProduit = Integer.decode(line.split(" ")[0]);
        nbCommandes = Integer.decode(line.split(" ")[1]);
        nbTypesBox = Integer.decode(line.split(" ")[2]);
        nbLignesProd = Integer.decode(line.split(" ")[3]);
     
        for(int i = 1 ; i <= nbLignesProd ; i++) {
            LigneProduction ligneProduction = new LigneProduction();
            ligneProduction.setNblignes(i);
            ligneProductionDao.create(ligneProduction);
        }
        
    }
    
    public void insertToCommande(String line) {
             
        JpaDaoFactory jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        CommandeDao commandeDao = jpaDaoFactory.getCommandeDao();
        ProduitCommandeDao produitCommandeDao = jpaDaoFactory.getProduitCommandeDao();
        TypeProduitDao typeProduitDao = jpaDaoFactory.getTypeProduitDao();
        
        String [] commandeTab = line.split(" ");
        
        Commande commande = new Commande();
        
        commande.setId(commandeTab[0]);
        commande.setStockmin(Integer.decode(commandeTab[1]));
        commande.setDenvoiprevue(Integer.decode(commandeTab[2]));
        commande.setPenalite(Double.valueOf(commandeTab[3]));
        
        commandeDao.create(commande);
        
        for(int i = 4 ; i< commandeTab.length; i++) {
            
            ProduitCommande produitCommande = new ProduitCommande();
            produitCommande.setIdCommande(commande);
            TypeProduit typeProduit = typeProduitDao.findById(TypesProduitValues.get(i - 4));
            produitCommande.setIdTypeProduit(typeProduit);
            produitCommande.setNbUnites(Integer.decode(commandeTab[i]));
            
            produitCommandeDao.create(produitCommande);
        }
        
    }
    
    public Boolean isNumber(String val) {
        try  {  
            double d = Double.parseDouble(val);  
        }  
        catch(NumberFormatException nfe) {  
            return false;  
        }  
        return true;  
    }
    
    public String eraseSpaces(String line) {
        Boolean verif = true;
        while(verif) {
            if(line.contains("  ")) {
                line = line.replace("  ", " ");
                verif = true;
            } else {
                verif = false;
            }
        }
        return line;
    }
     
}
