package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Product;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-08-22T02:25:56")
@StaticMetamodel(Company.class)
public class Company_ { 

    public static volatile SingularAttribute<Company, String> password;
    public static volatile SingularAttribute<Company, String> phone;
    public static volatile SingularAttribute<Company, String> name;
    public static volatile SingularAttribute<Company, Long> id;
    public static volatile SingularAttribute<Company, String> cnpj;
    public static volatile ListAttribute<Company, Product> menu;
    public static volatile SingularAttribute<Company, String> email;

}