/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bean.CompanyFacade;
import bean.ProductFacade;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import model.Product;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

/**
 *
 * @author ghmenezes
 */
@Named("productController")
@SessionScoped
public class ProductController implements Serializable {

    @EJB
    private bean.ProductFacade ejbFacade;
    @EJB
    private bean.CompanyFacade ejb;
    private List<Product> products = null;
//    private List<Product> products = ;
    private Product selected;
    private Long companyId = 1L;

    public Long getCompanyId() {
        return companyId;
    }

//    public List<Product> showMenuById(Long companyId){
//        
//        System.out.println("Ai" + companyId);
//        return 
//    }
            
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
//        System.out.println(this.companyId + "setou");
    }
    
    public ProductController() {
    }

    public Product getProduct(Long id) {
        return getEjbFacade().find(id);
    }

    public ProductFacade getEjbFacade() {
        return ejbFacade;
    }

    public CompanyFacade getEjb() {
        return ejb;
    }

    public void setEjb(CompanyFacade ejb) {
        this.ejb = ejb;
    }
    
    

    public List<Product> getProducts() {
        if (products == null) {
            products = getEjbFacade().findAll();
        }
        return products;
    }
    
    public void show(){
//        System.out.println("oie");
        getEjbFacade().findProductByCompanyId(this.companyId);
    }
    
    public List<Product> getProductsByCompanyId() {
//        String jpql = "";
        return getEjbFacade().findProductByCompanyId(companyId);
//        return products;
    }

    public Product getSelected() {
        return selected;
    }

    public void setSelected(Product selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public Product prepareCreate() {
        selected = new Product();
        initializeEmbeddableKey();
        return selected;
    }

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    getEjbFacade().edit(selected);
                } else {
                    getEjbFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public void create() {
        this.selected.setCompany(ejb.find(companyId));
//        System.out.println(companyId);
        persist(PersistAction.CREATE, "Adicionado com sucesso");
        if (!JsfUtil.isValidationFailed()) {
            products = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, "Atualizado com sucesso");
    }

    public void delete() {
        persist(PersistAction.DELETE, "Removido com sucesso");
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            products = null;    // Invalidate list of items to trigger re-query.
        }
    }

    @FacesConverter(forClass = Product.class)
    public static class ProductControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductController controller = (ProductController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productController");
            return controller.getProduct(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Product) {
                Product o = (Product) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Product.class.getName()});
                return null;
            }
        }

    }
}
