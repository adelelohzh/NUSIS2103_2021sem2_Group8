package easyappointmentsystemwebcustomerclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateful.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import entity.CustomerEntity;
import java.util.Set;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerEmailExistsException;
import util.exception.InputDataValidationException;
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

    private CustomerEntity currentCustomerEntity;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private SystemAdministrationModule systemAdministrationModule;


    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
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
            System.out.println("*** Welcome to Customer terminal ***\n");
            System.out.println("1: Registration");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doRegister();

                } else if (response == 2) {
                    try {
                        CustomerEntity currentCustomerEntity = doLogin();
                        System.out.println("Login successful!\n");
                        systemAdministrationModule = new SystemAdministrationModule(currentCustomerEntity, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote, currentCustomerEntity, queueCheckoutNotification, queueCheckoutNotificationFactory);
                        menuMain(currentCustomerEntity);
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

    private CustomerEntity doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Customer terminal :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentCustomerEntity = customerEntitySessionBeanRemote.customerLogin(username, password);
            return currentCustomerEntity;
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain(CustomerEntity customerEntity) {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Customer terminal :: Main ***\n");
            System.out.print("You are login as ");
            System.out.println(customerEntity.getFirstName() + " " + customerEntity.getLastName());
            System.out.println("1: Search Operation");
            System.out.println("2: Add Appointment");
            System.out.println("3: View Appointment");
            System.out.println("4: Cancel Appointment");
            System.out.println("5: Rate Service Provider");
            System.out.println("6: Logout\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    // search operation
                } else if (response == 2) {
                    // add appt
                } else if (response == 3) {
                    // view appt
                } else if (response == 4) {
                    // cancel appt
                } else if (response == 5) {
                    // rate service provider
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
                break;
            }
        }
    }

    private void doRegister() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Identity Number(NRIC or Passport)> ");
        String identityNumber = scanner.nextLine().trim();

        System.out.print("Enter Password> ");
        String password = scanner.nextLine().trim();

        System.out.print("Enter First Name> ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Enter Gender> ");
        Character gender = scanner.nextLine().trim().charAt(0);

        System.out.print("Enter Age> ");
        Integer age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Phone number> ");
        String phoneNumber = scanner.nextLine().trim();

        System.out.print("Enter Address> ");
        String address = scanner.nextLine().trim();

        System.out.print("Enter City> ");
        String city = scanner.nextLine().trim();

        System.out.print("Enter Email address> ");
        String email = scanner.nextLine().trim();

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setIdentityNumber(identityNumber);
        customerEntity.setPassword(password);
        customerEntity.setFirstName(firstName);
        customerEntity.setLastName(lastName);
        customerEntity.setGender(gender);
        customerEntity.setAge(age);
        customerEntity.setPhoneNumber(phoneNumber);
        customerEntity.setAddress(address);
        customerEntity.setCity(city);
        customerEntity.setEmailAddress(email);
        
        Set<ConstraintViolation<CustomerEntity>>constraintViolations = validator.validate(customerEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                Long newCustomerId = customerEntitySessionBeanRemote.createNewCustomer(customerEntity);
                System.out.println("New customer created successfully!: " + newCustomerId + "\n");
            }
            catch(CustomerEmailExistsException ex)
            {
                System.out.println("An error has occurred while creating the new customer!: The username already exists\n");
            }
            catch(UnknownPersistenceException ex)
            {
                System.out.println("An unknown error has occurred while creating the new customer!: " + ex.getMessage() + "\n");
            }
            catch(InputDataValidationException ex)
            {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else
        {
            showInputDataValidationErrorsForCustomerEntity(constraintViolations);
        }

    }
    
    private void showInputDataValidationErrorsForCustomerEntity(Set<ConstraintViolation<CustomerEntity>>constraintViolations)
    {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
}
