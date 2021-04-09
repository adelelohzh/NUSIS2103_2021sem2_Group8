package ejb.session.singleton;

import ejb.session.stateful.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.BusinessCategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ServiceProviderEntitySessionBeanLocal;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import util.exception.AppointmentNumberExistsException;
import util.exception.BusinessCategoryExistException;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderEmailExistException;
import util.exception.ServiceProviderNotFoundException;
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
    
    @EJB(name = "AppointmentEntitySessionBeanLocal")
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;
    
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
        
            CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerEntityByCustomerId(1L);
            BusinessCategoryEntity businessCategoryEntity = businessCategoryEntitySessionBeanLocal.retrieveAllBusinessCategories().get(0);
            ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(1L);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currDate = "2019-04-12";
            LocalDate date = LocalDate.parse(currDate, formatter);
            
            String appointmentId = "104120830";

            LocalTime scheduledTime = LocalTime.parse("08:30", DateTimeFormatter.ofPattern("HH:mm"));

            appointmentEntitySessionBeanLocal.createNewAppointment(1L, 1L, new AppointmentEntity(appointmentId, scheduledTime, date, customerEntity, serviceProviderEntity, businessCategoryEntity));
        
        }
        catch(AdminUsernameExistException |  CustomerEmailExistsException | BusinessCategoryExistException | ServiceProviderEmailExistException | UnknownPersistenceException | InputDataValidationException | CustomerNotFoundException | AppointmentNumberExistsException ex)
        {
            ex.printStackTrace();
        } catch (ServiceProviderNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}

//    public void persist(Object object) {
//        em.persist(object);
//    }
//}