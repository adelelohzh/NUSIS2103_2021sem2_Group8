package ejb.session.singleton;

import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.BusinessCategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ServiceProviderEntitySessionBeanLocal;
import entity.AdminEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.StatusEnum;
import util.exception.AdminNotFoundException;
import util.exception.AdminUsernameExistException;
import util.exception.BusinessCategoryExistException;
import util.exception.CustomerEmailExistsException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderEmailExistException;
import util.exception.UnknownPersistenceException;


@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB(name = "ServiceProviderEntitySessionBeanLocal")
    private ServiceProviderEntitySessionBeanLocal serviceProviderEntitySessionBeanLocal;

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB(name = "BusinessCategoryEntitySessionBeanLocal")
    private BusinessCategoryEntitySessionBeanLocal businessCategoryEntitySessionBeanLocal;

    @EJB(name = "AdminEntitySessionBeanLocal")
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;
    
    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;
    
    public DataInitializationSessionBean()
    {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        try {
            adminEntitySessionBeanLocal.retrieveAdminEntityById(1L);
            AdminEntity admin = em.find(AdminEntity.class, 1L);
        } 
        catch (AdminNotFoundException ex) {
                initializeData();
                
        }
    }

    
    private void initializeData() 
    {
        try
        {
            adminEntitySessionBeanLocal.createNewAdmin(new AdminEntity("Admin01", "admin01@gmail.com", "password"));
            adminEntitySessionBeanLocal.createNewAdmin(new AdminEntity("Admin02", "admin02@gmail.com", "password"));
            customerEntitySessionBeanLocal.createNewCustomer(new CustomerEntity("T020202F", "password", "Jack", "Son", 'M', 19, "03399393", "Jurong East 992", "Singapore", "jackson99@hotmail.com"));
            businessCategoryEntitySessionBeanLocal.createNewBusinessCategoryEntity(new BusinessCategoryEntity("Health"));
            businessCategoryEntitySessionBeanLocal.createNewBusinessCategoryEntity(new BusinessCategoryEntity("Fashion"));
            businessCategoryEntitySessionBeanLocal.createNewBusinessCategoryEntity(new BusinessCategoryEntity("Education"));
            serviceProviderEntitySessionBeanLocal.createNewServiceProvider(new ServiceProviderEntity("Kevin Peterson", "Health",  "1111001111", "Singapore", "13, Clementi Road", "kevin@nuh.com.sg", "93718799", "113322", StatusEnum.Approved));
        }
        catch(AdminUsernameExistException |  CustomerEmailExistsException | BusinessCategoryExistException | ServiceProviderEmailExistException | UnknownPersistenceException | InputDataValidationException ex)
        {
            ex.printStackTrace();
        } 
    }
}

//    public void persist(Object object) {
//        em.persist(object);
//    }
//}