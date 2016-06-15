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
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolutionGenerator {
    
    private final JpaDaoFactory jpaDaoFactory;
    private final TypeBoxDao typeBoxDao;
    private final CommandeDao commandeDao;
    private final ProduitDao produitDao;
    private final BoxAcheteDao boxAcheteDao;
    private double eval;
    private String fileName;

    public SolutionGenerator() {
        jpaDaoFactory = (JpaDaoFactory) DaoFactory.getDaoFactory(DaoFactory.PersistenceType.JPA);
        typeBoxDao = jpaDaoFactory.getTypeBoxDao();
        commandeDao = jpaDaoFactory.getCommandeDao();
        produitDao = jpaDaoFactory.getProduitDao();
        boxAcheteDao = jpaDaoFactory.getBoxAcheteDao();
        eval = 0;
        calculEval();
    }

    public double getEval() {
        return eval;
    }

    public void setEval(double eval) {
        this.eval = eval;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName.replace("txt", "sol");
    }
    
    /**
     * Fonction permettant a calculer eval
     */
    public void calculEval() {
        eval = 0;
        typeBoxDao.findAll().stream().forEach((typeBox) -> {
            eval = eval 
                    + typeBox.getPrixbox() 
                    * boxAcheteDao.countBoxes(typeBox);
            });
        commandeDao.findAll().stream().forEach((commande) -> {
          eval = eval +
                  commande.getPenalite() *
                  Math.abs(commande.getDenvoireel() - commande.getDenvoiprevue());
        });
    }
    
    /**
     * Fonction permettant de generer le fichier de solution
     * @return Fichier de solution
     */
    public File generateSolutionFile() {
        List<String> lines = new ArrayList();
        
        lines.add(Double.toString(eval));
        
        lines.add("");
        
        typeBoxDao.findAll().stream().forEach((typeBox) -> {
            String line = typeBox.getId()
                    + "\t"
                    + boxAcheteDao.countBoxes(typeBox);
            lines.add(line);
            });
        
        lines.add("");
        
        commandeDao.findAllOrderByDenvoireelle().stream().forEach((commande) -> {
            String line = commande.getId()
                    + "\t"
                    + commande.getDenvoireel();
            lines.add(line);
            });
        
        lines.add("");
        
        produitDao.findAll().stream().forEach((produit) -> {
            String line = produit.getIdProduitCommande().getIdCommande().getId()
                    + "\t"
                    + produit.getIdProduitCommande().getIdTypeProduit().getId()
                    + "\t"
                    + produit.getNblignes().getNblignes()
                    + "\t"
                    + produit.getDateDebutProd()
                    + "\t"
                    + produit.getIdPile().getIdBoxAchete().getIdTypeBox().getId()
                    + "\t"
                    + produit.getIdPile().getIdBoxAchete().getNumBox();
                    
            lines.add(line);
            });
        
        
        Path file = Paths.get(fileName);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            Logger.getLogger(SolutionGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return file.toFile();
    }
    
}