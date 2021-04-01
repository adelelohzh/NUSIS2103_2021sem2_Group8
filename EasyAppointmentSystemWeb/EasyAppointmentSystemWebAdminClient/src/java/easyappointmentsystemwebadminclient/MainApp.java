package easyappointmentsystemwebadminclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import java.util.Scanner;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import util.exception.InvalidLoginCredentialException;

public class MainApp 
{
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private EmailSessionBeanRemote emailSessionBeanRemote;
    
    private Queue queueCheckoutNotification;
    private ConnectionFactory queueCheckoutNotificationFactory;

    public MainApp() 
    {
    }

    public MainApp(AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, EmailSessionBeanRemote emailSessionBeanRemote, Queue queueCheckoutNotification, ConnectionFactory queueCheckoutNotificationFactory) {
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.businessCategoryEntitySessionBeanRemote = businessCategoryEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.emailSessionBeanRemote = emailSessionBeanRemote;
        
        this.queueCheckoutNotification = queueCheckoutNotification;
        this.queueCheckoutNotificationFactory = queueCheckoutNotificationFactory;
    }
    
     
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome to Easy-Appointment (EA) System Web ***\n");
            System.out.println("1: Login");
            System.out.println("2: Logout\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
            {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** EA System Web :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            // currentAdminEntity = adminEntitySessionBeanRemote.adminLogin(username, password);      
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
