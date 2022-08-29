/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.List;
import model.Product;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ghmenezes
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product>{

    @PersistenceContext(unitName = "StefaniniFoodPU")
    private EntityManager em;

    public ProductFacade() {
        super(Product.class);
    }

    @Override
    protected EntityManager getEntityManager() {
       return em;
    }
    
    public List<Product> findProductByCompanyId(Object idCompany){
        return em.createNamedQuery("Product.findByCompanyId").setParameter("id", idCompany).getResultList();
    }
    
}
