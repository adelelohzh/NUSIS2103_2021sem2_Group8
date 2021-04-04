package easyappointmentsystemwebadminclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.util.List;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.StatusEnum;
import util.exception.BusinessCategoryExistException;
import util.exception.CreateNewBusinessCategoryException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;

public class SystemAdministrationModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote;

    private AdminEntity currentAdminEntity;

    public SystemAdministrationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SystemAdministrationModule(CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, AdminEntity currentAdminEntity) {
        this();
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentAdminEntity = currentAdminEntity;
    }

    public void viewCustomerAppointments() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for customers ***\n");
        Long customerId;
        
        do {
            System.out.print("Enter customer Id> ");
            customerId = sc.nextLong();
            
            try {
                CustomerEntity customerEntity = customerEntitySessionBeanRemote.retrieveCustomerEntityByCustomerId(customerId);
                //retrieve appointments
                List<AppointmentEntity> appointmentEntities = customerEntity.getAppointmentEntities();
                //print appointments
                System.out.println("Appointments:");
                System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", "Name", "| Business Category", "| Date", "| Time", "| Appointment No.");
                for (AppointmentEntity appointmentEntity : appointmentEntities) {
                    System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", customerEntity.getFullName(), "| " + appointmentEntity.getBusinessCategoryEntity(), "| " +  appointmentEntity.getScheduledDate(), "| " + appointmentEntity.getScheduledTime(), "| " + appointmentEntity.getAppointmentNo());
                }
            } catch (CustomerNotFoundException ex) {
                System.out.println("An error has occurred while retrieving customer: " + ex.getMessage() + "\n");
            }
            
            System.out.println("Enter 0 to go back to the previous menu.");
        } while (customerId != 0);
    } 

    public void viewServiceProviderAppointments() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for service providers ***\n");
        Long serviceProviderId;
        
        do {
            System.out.print("Enter service provider Id> ");
            serviceProviderId = sc.nextLong();
            try {
                ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
                List<AppointmentEntity> appointmentEntities = serviceProviderEntity.getAppointmentEntities();
                System.out.println("Appointments:");
                System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", "Name", "| Business Category", "| Date", "| Time", "| Appointment No.");
                
                for (AppointmentEntity appointmentEntity : appointmentEntities) {
                    System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", serviceProviderEntity.getName(), "| " + appointmentEntity.getBusinessCategoryEntity(), "| " +  appointmentEntity.getScheduledDate(), "| " + appointmentEntity.getScheduledTime(), "| " + appointmentEntity.getAppointmentNo());
                }
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("An error has occurred while retrieving service provider: " + ex.getMessage() + "\n");
            }
        } while (serviceProviderId != 0);
    }

    public void viewServiceProviders() {

        System.out.println("*** Admin terminal :: View service providers ***\n");

        List<ServiceProviderEntity> serviceProviderEntities = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviderEntity();
        System.out.printf("%-15s%-20s%-15s%-20s%-15s\n", "Name", "| Business category", "| City", "| Overall Rating", "| Status");

        for (ServiceProviderEntity serviceProviderEntity : serviceProviderEntities) {
            System.out.printf("%-15s%-20s%-15s%-20s%-15s\n", serviceProviderEntity.getName(), "| " + serviceProviderEntity.getBusinessCategory(), "| " + serviceProviderEntity.getCity(), "| " + serviceProviderEntity.getRating(), "| " + serviceProviderEntity.getStatusEnum());
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();

    }

    public void approveServiceProviders() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Approve service provider ***\n");

        List<ServiceProviderEntity> serviceProviderEntities = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviderEntity();
        System.out.println("List of service providers with pending approval:");
        System.out.printf("%-3s%-7s%-20s%-20s%-7s%-10s%-8s%-7s\n", "Id", "| Name", "| Business category", "| Business Reg. No.", "| City", "| Address", "| Email", "| Phone");

        for (ServiceProviderEntity serviceProviderEntity : serviceProviderEntities) {
            if (serviceProviderEntity.getStatusEnum() == StatusEnum.Pending) {
                System.out.printf("%-3s%-7s%-20s%-20s%-7s%-10s%-8s%-7s\n", serviceProviderEntity.getServiceProviderId(), "| " + serviceProviderEntity.getName(), "| " + serviceProviderEntity.getBusinessCategory(), "| " + serviceProviderEntity.getBusinessRegistrationNumber(), "| " + serviceProviderEntity.getCity(), "| " + serviceProviderEntity.getBusinessAddress(),"| " + serviceProviderEntity.getEmailAddress(), "| " + serviceProviderEntity.getPhoneNumber());
                System.out.println();
            }
        }
        
        do {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter service provider Id> ");
            Long serviceProviderId = sc.nextLong();
            if (serviceProviderId == 0) {
                break;
            }
           
            try {
                ServiceProviderEntity serviceProvider = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
                serviceProvider.setStatusEnum(StatusEnum.Approved);
                System.out.println(serviceProvider.getName() + "'s registration is approved.");
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("An error has occurred while retrieving service provider: " + ex.getMessage() + "\n");
            }
        } while (serviceProviderId != 0); 
    }

    public void blockServiceProviders() {

        Scanner sc = new Scanner(System.in);

        System.out.println("*** Admin terminal :: Block service provider ***\n");
        List<ServiceProviderEntity> serviceProviderEntities = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviderEntity();
        viewServiceProviders();
      
        do {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter service provider Id> ");
            Long serviceProviderId = sc.nextLong();
            if (serviceProviderId == 0) {
                break;
            }
            
            try {
                ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
                serviceProviderEntity.setStatusEnum(StatusEnum.Blocked);
                System.out.println(serviceProviderEntity.getName() + " has been blocked.");
            } 
            catch (ServiceProviderNotFoundException ex) {
                System.out.println("An error has occurred while retrieving service provider: " + ex.getMessage() + "\n");
            }
        } while (serviceProviderId != 0);
    }

    public void addBusinessCategory() throws BusinessCategoryExistException, CreateNewBusinessCategoryException {
        
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Admin terminal :: Add a Business category ***\n");
        System.out.print("Enter 0 to go back to the previous menu.");
        Long input = sc.nextLong();
        
        while (input != 0) {
            System.out.print("Enter 0 to go back to the previous menu.");
            System.out.print("Enter a new business category> ");
            String category = sc.nextLine().trim();
            System.out.println();
        
            List<BusinessCategoryEntity> businessCategoryEntities = businessCategoryEntitySessionBeanRemote.retrieveAllBusinessCategories();
            
            for (BusinessCategoryEntity businessCategory: businessCategoryEntities) { 
                
                if (businessCategory.getCategory().equals(category)) {
                    throw new BusinessCategoryExistException("Business Category " + category + " already exists!");
                } else {
                    BusinessCategoryEntity newBusinessCategory = new BusinessCategoryEntity();
                    newBusinessCategory.setCategory(category);
                    businessCategoryEntities.add(newBusinessCategory);
                    businessCategoryEntitySessionBeanRemote.createNewBusinessCategoryEntity(newBusinessCategory);
                    
                    System.out.println("The business category \"\"" + category + "\" is added.");
                    System.out.print("Enter 0 to go back to the previous menu.");
                    input = sc.nextLong();
                }

        }
    }

    public void removeBusinessCategory() {
        
        Scanner sc = new Scanner(System.in);

        System.out.println("*** Admin terminal :: Remote a Business category ***\n");
        System.out.print("Enter 0 to go back to the previous menu.");
        Long input = sc.nextLong();
        
        System.out.print("Enter a business category to remove: ");
        String category = sc.nextLine().trim();
        
        /*while (input != 0) {
        
            List<BusinessCategoryEntity> businessCategoryEntities = currentAdminEntity.getBusinessCategoryEntities();

            if (businessCategoryEntities.contains(category)) {
                currentAdminEntity.setBusinessCategoryEntities(businessCategoryEntities);

                System.out.println();

                System.out.println("The business category \"\"" + category + "\" is added.");
                System.out.print("Enter 0 to go back to the previous menu.");
                System.out.print("Enter a new business category: ");
                input = sc.nextLong();
            } else {
                //throw new BusinessCategoryNotFoundException("Business Category " + category + " does not exist!");
            }
        }*/
    }

    public void sendReminderEmail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
