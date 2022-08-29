/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bean.CompanyFacade;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import model.Company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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
import model.Product;

/**
 *
 * @author ghmenezes
 */
@Named("companyController")
@SessionScoped
public class CompanyController implements Serializable {

    @EJB
    private bean.CompanyFacade ejb;
    private List<Company> companies = null;
    private Company company = new Company();
    private boolean userSelected = false;
    private Long selectedId;

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }

    public CompanyController() {
    }

    public Company getCompany(Long id) {
        return getEjbFacade().find(id);
    }

    public CompanyFacade getEjbFacade() {
        return ejb;
    }

    public List<Company> getCompanies() {
            companies = getEjbFacade().findAll();
        return companies;
    }

    public Company getCompany() {
        return company;
    }
    
    public List<Product> getMenu(){
        List<Product> selectedMenu;
        company = this.getCompanyById(this.selectedId);
//        System.out.println(company.getId());
        if(company.getMenu().isEmpty()){
            selectedMenu = new ArrayList<>();
        } else {
            selectedMenu = company.getMenu();
        }
//        System.out.println(selectedMenu.size());
        return selectedMenu;
    }
    
    public Company getCompanyById(Long id){
        company = getEjbFacade().find(id);
//        System.out.println(company);
        return getEjbFacade().find(id);
    }
    
    public Company getCompanyByCNPJ(String cnpj){
        company = getEjbFacade().find(cnpj);
//        System.out.println(company);
        return getEjbFacade().find(cnpj);
    }

    public boolean isUserSelected() {
        return userSelected;
    }

    public void setUserSelected(boolean userSelected) {
        this.userSelected = userSelected;
    }
    
//
//    protected void setEmbeddableKeys() {
//    }
//
//    protected void initializeEmbeddableKey() {
//    }

//    public Company prepareCreate() {
//        selected = new Company();
//        initializeEmbeddableKey();
//        return selected;
//    }
    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (company != null) {
//            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    getEjbFacade().edit(company);
                } else {
                    getEjbFacade().remove(company);
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
        if(!isCNPJ(this.company) || validCNPJ(this.company.getCnpj().replaceAll("[^a-zA-Z0-9]", ""))){
            JsfUtil.addErrorMessage("CNPJ Inválido");
        } else if (alreadyRegistered(this.company)){
            JsfUtil.addErrorMessage("CNPJ informado já está cadastrado");
        } else if (this.company.getPassword().length() < 4){
            JsfUtil.addErrorMessage("Sua senha deve ter no mínimo 4 caracteres");
        }else if (!isEmail(this.company)){
            JsfUtil.addErrorMessage("Email Inválido");
        } else {
            this.company.setCnpj(this.company.getCnpj().replaceAll("[^a-zA-Z0-9]", ""));
        persist(PersistAction.CREATE, "Empresa cadastrada com sucesso");
        if (!JsfUtil.isValidationFailed()) {
            this.company = new Company();    // Invalidate list of items to trigger re-query.
//        System.out.println("Criando empresa");
        }
        }
    }
    
    public boolean isCNPJ(Company company){
        return (company.getCnpj().replaceAll("[^a-zA-Z0-9]", "").length() == 14);
    }
    
    public boolean validCNPJ(String CNPJ){
        return (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
            CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
            CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
            CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
            CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999"));
    }
    
    public boolean isEmail(Company company){
        return (company.getEmail().length() >= 11) && company.getEmail().contains("@") && company.getEmail().contains(".com");
    }
    
    public boolean alreadyRegistered(Company company){
        String cnpj = company.getCnpj().replaceAll("[^a-zA-Z0-9]", "");
        List<Company> list = getCompanies();
        return list.stream().anyMatch((item) -> (item.getCnpj().equals(cnpj)));
    }

    public void update() {
        persist(PersistAction.UPDATE, "Atualizado com sucesso");
    }

    public void delete() {
        persist(PersistAction.DELETE, "Removido com sucesso");
        if (!JsfUtil.isValidationFailed()) {
            company = null; // Remove selection
            companies = null;    // Invalidate list of items to trigger re-query.
        }
    }

    @FacesConverter(forClass = Company.class)
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
            if (object instanceof Company) {
                Company o = (Company) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Company.class.getName()});
                return null;
            }
        }

    }

}
