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
    
    protected JpaDaoFactory() {
        
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
    public PileDao getPileDao() {
        return JpaDaoPile.getInstance();
    }

    @Override
    public ProduitDao getProduitDao() {
        return JpaDaoProduit.getInstance();
    }

    @Override
    public ProduitCommandeDao getProduitCommandeDao() {
        return JpaDaoProduitCommande.getInstance();
    }

    @Override
    public TypeBoxDao getTypeBoxDao() {
        return JpaDaoTypeBox.getInstance();
    }

    @Override
    public TypeProduitDao getTypeProduitDao() {
        return JpaDaoTypeProduit.getInstance();
    }
    
    
    
    
}
