/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import model.Company;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ghmenezes
 */
@Stateless
public class CompanyFacade extends AbstractFacade<Company>{

    @PersistenceContext(unitName = "StefaniniFoodPU")
    private EntityManager em;

    public CompanyFacade() {
        super(Company.class);
    }

    @Override
    protected EntityManager getEntityManager() {
       return em;
    }
    
}
