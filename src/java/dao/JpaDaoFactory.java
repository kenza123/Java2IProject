/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author aBennouna
 */
public class JpaDaoFactory extends DaoFactory {
    
    public JpaDaoFactory() {
        
    }
    
    @Override
    public JpaDaoBoxAchete getBoxAcheteDao() {
        return JpaDaoBoxAchete.getInstance();
    }
    
    @Override
    public JpaDaoCommande getCommandeDao() {
        return JpaDaoCommande.getInstance();
    }
    
    @Override
    public JpaDaoLigneProduction getLigneProductionDao() {
        return JpaDaoLigneProduction.getInstance();
    }

    @Override
    public JpaDaoPile getPileDao() {
        return JpaDaoPile.getInstance();
    }

    @Override
    public JpaDaoProduit getProduitDao() {
        return JpaDaoProduit.getInstance();
    }

    @Override
    public JpaDaoProduitCommande getProduitCommandeDao() {
        return JpaDaoProduitCommande.getInstance();
    }

    @Override
    public JpaDaoTypeBox getTypeBoxDao() {
        return JpaDaoTypeBox.getInstance();
    }

    @Override
    public JpaDaoTypeProduit getTypeProduitDao() {
        return JpaDaoTypeProduit.getInstance();
    }
    
    @Override
    public void resetInstances(){
        JpaDaoBoxAchete.setInstance(null);
        JpaDaoCommande.setInstance(null);
        JpaDaoLigneProduction.setInstance(null);
        JpaDaoPile.setInstance(null);
        JpaDaoProduit.setInstance(null);
        JpaDaoProduitCommande.setInstance(null);
        JpaDaoTypeBox.setInstance(null);
        JpaDaoTypeProduit.setInstance(null);
    }
    
    
    
}
