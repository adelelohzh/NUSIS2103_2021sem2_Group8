package easyappointmentsystemwebadminclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.util.List;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;


public class SystemAdministrationModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    
    private AdminEntity currentAdminEntity;
    
    
    public SystemAdministrationModule()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public SystemAdministrationModule(CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, AdminEntity currentAdminEntity) 
    {
        this();
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentAdminEntity = currentAdminEntity;
    }

    public void viewCustomerAppointments() {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for customers ***\n");
        System.out.print("Enter customer Id> ");
        
        Long customerId = sc.nextLong();
        
        try {
            CustomerEntity customerEntity = customerEntitySessionBeanRemote.retrieveCustomerEntityByCustomerId(customerId);
            //retrieve appointments
            List<AppointmentEntity> appointmentEntities = customerEntity.getAppointmentEntities();
            //print appointments
            System.out.println("Appointments:");
            System.out.println("Name           | Business category | Date       | Time  | Appointment No.");
            System.out.println(customerEntity.
                    
            System.out.println("Enter 0 to go back to the previous menu.");
            
            System.out.print("Enter customer Id> ");
            Long input = sc.nextLong();   
        }  
        catch(CustomerNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving customer: " + ex.getMessage() + "\n");
        }
    }

    public void viewServiceProviderAppointments() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for service providers ***\n");
        System.out.print("Enter service provider Id> ");
        
        Long serviceProviderId = sc.nextLong();
        
        try {
            ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
            //retrieve appointments
            //print appointments
            System.out.println("Enter 0 to go back to the previous menu.");
            
            System.out.print("Enter service provider Id> ");
            Long input = sc.nextLong();   
        }
        catch (ServiceProviderNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving service provider: " + ex.getMessage() + "\n");
        }
    }

    public void viewServiceProviders() {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View service providers ***\n");
        System.out.println("*** POS System :: System Administration :: View All Service Providers ***\n");
        
        List<ServiceProviderEntity> serviceProviderEntities = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviderEntity();
        //System.out.printf("%8s%20s%20s%15s%20s%20s\n", "Id | ", "Name | ", "Business Category | ", "Business Reg. No. | ", "City | ", " Address | ", " Email | ", "Phone");

        for(ServiceProviderEntity serviceProviderEntity:serviceProviderEntities)
        {
            //System.out.printf("%8s%20s%20s%15s%20s%20s\n", serviceProviderEntity.getServiceProviderId().toString(), serviceProviderEntity.getName(), staffEntity.getPassword());
        }
        
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void approveServiceProviders() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void blockServiceProviders() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addBusinessCategory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeBusinessCategory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendReminderEmail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
    