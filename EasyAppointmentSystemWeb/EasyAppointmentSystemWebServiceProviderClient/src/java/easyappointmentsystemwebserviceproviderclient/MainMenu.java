package easyappointmentsystemwebserviceproviderclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.ServiceProviderEntity;
import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AppointmentNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UpdateServiceProviderException;


public class MainMenu 
{
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
     
    private ServiceProviderEntity currentServiceProviderEntity;
    
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
            
    public MainMenu()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public MainMenu(ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, ServiceProviderEntity serviceProviderEntity, AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote)
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentServiceProviderEntity = serviceProviderEntity;
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
    }
    
    public void menu()
    {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Service provider terminal :: Main ***\n");
            System.out.println("You are login as " + currentServiceProviderEntity.getName() + "\n");
            System.out.println("1: View Profile");
            System.out.println("2: Edit Profile");
            System.out.println("3: View Appointments");
            System.out.println("4: Cancel Appointment");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = sc.nextInt();

                if(response == 1)
                {
                    viewProfile();
                }
                else if(response == 2)
                {
                    editProfile(currentServiceProviderEntity);
                }
                else if (response == 3)
                {
                    viewAppointment();
                }
                else if (response == 4)
                {
                    cancelAppointment();
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5)
            {
                break;
            }
        }
    }
    
    public void viewProfile()
    {
        System.out.println("*** Service provider terminal :: View Profile ***\n");
        System.out.println("Name: " + currentServiceProviderEntity.getName());
        System.out.println("Business Category: " + currentServiceProviderEntity.getBusinessCategory());
        System.out.println("Business Registration Number: " + currentServiceProviderEntity.getBusinessRegistrationNumber());
        System.out.println("Business Address: " + currentServiceProviderEntity.getBusinessAddress());
        System.out.println("City: " + currentServiceProviderEntity.getCity());
        System.out.println("Email Address: " + currentServiceProviderEntity.getEmailAddress());
        System.out.println("Phone Number: " + currentServiceProviderEntity.getPhoneNumber());
        //System.out.println("Overall Rating: " + currentServiceProviderEntity.getBusinessCategory());
    }
    
    public void editProfile(ServiceProviderEntity serviceProviderEntity)
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("*** Service provider terminal :: Edit Profile ***\n");
        System.out.print("Enter City (blank if no change)> ");
        String city = sc.nextLine().trim();
        if (city.length() > 0) 
        {
            serviceProviderEntity.setCity(city);

        }
        System.out.print("Enter Business address (blank if no change)> ");
        String businessAddr = sc.nextLine().trim();
        if (businessAddr.length() > 0) 
        {
            serviceProviderEntity.setBusinessAddress(businessAddr);

        }
        System.out.print("Enter Email address (blank if no change)> ");
        String emailAddr = sc.nextLine().trim();
        if (emailAddr.length() > 0) 
        {
            serviceProviderEntity.setEmailAddress(emailAddr);

        }
        System.out.print("Enter Phone number (blank if no change)> ");
        String phoneNumber = sc.nextLine().trim();
        if (phoneNumber.length() > 0) 
        {
            serviceProviderEntity.setPhoneNumber(phoneNumber);

        }
        System.out.print("Enter Password (blank if no change)> ");
        String password = sc.nextLine().trim();
        if (password.length() > 0) 
        {
            serviceProviderEntity.setPassword(password);

        }
        
        Set<ConstraintViolation<ServiceProviderEntity>>constraintViolations = validator.validate(serviceProviderEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                serviceProviderEntitySessionBeanRemote.updateServiceProvider(serviceProviderEntity);
                currentServiceProviderEntity = serviceProviderEntity; //update the current service provider if it can be updated in database
            }
            catch (UpdateServiceProviderException | ServiceProviderNotFoundException ex)
            {
                System.out.println("Profile has NOT been updated!\n");
            }
            catch (InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForServiceProviderEntity(constraintViolations);
        }
    }
    
    public void viewAppointment()
    {  
        Scanner sc = new Scanner(System.in);
        String response = "";
        
        List<AppointmentEntity> appointments = currentServiceProviderEntity.getAppointmentEntities();
        
        System.out.println("*** Service provider terminal :: View Appointments ***\n");
        System.out.print("Appointments: ");
        System.out.printf("%16s%10s%7s%9s\n", "Name", "Date", "Time", "Appointment No.");
        
        for(AppointmentEntity appointment:appointments)
        {
            System.out.printf("%16s%10s%7s%9s\n", appointment.getCustomerEntity().getFullName(), appointment.getScheduledDate(), appointment.getScheduledTime(), appointment.getAppointmentNo());
        }

        while (response != "0")
        {
            System.out.println("Enter 0 to go back to the previous menu.");  
            response = sc.nextLine().trim();
        }
    }
    
    public void cancelAppointment()
    {
        Scanner sc = new Scanner(System.in);
        String response = "";
        
        List<AppointmentEntity> appointments = currentServiceProviderEntity.getAppointmentEntities();
        
        System.out.println("*** Service provider terminal :: Delete Appointments ***\n");
        System.out.print("Appointments: ");
        System.out.printf("%16s%10s%7s%9s\n", "Name", "Date", "Time", "Appointment No.");
        
        for(AppointmentEntity appointment:appointments)
        {
            System.out.printf("%16s%10s%7s%9s\n", appointment.getCustomerEntity().getFullName(), appointment.getScheduledDate(), appointment.getScheduledTime(), appointment.getAppointmentNo());
        }
        
        System.out.println("Enter Appointment Id> ");
        String appointmentId = sc.nextLine().trim();
        
        try 
        {
            appointmentEntitySessionBeanRemote.deleteAppointment(appointmentId);
        }
        catch (AppointmentNotFoundException ex)
        {
            System.out.println("Appointment Number " + appointmentId + " cannot be found!");
        }
        
        while (response != "0")
        {
            System.out.println("Enter 0 to go back to the previous menu.");  
            response = sc.nextLine().trim();
        }

    }
    
    private void showInputDataValidationErrorsForServiceProviderEntity(Set<ConstraintViolation<ServiceProviderEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }    
    
    
}
