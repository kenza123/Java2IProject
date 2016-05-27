/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import dao.BoxAcheteDao;
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

    private final JpaDaoFactory jpaDaoFactory;
    private final TypeProduitDao typeProduitDao;
    private final TypeBoxDao typeBoxDao;
    private final LigneProductionDao ligneProductionDao;
    private final CommandeDao commandeDao;
    private final ProduitCommandeDao produitCommandeDao;
    private final ProduitDao produitDao;
    private final BoxAcheteDao boxAcheteDao;
    private final PileDao pileDao;

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
        pileDao.deleteAll();
        produitDao.deleteAll();
        ligneProductionDao.deleteAll();
        produitCommandeDao.deleteAll();
        typeProduitDao.deleteAll();
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
        
        typeProduitDao.create(typeProduit);
        typesProduitValues.add(typeProduitTab[0]);
        
    }
     
    public void insertToTypeBox(String line) {
        String [] typeBoxTab = line.split(" ");
        TypeBox typeBox = new TypeBox();
        typeBox.setId(typeBoxTab[0]);
        typeBox.setLbox(Integer.decode(typeBoxTab[1]));
        typeBox.setHbox(Integer.decode(typeBoxTab[2]));
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
}
