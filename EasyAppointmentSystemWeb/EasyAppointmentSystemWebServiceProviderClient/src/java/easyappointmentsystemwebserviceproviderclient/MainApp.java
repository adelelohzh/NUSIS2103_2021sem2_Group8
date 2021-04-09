package easyappointmentsystemwebserviceproviderclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateful.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.BusinessCategoryEntity;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import entity.ServiceProviderEntity;
import java.util.InputMismatchException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.enumeration.StatusEnum;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderEmailExistException;
import util.exception.UnknownPersistenceException;

public class MainApp {

    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private EmailSessionBeanRemote emailSessionBeanRemote;

    private Queue queueCheckoutNotification;
    private ConnectionFactory queueCheckoutNotificationFactory;

    private ServiceProviderEntity currentServiceProvider;

    public MainApp() {
    }

    public MainApp(AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, EmailSessionBeanRemote emailSessionBeanRemote, Queue queueCheckoutNotification, ConnectionFactory queueCheckoutNotificationFactory) {
        this();
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.businessCategoryEntitySessionBeanRemote = businessCategoryEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.emailSessionBeanRemote = emailSessionBeanRemote;

        this.queueCheckoutNotification = queueCheckoutNotification;
        this.queueCheckoutNotificationFactory = queueCheckoutNotificationFactory;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** Welcome to Service Provider terminal ***\n");
            System.out.println("1: Registration");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    registerServiceProvider();
                } else if (response == 2) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        MainMenu mainMenu = new MainMenu(serviceProviderEntitySessionBeanRemote, currentServiceProvider, appointmentEntitySessionBeanRemote);
                        mainMenu.menu();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner sc = new Scanner(System.in);
        String email = "";
        String password = "";

        System.out.println("*** Service Provider terminal :: Login ***\n");
        System.out.print("Enter Email Address> ");
        email = sc.nextLine().trim();
        System.out.print("Enter Password> ");
        password = sc.nextLine().trim();

        if (email.length() > 0 && password.length() > 0) {
            currentServiceProvider = serviceProviderEntitySessionBeanRemote.doServiceProviderLogin(email, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void registerServiceProvider() {
        Scanner sc = new Scanner(System.in);

        ServiceProviderEntity newServiceProviderEntity = new ServiceProviderEntity();

        System.out.println("*** Service Provider terminal :: Registration Operation ***\n");

        String input = "";
        
        try // is this how to catch?
        {
            System.out.print("Enter Name> ");
            String name = sc.nextLine().trim();

            System.out.println();

            //System.out.print("1 Health | 2 Fashion | 3 Education\n");
            List<BusinessCategoryEntity> businessCategoryEntities = businessCategoryEntitySessionBeanRemote.retrieveAllBusinessCategories();
            
            int i = 0;
            int max = businessCategoryEntities.size();
            for (BusinessCategoryEntity businessCategoryEntity: businessCategoryEntities) {
                i++;
                if (i < max) {
                    System.out.print(businessCategoryEntity.getBusinessCategoryId() + " " + businessCategoryEntity.getCategory() + " | ");
                } else {
                    System.out.print(businessCategoryEntity.getBusinessCategoryId() + " " + businessCategoryEntity.getCategory() + "\n");
                }
            }
            
            System.out.print("Enter Business Category> ");
            int number = sc.nextInt();
            
            sc.nextLine();
            
            if (number < 1 || number > max) {
                System.out.println("Invalid option!");
            } else {
                System.out.print("Enter Business Registration Number> ");
                String businessRegNumber = sc.nextLine().trim();
                System.out.print("Enter City> ");
                String city = sc.nextLine().trim();
                System.out.print("Enter Phone> ");
                String phone = sc.nextLine().trim();
                System.out.print("Enter Business Address> ");
                String address = sc.nextLine().trim();
                System.out.print("Enter Email> ");
                String email = sc.nextLine().trim();
                System.out.print("Enter Password> ");
                String password = sc.nextLine().trim();
                System.out.println();

                ServiceProviderEntity serviceProviderEntity = new ServiceProviderEntity();
                serviceProviderEntity.setName(name);
                serviceProviderEntity.setBusinessRegistrationNumber(businessRegNumber);
                serviceProviderEntity.setBusinessAddress(address);
                serviceProviderEntity.setCity(city);
                serviceProviderEntity.setPhoneNumber(phone);
                serviceProviderEntity.setEmailAddress(email);
                serviceProviderEntity.setPassword(password);
                serviceProviderEntity.setStatusEnum(StatusEnum.Pending);
                
                BusinessCategoryEntity businessCategoryEntity = businessCategoryEntitySessionBeanRemote.retrieveAllBusinessCategories().get(number-1);
                serviceProviderEntity.setBusinessCategory(businessCategoryEntity.getCategory());
           
                
                Long serviceProviderId = serviceProviderEntitySessionBeanRemote.createNewServiceProvider(serviceProviderEntity);
                System.out.println("You have been registered successfully!\n");

                System.out.println("Enter 0 to go back to the previous menu.\n");
                System.out.print(">");
                
                input = sc.nextLine().trim();
            }
        } catch (InputMismatchException | ServiceProviderEmailExistException | UnknownPersistenceException | InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } while (!input.equals("0"));
    }
}
