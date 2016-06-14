/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlleur;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import metier.InstanceUploader;
import metier.SolutionGenerator;
import metier.OptimizedSolution;
import metier.OptimizedSolution2;
import metier.OptimizedSolution3;

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
    private SolutionGenerator solutionGenerator;

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public SolutionGenerator getSolutionGenerator() {
        return solutionGenerator;
    }

    public void setSolutionGenerator(SolutionGenerator solutionGenerator) {
        this.solutionGenerator = solutionGenerator;
    }
    
    
    
    public UploadControl() {

    }
    
    public String uploadFile() {
        if(file.getSubmittedFileName().contains(".txt")) {
            InstanceUploader instanceUploader = new InstanceUploader();
            instanceUploader.upload(file);
            
            // Using only one line
            //TrivialSolution trivialSolution = new TrivialSolution();
            //trivialSolution.execute();
            
            // Using all production lines
            //OptimizedSolution optimizedSolution = new OptimizedSolution();
            //optimizedSolution.execute();
            
            // Reusing boxes
            //OptimizedSolution2 optimizedSolution2 = new OptimizedSolution2();
            //optimizedSolution2.execute();

            // Reusing boxes and empiler
            OptimizedSolution3 optimizedSolution3 = new OptimizedSolution3();
            optimizedSolution3.execute();
            
            this.solutionGenerator = new SolutionGenerator();
            this.solutionGenerator.setFileName(file.getSubmittedFileName());
            this.solutionGenerator.generateSolutionFile();
            return "stats";
        } else {
            error("Le fichier n'est pas un fichier txt");
            return "";
        }
    }
    
    public void downloadFile () {
        System.out.println("ok");
        if (this.solutionGenerator == null) {
            this.solutionGenerator = new SolutionGenerator();
            this.solutionGenerator.setFileName("Solution.sol");
            this.solutionGenerator.generateSolutionFile();
        }
        File file = this.solutionGenerator.generateSolutionFile();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();  

        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());  
        response.setContentLength((int) file.length());  
        ServletOutputStream out = null;  
        try {  
            FileInputStream input = new FileInputStream(file);  
            byte[] buffer = new byte[1024];  
            out = response.getOutputStream();  
            int i = 0;  
            while ((i = input.read(buffer)) != -1) {  
                out.write(buffer);  
                out.flush();  
            }  
            FacesContext.getCurrentInstance().responseComplete();  
            System.out.println("ok2");
        } catch (IOException err) {  
            err.printStackTrace();  
            System.out.println("nok" + err.toString());
        } finally {  
            try {  
                if (out != null) {  
                    out.close();  
                    System.out.println("ok3");
                }  
            } catch (IOException err) {  
                err.printStackTrace();
                System.out.println("nok2" + err.toString());  
            }  
        }  
    }
    
    public void error(String message) {
        FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
     
}
