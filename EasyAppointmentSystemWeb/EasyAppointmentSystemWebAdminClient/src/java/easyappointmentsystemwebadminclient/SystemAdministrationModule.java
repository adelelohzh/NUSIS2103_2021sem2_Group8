package easyappointmentsystemwebadminclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.StatusEnum;
import util.exception.AppointmentNotFoundException;
import util.exception.BusinessCategoryExistException;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteBusinessCategoryException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderBlockedException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;
import util.thread.RunnableNotification;

public class SystemAdministrationModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private EmailSessionBeanRemote emailSessionBeanRemote;

    private AdminEntity currentAdminEntity;

    private Queue queueCheckoutNotification;
    private ConnectionFactory queueCheckoutNotificationFactory;

    public SystemAdministrationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SystemAdministrationModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, AdminEntity currentAdminEntity, EmailSessionBeanRemote emailSessionBeanRemote, Queue queueCheckoutNotification, ConnectionFactory queueCheckoutNotificationFactory) {
        this();
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.businessCategoryEntitySessionBeanRemote = businessCategoryEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentAdminEntity = currentAdminEntity;
        this.queueCheckoutNotification = queueCheckoutNotification;
        this.queueCheckoutNotificationFactory = queueCheckoutNotificationFactory;
        this.emailSessionBeanRemote = emailSessionBeanRemote;
    }

    public void viewCustomerAppointments() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for customers ***\n");

        String response = "";
        System.out.print("Enter customer Id> ");
        response = sc.nextLine().trim();
        System.out.println();
        
        do {
            try {
                Long customerId = Long.parseLong(response);
                CustomerEntity customerEntity = customerEntitySessionBeanRemote.retrieveCustomerEntityByCustomerId(customerId);
                //retrieve appointments

                List<AppointmentEntity> appointmentEntities = customerEntity.getAppointmentEntities();
                //print appointments

                if (appointmentEntities.size() != 0) {
                    System.out.println("Appointments: \n");
                    System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", "Name", "| Business Category", "| Date", "| Time", "| Appointment No.\n");
                    String name = customerEntity.getFullName();
                    for (AppointmentEntity appointmentEntity : appointmentEntities) {
                        if (appointmentEntity.getIsCancelled().equals(Boolean.FALSE))
                        {
                            String businessCategory = appointmentEntity.getBusinessCategoryEntity().getCategory();
                            String scheduledDate = appointmentEntity.getScheduledDate().toString();
                            String scheduledTime = appointmentEntity.getScheduledTime().toString();
                            String appointmentNumber = appointmentEntity.getAppointmentNo();
                            System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", name, "| " + businessCategory, "| " + scheduledDate, "| " + scheduledTime, "| " + appointmentNumber);
                        }
                    }
                    System.out.println();
                } else {
                    throw new AppointmentNotFoundException("No appointments found for customer with customer id " + customerEntity.getCustomerId() + "\n");
                }
            } catch (CustomerNotFoundException ex) {
                System.out.println("An error has occurred while retrieving customer: " + ex.getMessage() + "\n");
            } catch (AppointmentNotFoundException ex) {
                System.out.println("No appointments found for customer: " + ex.getMessage() + "\n");
            } catch (InputMismatchException | NumberFormatException ex) {
                System.out.println("Please input a valid Customer ID!");
            }
            
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter customer Id> ");
            response = sc.nextLine().trim();
        } while (!response.equals("0"));
    }

    public void viewServiceProviderAppointments() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for service providers ***\n");
        String response = "";
        
        System.out.print("Enter service provider Id> ");
        response = sc.nextLine().trim();
        System.out.println();

        do {
            try {
                Long serviceProviderId = Long.parseLong(response);
                ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
                List<AppointmentEntity> appointmentEntities = serviceProviderEntity.getAppointmentEntities();
                
                if (appointmentEntities.size() != 0) {
                    System.out.println("Appointments: \n");
                    System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", "Name", "| Business Category", "| Date", "| Time", "| Appointment No.\n");
                    
                    String name = serviceProviderEntity.getName();
                    for (AppointmentEntity appointmentEntity : appointmentEntities) {
                        if (appointmentEntity.getIsCancelled().equals(Boolean.FALSE))
                        {
                            String businessCategory = appointmentEntity.getBusinessCategoryEntity().getCategory();
                            String scheduledDate = appointmentEntity.getScheduledDate().toString();
                            String scheduledTime = appointmentEntity.getScheduledTime().toString();
                            String appointmentNumber = appointmentEntity.getAppointmentNo();
                            System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", name, "| " + businessCategory, "| " + scheduledDate, "| " + scheduledTime, "| " + appointmentNumber);
                        }
                    }
                } else {
                    throw new AppointmentNotFoundException("No appointments found for Service Provider with service provider id " + serviceProviderEntity.getServiceProviderId() + "\n");
                }
                    System.out.println();
            } catch (ServiceProviderNotFoundException | ServiceProviderBlockedException ex) {
                System.out.println("An error has occurred while retrieving service provider: " + ex.getMessage() + "\n");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid Service Provider Id!");
            } catch (AppointmentNotFoundException ex) {
                System.out.println("No appointments found for Service Provider: " + ex.getMessage() + "\n");
            }
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter service provider Id> ");
            response = sc.nextLine().trim();
            System.out.println();
        } while (!response.equals("0"));
    }

    public void viewServiceProviders() throws ServiceProviderNotFoundException {

        Scanner sc = new Scanner(System.in);
        String response = "";
        System.out.println("*** Admin terminal :: View service providers ***\n");

        List<ServiceProviderEntity> serviceProviderEntities = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviderEntity();
        
        if (serviceProviderEntities.size() != 0) 
        {
            System.out.printf("%-3s%-25s%-20s%-30s%-15s%-30s%-20s%-20s%-20s%-15s\n", "Id", "| Name", "| Business category", "| Business Registration Number", "| City", "| Address", "| Email", "| Phone Number", "| Overall Rating", "| Status\n");
            for (ServiceProviderEntity serviceProviderEntity : serviceProviderEntities) {
                System.out.printf("%-3s%-25s%-20s%-30s%-15s%-30s%-20s%-20s%-20s%-15s\n", serviceProviderEntity.getServiceProviderId(), "| " +serviceProviderEntity.getName(), "| " + serviceProviderEntity.getBusinessCategory(), "| " + serviceProviderEntity.getBusinessRegistrationNumber(), "| " + serviceProviderEntity.getCity(), "| " + serviceProviderEntity.getBusinessAddress(), "| " + serviceProviderEntity.getEmailAddress(), "| " + serviceProviderEntity.getPhoneNumber(), "| " + serviceProviderEntity.getRating(), "| " + serviceProviderEntity.getStatusEnum());
            }
        } else {
            throw new ServiceProviderNotFoundException("There are currently no service providers.\n");
        }
        
        
        while (!response.equals("0"))
        {
            System.out.println();
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("> ");
            response = sc.nextLine();
            System.out.println();
        }

    }

    public void approveServiceProviders() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Approve service provider ***\n");
        String response = "";

        List<ServiceProviderEntity> serviceProviderEntities = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviderEntity();
        System.out.println("List of service providers with pending approval:\n");
        System.out.printf("%-3s%-7s%-20s%-20s%-7s%-10s%-8s%-7s\n", "Id", "| Name", "| Business category", "| Business Reg. No.", "| City", "| Address", "| Email", "| Phone");

        for (ServiceProviderEntity serviceProviderEntity : serviceProviderEntities) {
            if (serviceProviderEntity.getStatusEnum() == StatusEnum.Pending) {
                System.out.printf("%-3s%-7s%-20s%-20s%-7s%-10s%-8s%-7s\n", serviceProviderEntity.getServiceProviderId(), "| " + serviceProviderEntity.getName(), "| " + serviceProviderEntity.getBusinessCategory(), "| " + serviceProviderEntity.getBusinessRegistrationNumber(), "| " + serviceProviderEntity.getCity(), "| " + serviceProviderEntity.getBusinessAddress(), "| " + serviceProviderEntity.getEmailAddress(), "| " + serviceProviderEntity.getPhoneNumber());
            }
        }
        
        System.out.println();
        
        do {
            try {
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter service provider Id> ");
                response = sc.nextLine().trim();

                if (!response.equals("0")) {

                    System.out.println();

                    try {
                        Long serviceProviderId = Long.parseLong(response);
                        ServiceProviderEntity serviceProvider = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
                        if (serviceProvider.getStatusEnum() == StatusEnum.Approved) {
                            System.out.println("Service Provider is already approved!\n");
                        } else if (serviceProvider.getStatusEnum() == StatusEnum.Blocked) {
                            System.out.println("Service Provider is blocked and cannot be approved!\n");
                        } else {
                            serviceProviderEntitySessionBeanRemote.approveServiceProvider(serviceProviderId);
                            System.out.println(serviceProvider.getName() + "'s registration is approved.\n");
                        }
                    } catch (ServiceProviderNotFoundException | ServiceProviderBlockedException ex) {
                        System.out.println("An error has occurred while retrieving service provider: " + ex.getMessage() + "\n");
                    }

                }
            } catch (InputMismatchException | NumberFormatException ex) {
                System.out.println("Please enter a valid Service Provider Id!");
            }
        } while (!response.equals("0"));
        System.out.println();
    }

    public void blockServiceProviders() {

        Scanner sc = new Scanner(System.in);
        String response = "";

        System.out.println("*** Admin terminal :: Block service provider ***\n");
        System.out.println("List of service providers:\n");
        List<ServiceProviderEntity> serviceProviderEntities = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviderEntity();
        System.out.printf("%-3s%-15s%-20s%-30s%-15s%-15s%-15s%-20s%-20s%-15s\n", "Id", "| Name", "| Business category", "| Business Registration Number", "| City", "| Address", "| Email", "| Phone Number", "| Overall Rating", "| Status\n");
        for (ServiceProviderEntity serviceProviderEntity : serviceProviderEntities) {
            System.out.printf("%-3s%-15s%-20s%-30s%-15s%-15s%-15s%-20s%-20s%-15s\n", serviceProviderEntity.getServiceProviderId(), "| " + serviceProviderEntity.getName(), "| " + serviceProviderEntity.getBusinessCategory(), "| " + serviceProviderEntity.getBusinessRegistrationNumber(), "| " + serviceProviderEntity.getCity(), "| " + serviceProviderEntity.getBusinessAddress(), "| " + serviceProviderEntity.getEmailAddress(), "| " + serviceProviderEntity.getPhoneNumber(), "| " + serviceProviderEntity.getRating(), "| " + serviceProviderEntity.getStatusEnum());
        }

        System.out.println();

        do {
            try {
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter service provider Id> ");
                response = sc.nextLine().trim();

                if (!response.equals("0")) {

                    System.out.println();

                    try {
                        Long serviceProviderId = Long.parseLong(response);
                        ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
                        if (serviceProviderEntity.getStatusEnum() == StatusEnum.Blocked) {
                            System.out.println("Service Provider is already blocked!\n");
                        } else {
                            serviceProviderEntitySessionBeanRemote.blockServiceProvider(serviceProviderId);
                            System.out.println(serviceProviderEntity.getName() + " has been blocked.\n");
                            List<AppointmentEntity> apptList = serviceProviderEntity.getAppointmentEntities();
                            for (AppointmentEntity a : apptList) {
                                String apptNo = a.getAppointmentNo();
                                appointmentEntitySessionBeanRemote.cancelAppointment(apptNo);
                            }
                        }
                    } catch (ServiceProviderNotFoundException | ServiceProviderBlockedException | AppointmentNotFoundException ex) {
                        System.out.println("An error has occurred while retrieving service provider: " + ex.getMessage() + "\n");
                    }
                }
            } catch (InputMismatchException | NumberFormatException ex) {
                System.out.println("Please enter a valid Service Provider Id!");
            }
        } while ((!response.equals("0")));
        System.out.println();
    }

    public void addBusinessCategory() {

        Scanner sc = new Scanner(System.in);
        boolean contains = false;
        String category;

        System.out.println("*** Admin terminal :: Add a Business category ***\n");
        
        System.out.println("Enter 0 to go back to the previous menu.");
        System.out.print("Enter a new business category> ");
        category = sc.nextLine().trim();
        System.out.println();

        while (!category.equals("0")) {
            try 
            {
                BusinessCategoryEntity businessCategory = businessCategoryEntitySessionBeanRemote.retrieveBusinessCategoriesByName(category);
                System.out.println("Business Category already exists!\n");
            }
            catch (BusinessCategoryNotFoundException ex)
            {
                BusinessCategoryEntity newBusinessCategory = new BusinessCategoryEntity();
                newBusinessCategory.setCategory(category);
                try {
                    businessCategoryEntitySessionBeanRemote.createNewBusinessCategoryEntity(newBusinessCategory);
                } catch (UnknownPersistenceException | InputDataValidationException exception) {
                    System.out.println("Business Category is not created!\n");
                } catch (BusinessCategoryExistException exception) {
                    System.out.println("Business Category already exists!\n");
                }
                System.out.println("The business category \"" + category + "\" is added.\n");
            }
                
           
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter a new business category> ");
            category = sc.nextLine().trim();
        } 
        System.out.println();
    }

    public void removeBusinessCategory() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Remove a Business category ***\n");
        String category;
        List<BusinessCategoryEntity> businessCategoryEntities = businessCategoryEntitySessionBeanRemote.retrieveAllBusinessCategories();
        for (BusinessCategoryEntity businessCategory : businessCategoryEntities) {
            System.out.printf("%-3s%-10s", businessCategory.getBusinessCategoryId(), businessCategory.getCategory() + " |");
        }
        System.out.println("\n");
        
        System.out.println("Enter 0 to go back to the previous menu.");
        System.out.print("Enter a business category to remove> ");
        category = sc.nextLine().trim();
        
        while (!category.equals("0")) {
            
            System.out.println();
            
            try {
                businessCategoryEntitySessionBeanRemote.deleteBusinessCategory(category);
                System.out.println("Business Category " + category + " is successfully deleted!\n");
            } catch (BusinessCategoryNotFoundException ex) {
                System.out.println("Business Category " + category + " does not exist! Please enter a valid Business Category name!\n");
            } catch (DeleteBusinessCategoryException ex) { 
                System.out.println("Business Category " + category + " cannot be deleted!\n");
            }
            
            //throw new BusinessCategoryNotFoundException("Business Category " + category + " does not exist!");
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter a business category to remove> ");
            category = sc.nextLine().trim();
            // method to be added into SessionBean
        } 
        System.out.println();
    }

    private void sendJMSMessageToQueueCheckoutNotification(Long appointmentEntityId, String fromEmailAddress, String toEmailAddress) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = queueCheckoutNotificationFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("fromEmailAddress", fromEmailAddress);
            mapMessage.setString("toEmailAddress", toEmailAddress);
            mapMessage.setLong("appointmentEntityId", appointmentEntityId);
            MessageProducer messageProducer = session.createProducer(queueCheckoutNotification);
            messageProducer.send(mapMessage);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            }

            if (connection != null) {
                connection.close();
            }
        }
    }

    public void sendReminderEmail() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Send reminder email ***\n");
        Long customerId;
                    
        System.out.println("Enter 0 to go back to the previous menu.");
        System.out.print("Enter customer Id> ");
        customerId = sc.nextLong();

        while (customerId.equals(0l)) {
            
            System.out.println();
            try {
                CustomerEntity currentCustomerEntity = customerEntitySessionBeanRemote.retrieveCustomerEntityByCustomerId(customerId);
                List<AppointmentEntity> customerAppointmentEntities = currentCustomerEntity.getAppointmentEntities();
                //the above list will retrieve all the previous and upcoming

                LocalDate todayDate = LocalDate.now();

                String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime currTime = LocalTime.parse(time);


                if (customerAppointmentEntities.isEmpty()) {
                    throw new AppointmentNotFoundException("There are no new appointments to " + currentCustomerEntity.getFullName() + "\n");
                } else {

                    String toEmailAddress = currentCustomerEntity.getEmailAddress();
                    //get the upcoming one
                    
                    int index = 0;
                    for (AppointmentEntity appointmentEntity : customerAppointmentEntities) {
                        LocalDate appointmentDate = appointmentEntity.getScheduledDate();
                        if (appointmentDate.compareTo(todayDate) < 0) { //previous appointment
                            index++;
                            continue;
                        } else if (appointmentDate.compareTo(todayDate) == 0) {
                            LocalTime appointmentTime = appointmentEntity.getScheduledTime();
                            if (appointmentTime.compareTo(currTime) <= 0) {
                                index++;
                                continue;
                            } else {
                                break;
                            }
                        }
                        break;
                    }
                    AppointmentEntity currentAppointment = customerAppointmentEntities.get(index);
                    System.out.println("Current Appointment Entity is " + currentAppointment + "\n");
                    //}
                    // 01 - Synchronous Session Bean Invocation
                    String sendTo = "valencia.teh00@gmail.com";
                    try {
                        emailSessionBeanRemote.emailCheckoutNotificationSync(currentAppointment, "Valencia Teh<vtjw1000@gmail.com>", sendTo); //testing with my email first
                    
                    
                    // 02 - Asynchronous Session Bean Invocation
                    //Future<Boolean> asyncResult = emailSessionBeanRemote.emailCheckoutNotificationAsync(customerAppointmentEntities, "Name <name@comp.nus.edu.sg>", toEmailAddress);
                    //RunnableNotification runnableNotification = new RunnableNotification(asyncResult);
                    //runnableNotification.start();
                    // 03 - JMS Messaging with Message Driven Bean
                        //sendJMSMessageToQueueCheckoutNotification(currentAppointment.getAppointmentId(), "Valencia Teh<vtjw1000@gmail.com>", toEmailAddress);
                        System.out.println("An email is sent to " + currentCustomerEntity.getFullName() + " for the appointment " + currentAppointment.getAppointmentNo() + ".\n");
                    
                    }
                    catch (Exception ex) {
                        System.out.println("An error has occurred while sending the checkout notification email: " + ex.getMessage() + "\n");
                    }
                }
            } catch (CustomerNotFoundException ex) {
                System.out.println("An error has occurred while sending the reminder email: " + ex.getMessage() + "\n");
            } catch (AppointmentNotFoundException ex) {
                System.out.println("There are no appointments to send a reminder email for!\n");
            }
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter customer Id> ");
            customerId = sc.nextLong();

        } 
        System.out.println();

    }

}
