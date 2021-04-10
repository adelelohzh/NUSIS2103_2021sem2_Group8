package easyappointmentsystemwebserviceproviderclient;

import ejb.session.stateful.AppointmentEntitySessionBeanRemote;
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
                    editProfile();
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

        Scanner sc = new Scanner(System.in);
        String response = "";
       
        System.out.println("*** Service provider terminal :: View Profile ***\n");
        System.out.println("Profile: ");
        System.out.printf("%-15s%-20s%-30s%-15s%-30s%-15s%-20s%-20s%-15s\n", "Name", "| Business category", "| Business Registration Number", "| City", "| Address", "| Email", "| Phone Number", "| Overall Rating", "| Status");
        System.out.printf("%-15s%-20s%-30s%-15s%-30s%-15s%-20s%-20s%-15s\n", currentServiceProviderEntity.getName(), "| " + currentServiceProviderEntity.getBusinessCategory(), "| " + currentServiceProviderEntity.getBusinessRegistrationNumber(), "| " + currentServiceProviderEntity.getCity(), "| " + currentServiceProviderEntity.getBusinessAddress(), "| " + currentServiceProviderEntity.getEmailAddress(), "| " + currentServiceProviderEntity.getPhoneNumber(), "| " + currentServiceProviderEntity.getRating(), "| " + currentServiceProviderEntity.getStatusEnum());
            
        while (!response.equals("0"))
        {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print(">");
            response = sc.nextLine().trim();
        }
    }

    
    public void editProfile()
    {
        Scanner sc = new Scanner(System.in);
        String response = "";
        
        System.out.println("*** Service provider terminal :: Edit Profile ***\n");
        
        System.out.print("Enter Name (blank if no change)> ");
        String name = sc.nextLine().trim();
        if (name.length() > 0) 
        {
            currentServiceProviderEntity.setName(name);

        }
        System.out.print("Enter Business Category (blank if no change)> ");
        String category = sc.nextLine().trim();
        if (category.length() > 0) 
        {
            currentServiceProviderEntity.setBusinessCategory(category);

        }
        System.out.print("Enter Business Registration Number (blank if no change)> ");
        String regNo = sc.nextLine().trim();
        if (regNo.length() > 0) 
        {
            currentServiceProviderEntity.setBusinessCategory(regNo);

        }
        System.out.print("Enter City (blank if no change)> ");
        String city = sc.nextLine().trim();
        if (city.length() > 0) 
        {
            currentServiceProviderEntity.setCity(city);

        }
        System.out.print("Enter Business address (blank if no change)> ");
        String businessAddr = sc.nextLine().trim();
        if (businessAddr.length() > 0) 
        {
            currentServiceProviderEntity.setBusinessAddress(businessAddr);

        }
        System.out.print("Enter Email address (blank if no change)> ");
        String emailAddr = sc.nextLine().trim();
        if (emailAddr.length() > 0) 
        {
            currentServiceProviderEntity.setEmailAddress(emailAddr);

        }
        System.out.print("Enter Phone number (blank if no change)> ");
        String phoneNumber = sc.nextLine().trim();
        if (phoneNumber.length() > 0) 
        {
            currentServiceProviderEntity.setPhoneNumber(phoneNumber);

        }
        System.out.print("Enter Password (blank if no change)> ");
        String password = sc.nextLine().trim();
        if (password.length() > 0) 
        {
            currentServiceProviderEntity.setPassword(password);

        }
        
        Set<ConstraintViolation<ServiceProviderEntity>>constraintViolations = validator.validate(currentServiceProviderEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                serviceProviderEntitySessionBeanRemote.updateServiceProvider(currentServiceProviderEntity);
                System.out.println("Update successful!\n");
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
        
        while (!response.equals("0"))
        {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print(">");
            response = sc.nextLine().trim();
        }
    }
    
    public void viewAppointment()
    {  
        Scanner sc = new Scanner(System.in);
        String response = "";
        
        List<AppointmentEntity> appointments = currentServiceProviderEntity.getAppointmentEntities();
        
        System.out.println("*** Service provider terminal :: View Appointments ***\n");
        System.out.println("Appointments: ");
        System.out.printf("%-15s%-13s%-8s%-15s\n", "Name", "| Date", "| Time", "| Appointment No.");
        
        for(AppointmentEntity appointment:appointments)
        {
            System.out.printf("%-15s%-13s%-8s%-15s\n", appointment.getCustomerEntity().getFullName(), "| " + appointment.getScheduledDate(), "| " + appointment.getScheduledTime(), "| " + appointment.getAppointmentNo());
        }

        while (!response.equals("0"))
        {
            System.out.println("Enter 0 to go back to the previous menu."); 
            System.out.print(">");
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
        System.out.printf("%-15s%-13s%-8s%-15s\n", "Name", "| Date", "| Time", "| Appointment No.");
        
        for(AppointmentEntity appointment:appointments)
        {
            System.out.printf("%-15s%-13s%-8s%-15s\n", appointment.getCustomerEntity().getFullName(), "| " + appointment.getScheduledDate(), "| " + appointment.getScheduledTime(), "| " + appointment.getAppointmentNo());
        }
        
        while (!response.equals("0")) 
        {
            System.out.println("Enter 0 to go back to the previous menu.");  
            System.out.print("Enter Appointment Id> ");
            response = sc.nextLine().trim();
            
            if (!response.equals("0"))
            {
                try 
                {
                    appointmentEntitySessionBeanRemote.deleteAppointment(response);
                    System.out.println("Appointment " +  response + " has been canceled successfully!");
                }
                catch (AppointmentNotFoundException ex)
                {
                    System.out.println("Appointment Number " + response + " cannot be found!");
                }
            } 
            else
            {
                break;
            }
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
