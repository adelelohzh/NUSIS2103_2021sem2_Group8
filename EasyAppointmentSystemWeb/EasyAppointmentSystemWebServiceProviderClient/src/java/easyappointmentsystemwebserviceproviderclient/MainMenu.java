package easyappointmentsystemwebserviceproviderclient;

import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.ServiceProviderEntity;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


public class MainMenu 
{
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    
    private ServiceProviderEntity currentServiceProviderEntity;
    
    public MainMenu()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    public MainMenu(ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote)
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
    }
    
    public void menu()
    {
        Scanner scanner = new Scanner(System.in);
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

                response = scanner.nextInt();

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
        
    }
    
    public void editProfile()
    {
        
    }
    
    public void viewAppointment()
    {
        
    }
    
    public void cancelAppointment()
    {
        
    }
    
}
