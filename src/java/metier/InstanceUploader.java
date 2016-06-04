/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import dao.BoxAcheteDao;
import dao.CommandeBoxDao;
import dao.CommandeDao;
import dao.DaoFactory;
import dao.JpaDaoFactory;
import dao.LigneProductionDao;
import dao.PileDao;
import dao.ProduitCommandeDao;
import dao.ProduitDao;
import dao.TypeBoxDao;
import dao.TypeProduitDao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Part;
import model.Commande;
import model.LigneProduction;
import model.ProduitCommande;
import model.TypeBox;
import model.TypeProduit;

/**
 *
 * @author ghitakhamaily
 */
public class InstanceUploader {
    private List<String> typesProduitValues;
    private int nbLignesProd;
    private String fileName;

    private final JpaDaoFactory jpaDaoFactory;
    private final TypeProduitDao typeProduitDao;
    private final TypeBoxDao typeBoxDao;
    private final LigneProductionDao ligneProductionDao;
    private final CommandeDao commandeDao;
    private final ProduitCommandeDao produitCommandeDao;
    private final ProduitDao produitDao;
    private final BoxAcheteDao boxAcheteDao;
    private final PileDao pileDao;
    private final CommandeBoxDao commandeBoxDao;
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InstanceUploader() {
        jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        typeProduitDao = jpaDaoFactory.getTypeProduitDao();
        typeBoxDao = jpaDaoFactory.getTypeBoxDao();
        ligneProductionDao = jpaDaoFactory.getLigneProductionDao();
        commandeDao = jpaDaoFactory.getCommandeDao();
        produitCommandeDao = jpaDaoFactory.getProduitCommandeDao();
        produitDao = jpaDaoFactory.getProduitDao();
        boxAcheteDao = jpaDaoFactory.getBoxAcheteDao();
        pileDao = jpaDaoFactory.getPileDao();
        commandeBoxDao = jpaDaoFactory.getCommandeBoxDao();
        typesProduitValues = new ArrayList();
    }
        
    
    public void upload(Part file) {
       try {
            BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line = null;
            resetDB();
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
        }
    }
    
    public void resetDB() {
        produitDao.deleteAll();
        ligneProductionDao.deleteAll();
        produitCommandeDao.deleteAll();
        typeProduitDao.deleteAll();
        pileDao.deleteAll();
        commandeBoxDao.deleteAll();
        boxAcheteDao.deleteAll();
        commandeDao.deleteAll();
        typeBoxDao.deleteAll();
    }
    
    public void insertToTypeProduit(String line) {
        String [] typeProduitTab = line.split(" ");
        
        TypeProduit typeProduit = new TypeProduit();
        
        typeProduit.setId(typeProduitTab[0]);
        typeProduit.setTSetup(Integer.decode(typeProduitTab[1]));
        typeProduit.setTProduction(Integer.decode(typeProduitTab[2]));
        typeProduit.setHauteur(Integer.decode(typeProduitTab[3]));
        typeProduit.setLongueur(Integer.decode(typeProduitTab[4]));
        typeProduit.setNbempilemax(Integer.decode(typeProduitTab[5]));
        typesProduitValues.add(typeProduitTab[0]);
        typeProduit.setColor(this.getColor(typesProduitValues.size()));
        typeProduitDao.create(typeProduit);
        
    }
     
    public void insertToTypeBox(String line) {
        String [] typeBoxTab = line.split(" ");
        TypeBox typeBox = new TypeBox();
        typeBox.setId(typeBoxTab[0]);
        typeBox.setHbox(Integer.decode(typeBoxTab[1]));
        typeBox.setLbox(Integer.decode(typeBoxTab[2]));
        typeBox.setPrixbox(Double.valueOf(typeBoxTab[3]));
        
        typeBoxDao.create(typeBox);
        
    }
    
    public void insertToLigneProduction(String line) {
        nbLignesProd = Integer.decode(line.split(" ")[3]);
     
        for(int i = 1 ; i <= nbLignesProd ; i++) {
            LigneProduction ligneProduction = new LigneProduction();
            ligneProduction.setNblignes(i);
            ligneProductionDao.create(ligneProduction);
        }
        
    }
    
    public void insertToCommande(String line) {
        String [] commandeTab = line.split(" ");
        Commande commande = new Commande();
        commande.setId(commandeTab[0]);
        commande.setStockmin(Integer.decode(commandeTab[1]));
        commande.setDenvoiprevue(Integer.decode(commandeTab[2]));
        commande.setPenalite(Double.valueOf(commandeTab[3]));
        commandeDao.create(commande);
        
        for(int i = 4 ; i < commandeTab.length; i++) {
            if(Integer.decode(commandeTab[i]) != 0){
                ProduitCommande produitCommande = new ProduitCommande();
                produitCommande.setIdCommande(commande);
                TypeProduit typeProduit = typeProduitDao.findById(typesProduitValues.get(i - 4));
                produitCommande.setIdTypeProduit(typeProduit);
                produitCommande.setNbUnites(Integer.decode(commandeTab[i]));
                produitCommandeDao.create(produitCommande);
                
                commande.getProduitCommandeCollection().add(produitCommande);
                commandeDao.update(commande);
                
                typeProduit.getProduitCommandeCollection().add(produitCommande);
                typeProduitDao.update(typeProduit);
            }
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
    
    public String getColor(int position) {
        String[] colors = 
        {   
            "#E57373" , "#F06292" , "#BA68C8" , "#9575CD" , "#7986CB" , 
            "#64B5F6" , "#4FC3F7" , "#4DD0E1" , "#4DB6AC" , "#81C784" , 
            "#AED581" , "#DCE775" , "#FFF176" , "#FFD54F" , "#FFB74D" , 
            "#FF8A65" , "#A1887F" , "#E0E0E0" , "#90A4AE" , "#F44336" , 
            "#E91E63" , "#9C27B0" , "#673AB7" , "#3F51B5" , "#2196F3" , 
            "#03A9F4" , "#00BCD4" , "#009688" , "#4CAF50" , "#8BC34A" , 
            "#CDDC39" , "#FFEB3B" , "#FFC107" , "#FF9800" , "#FF5722" , 
            "#795548" , "#9E9E9E" , "#607D8B" , "#D32F2F" , "#C2185B" , 
            "#7B1FA2" , "#512DA8" , "#303F9F" , "#1976D2" , "#0288D1" , 
            "#0097A7" , "#00796B" , "#388E3C" , "#689F38" , "#AFB42B" , 
            "#FBC02D" , "#FFA000" , "#F57C00" , "#E64A19" , "#5D4037" , 
            "#616161" , "#455A64"
        };
        boolean inBounds = (position >= 0) && (position < colors.length);
        return (inBounds) ? colors[position] : colors[0];
    }
}
