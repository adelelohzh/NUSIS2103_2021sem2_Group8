package ejb.session.singleton;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
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
import java.util.List;
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
import util.exception.AppointmentNotFoundException;
import util.exception.AppointmentNumberExistsException;
import util.exception.BusinessCategoryExistException;
import util.exception.CustomerEmailExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderBlockedException;
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
            customerEntitySessionBeanLocal.createNewCustomer(new CustomerEntity("S9876223A", "password", "Lily", "Manni", 'F', 21, "99836123", "9 Sentosa Cove", "Singapore", "lilymanni@gmail.com"));
            businessCategoryEntitySessionBeanLocal.createNewBusinessCategoryEntity(new BusinessCategoryEntity("Health"));
            businessCategoryEntitySessionBeanLocal.createNewBusinessCategoryEntity(new BusinessCategoryEntity("Fashion"));
            businessCategoryEntitySessionBeanLocal.createNewBusinessCategoryEntity(new BusinessCategoryEntity("Education"));
            
            ServiceProviderEntity newServiceProvider = new ServiceProviderEntity();
            BusinessCategoryEntity bizCategoryEntity = businessCategoryEntitySessionBeanLocal.retrieveAllBusinessCategories().get(0);
            newServiceProvider.setName("Kevin Peterson");
            newServiceProvider.setBusinessCategory(bizCategoryEntity.getCategory());
            newServiceProvider.setBusinessAddress("13, Clementi Road");
            newServiceProvider.setCity("Singapore");
            newServiceProvider.setBusinessRegistrationNumber("1111001111");
            newServiceProvider.setEmailAddress("kevin@nuh.com.sg");
            newServiceProvider.setPhoneNumber("93718799");
            newServiceProvider.setPassword("113322");
            newServiceProvider.setStatusEnum(StatusEnum.Approved);
            newServiceProvider.setBusinessCategoryEntity(bizCategoryEntity);
            serviceProviderEntitySessionBeanLocal.createNewServiceProvider(newServiceProvider);
            
            ServiceProviderEntity newServiceProvider2 = new ServiceProviderEntity();
            BusinessCategoryEntity bizCategoryEntity2 = businessCategoryEntitySessionBeanLocal.retrieveAllBusinessCategories().get(1);
            newServiceProvider2.setName("Christian Dior");
            newServiceProvider2.setBusinessCategory(bizCategoryEntity2.getCategory());
            newServiceProvider2.setBusinessAddress("15, Marina One Drive");
            newServiceProvider2.setCity("Singapore");
            newServiceProvider2.setBusinessRegistrationNumber("1111001112");
            newServiceProvider2.setEmailAddress("dior@gmail.com");
            newServiceProvider2.setPhoneNumber("94827980");
            newServiceProvider2.setPassword("113322");
            newServiceProvider2.setStatusEnum(StatusEnum.Approved);
            newServiceProvider2.setBusinessCategoryEntity(bizCategoryEntity2);
            serviceProviderEntitySessionBeanLocal.createNewServiceProvider(newServiceProvider2);
            //serviceProviderEntitySessionBeanLocal.createNewServiceProvider(new ServiceProviderEntity("Kevin Peterson", "Health",  "1111001111", "Singapore", "13, Clementi Road", "kevin@nuh.com.sg", "93718799", "113322", StatusEnum.Approved));
            //serviceProviderEntitySessionBeanLocal.createNewServiceProvider(new ServiceProviderEntity("Christian Dior", "Fashion",  "1111001112", "Singapore", "15, Marina One Drive", "dior@gmail.com", "94827980", "113322", StatusEnum.Approved));

            
            CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerEntityByCustomerId(1L);
            CustomerEntity customerEntityTwo = customerEntitySessionBeanLocal.retrieveCustomerEntityByCustomerId(2L);
            BusinessCategoryEntity businessCategoryEntity = businessCategoryEntitySessionBeanLocal.retrieveAllBusinessCategories().get(0);
            BusinessCategoryEntity businessCategoryEntityTwo = businessCategoryEntitySessionBeanLocal.retrieveAllBusinessCategories().get(1);
            ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(1L);
            ServiceProviderEntity serviceProviderEntityTwo = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(2L);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currDate = "2021-04-12";
            LocalDate date = LocalDate.parse(currDate, formatter);
            LocalDate dateTwo = LocalDate.parse("2021-04-14", formatter);

            appointmentEntitySessionBeanLocal.createNewAppointment(1L, 1L, new AppointmentEntity("104120830", LocalTime.parse("08:30", DateTimeFormatter.ofPattern("HH:mm")), date, customerEntity, serviceProviderEntity, businessCategoryEntity));
            appointmentEntitySessionBeanLocal.createNewAppointment(1L, 1L, new AppointmentEntity("104140830", LocalTime.parse("08:30", DateTimeFormatter.ofPattern("HH:mm")), dateTwo, customerEntity, serviceProviderEntity, businessCategoryEntity));
            appointmentEntitySessionBeanLocal.createNewAppointment(1L, 2L, new AppointmentEntity("204121130", LocalTime.parse("11:30", DateTimeFormatter.ofPattern("HH:mm")), date, customerEntity, serviceProviderEntityTwo, businessCategoryEntity));
            appointmentEntitySessionBeanLocal.createNewAppointment(1L, 2L, new AppointmentEntity("204141130", LocalTime.parse("11:30", DateTimeFormatter.ofPattern("HH:mm")), dateTwo, customerEntity, serviceProviderEntityTwo, businessCategoryEntity));
            
            appointmentEntitySessionBeanLocal.createNewAppointment(2L, 1L, new AppointmentEntity("104121130", LocalTime.parse("11:30", DateTimeFormatter.ofPattern("HH:mm")), date, customerEntityTwo, serviceProviderEntity, businessCategoryEntityTwo));
            appointmentEntitySessionBeanLocal.createNewAppointment(2L, 1L, new AppointmentEntity("104141130", LocalTime.parse("11:30", DateTimeFormatter.ofPattern("HH:mm")), dateTwo, customerEntityTwo, serviceProviderEntity, businessCategoryEntityTwo));
            appointmentEntitySessionBeanLocal.createNewAppointment(2L, 2L, new AppointmentEntity("204120830", LocalTime.parse("08:30", DateTimeFormatter.ofPattern("HH:mm")), date, customerEntityTwo, serviceProviderEntityTwo, businessCategoryEntityTwo));
            appointmentEntitySessionBeanLocal.createNewAppointment(2L, 2L, new AppointmentEntity("204140830", LocalTime.parse("08:30", DateTimeFormatter.ofPattern("HH:mm")), dateTwo, customerEntityTwo, serviceProviderEntityTwo, businessCategoryEntityTwo));
            
            
            AppointmentEntity appointmentOne = appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(1L);
            AppointmentEntity appointmentTwo = appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(2L);
            AppointmentEntity appointmentThree = appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(3L);
            AppointmentEntity appointmentFour = appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(4L);
            
            AppointmentEntity appointmentFive = appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(5L);
            AppointmentEntity appointmentSix = appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(6L);
            AppointmentEntity appointmentSeven = appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(7L);
            AppointmentEntity appointmentEight = appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(8L);
            
            List<AppointmentEntity> apptListOne = customerEntity.getAppointmentEntities();
            apptListOne.add(appointmentOne);
            apptListOne.add(appointmentTwo);
            apptListOne.add(appointmentThree);
            apptListOne.add(appointmentFour);
            
            List<AppointmentEntity> apptListTwo = customerEntityTwo.getAppointmentEntities();
            apptListTwo.add(appointmentFive);
            apptListTwo.add(appointmentSix);
            apptListTwo.add(appointmentSeven);
            apptListTwo.add(appointmentEight);
            
            List<AppointmentEntity> apptListThree = serviceProviderEntity.getAppointmentEntities();
            apptListThree.add(appointmentOne);
            apptListThree.add(appointmentTwo);
            apptListThree.add(appointmentFive);
            apptListThree.add(appointmentSix);
            
            List<AppointmentEntity> apptListFour = serviceProviderEntityTwo.getAppointmentEntities();
            apptListThree.add(appointmentThree);
            apptListThree.add(appointmentFour);
            apptListThree.add(appointmentSeven);
            apptListThree.add(appointmentEight);
            
            customerEntity.setAppointmentEntities(apptListOne);
            customerEntityTwo.setAppointmentEntities(apptListTwo);
            serviceProviderEntity.setAppointmentEntities(apptListThree);
            serviceProviderEntityTwo.setAppointmentEntities(apptListFour);

            
        }
        catch(AdminUsernameExistException |  CustomerEmailExistsException | BusinessCategoryExistException | UnknownPersistenceException | InputDataValidationException | CustomerNotFoundException | AppointmentNumberExistsException ex)
        {
            ex.printStackTrace();
        } catch (ServiceProviderNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceProviderBlockedException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceProviderEmailExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AppointmentNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}

//    public void persist(Object object) {
//        em.persist(object);
//    }
//}