package easyappointmentsystemwebadminclient;

import ejb.session.stateless.BusinessCategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
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
import util.exception.BusinessCategoryExistException;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;
import util.thread.RunnableNotification;

public class SystemAdministrationModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote;
    private EmailSessionBeanRemote emailSessionBeanRemote;

    private AdminEntity currentAdminEntity;

    private Queue queueCheckoutNotification;
    private ConnectionFactory queueCheckoutNotificationFactory;

    public SystemAdministrationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SystemAdministrationModule(CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, AdminEntity currentAdminEntity, Queue queueCheckoutNotification, ConnectionFactory queueCheckoutNotificationFactory) {
        this();
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentAdminEntity = currentAdminEntity;
        this.queueCheckoutNotification = queueCheckoutNotification;
        this.queueCheckoutNotificationFactory = queueCheckoutNotificationFactory;
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
                customerEntity.getAppointmentEntities().size();
                //print appointments
                System.out.println("Appointments:");
                
                List<AppointmentEntity> appointmentEntities = customerEntity.getAppointmentEntities();
                
                System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", "Name", "| Business Category", "| Date", "| Time", "| Appointment No.");
                String name = customerEntity.getFullName();
                for (AppointmentEntity appointmentEntity : appointmentEntities) {
                    String businessCategory = appointmentEntity.getBusinessCategoryEntity().getCategory();
                    String scheduledDate = appointmentEntity.getScheduledDate().toString();
                    String scheduledTime = appointmentEntity.getScheduledTime().toString();
                    String appointmentNumber = appointmentEntity.getAppointmentNo();
                    System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", name, "| " + businessCategory, "| " + scheduledDate, "| " + scheduledTime, "| " + appointmentNumber);
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
                    System.out.printf("%-15s%-20s%-13s%-8s%-15s\n", serviceProviderEntity.getName(), "| " + appointmentEntity.getBusinessCategoryEntity(), "| " + appointmentEntity.getScheduledDate(), "| " + appointmentEntity.getScheduledTime(), "| " + appointmentEntity.getAppointmentNo());
                }
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("An error has occurred while retrieving service provider: " + ex.getMessage() + "\n");
            }
        } while (serviceProviderId != 0);
    }

    public void viewServiceProviders() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View service providers ***\n");

        List<ServiceProviderEntity> serviceProviderEntities = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviderEntity();
        System.out.printf("%-15s%-20s%-30s%-15s%-15s%-15s%-20s%-20s%-15s\n", "Name", "| Business category", "| Business Registration Number", "| City", "| Address", "| Email", "| Phone Number", "| Overall Rating", "| Status");

        for (ServiceProviderEntity serviceProviderEntity : serviceProviderEntities) {
            System.out.printf("%-15s%-20s%-30s%-15s%-15s%-15s%-20s%-20s%-15s\n", serviceProviderEntity.getName(), "| " + serviceProviderEntity.getBusinessCategory(), "| " + serviceProviderEntity.getBusinessRegistrationNumber(), "| " + serviceProviderEntity.getCity(), "| " + serviceProviderEntity.getBusinessAddress(), "| " + serviceProviderEntity.getEmailAddress(), "| " + serviceProviderEntity.getPhoneNumber(), "| " + serviceProviderEntity.getRating(), "| " + serviceProviderEntity.getStatusEnum());
        }

        System.out.print("Press any key to continue...> ");
        sc.nextLine();

    }

    public void approveServiceProviders() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Approve service provider ***\n");
        Long serviceProviderId;

        List<ServiceProviderEntity> serviceProviderEntities = serviceProviderEntitySessionBeanRemote.retrieveAllServiceProviderEntity();
        System.out.println("List of service providers with pending approval:");
        System.out.printf("%-3s%-7s%-20s%-20s%-7s%-10s%-8s%-7s\n", "Id", "| Name", "| Business category", "| Business Reg. No.", "| City", "| Address", "| Email", "| Phone");

        for (ServiceProviderEntity serviceProviderEntity : serviceProviderEntities) {
            if (serviceProviderEntity.getStatusEnum() == StatusEnum.Pending) {
                System.out.printf("%-3s%-7s%-20s%-20s%-7s%-10s%-8s%-7s\n", serviceProviderEntity.getServiceProviderId(), "| " + serviceProviderEntity.getName(), "| " + serviceProviderEntity.getBusinessCategory(), "| " + serviceProviderEntity.getBusinessRegistrationNumber(), "| " + serviceProviderEntity.getCity(), "| " + serviceProviderEntity.getBusinessAddress(), "| " + serviceProviderEntity.getEmailAddress(), "| " + serviceProviderEntity.getPhoneNumber());
                System.out.println();
            }
        }

        do {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter service provider Id> ");
            serviceProviderId = sc.nextLong();
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
        Long serviceProviderId;

        do {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter service provider Id> ");
            serviceProviderId = sc.nextLong();
            if (serviceProviderId == 0) {
                break;
            }

            try {
                ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId);
                serviceProviderEntity.setStatusEnum(StatusEnum.Blocked);
                System.out.println(serviceProviderEntity.getName() + " has been blocked.");
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("An error has occurred while retrieving service provider: " + ex.getMessage() + "\n");
            }
        } while (serviceProviderId != 0);
    }

    public void addBusinessCategory() throws BusinessCategoryExistException, UnknownPersistenceException, InputDataValidationException {

        Scanner sc = new Scanner(System.in);
        boolean contains = false;
        String category;

        System.out.println("*** Admin terminal :: Add a Business category ***\n");

        do {
            System.out.print("Enter 0 to go back to the previous menu.");
            System.out.print("Enter a new business category> ");
            category = sc.nextLine().trim();
            if (category.equals(0)) {
                break;
            }
            System.out.println();

            List<BusinessCategoryEntity> businessCategoryEntities = businessCategoryEntitySessionBeanRemote.retrieveAllBusinessCategories();

            for (BusinessCategoryEntity businessCategory : businessCategoryEntities) {

                if (businessCategory.getCategory().equals(category)) {
                    contains = true;
                    throw new BusinessCategoryExistException("Business Category " + category + " already exists!");
                }
            }
            if (!contains) {
                BusinessCategoryEntity newBusinessCategory = new BusinessCategoryEntity();
                newBusinessCategory.setCategory(category);
                businessCategoryEntities.add(newBusinessCategory);
                businessCategoryEntitySessionBeanRemote.createNewBusinessCategoryEntity(newBusinessCategory);
                System.out.println("The business category \"\"" + category + "\" is added.");
            }
        } while (!category.equals(0));
    }

    public void removeBusinessCategory() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Remove a Business category ***\n");
        String category;

        do {
            System.out.print("Enter 0 to go back to the previous menu.");
            System.out.print("Enter a business category to remove> ");
            category = sc.nextLine().trim();
            if (category.equals(0)) {
                break;
            }
            
            try {
                businessCategoryEntitySessionBeanRemote.deleteBusinessCategory(category);
            } catch (BusinessCategoryNotFoundException ex) {
                System.out.println("Business Category " + category + " does not exist!");
            }
            //throw new BusinessCategoryNotFoundException("Business Category " + category + " does not exist!");
            System.out.println();

            // method to be added into SessionBean
            
        } while (!category.equals(0));
    }

    private void sendJMSMessageToQueueCheckoutNotification(Long customerEntityId, String fromEmailAddress, String toEmailAddress) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = queueCheckoutNotificationFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("fromEmailAddress", fromEmailAddress);
            mapMessage.setString("toEmailAddress", toEmailAddress);
            mapMessage.setLong("customerEntityId", customerEntityId);
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

        do {
            System.out.print("Enter 0 to go back to the previous menu.");
            System.out.print("Enter customer Id> ");
            customerId = sc.nextLong();

            try {
                CustomerEntity currentCustomerEntity = customerEntitySessionBeanRemote.retrieveCustomerEntityByCustomerId(customerId);
                List<AppointmentEntity> customerAppointmentEntities = currentCustomerEntity.getAppointmentEntities();

                if (customerAppointmentEntities.isEmpty()) {
                    System.out.println("There are no new appointments to " + currentCustomerEntity.getFullName());
                } else {

                    String toEmailAddress = currentCustomerEntity.getEmailAddress();
                    //List<AppointmentEntity> appointmentEntities = customerEntitySessionBeanRemote.getAppointmentEntities();
                    //if (appointmentEntities.length() == 0) {

                    //}
                    // 01 - Synchronous Session Bean Invocation
                    emailSessionBeanRemote.emailCheckoutNotificationSync(customerAppointmentEntities, "Name <name@comp.nus.edu.sg>", toEmailAddress);
                    // 02 - Asynchronous Session Bean Invocation
                    //Future<Boolean> asyncResult = emailSessionBeanRemote.emailCheckoutNotificationAsync(customerAppointmentEntities, "Name <name@comp.nus.edu.sg>", toEmailAddress);
                    //RunnableNotification runnableNotification = new RunnableNotification(asyncResult);
                    //runnableNotification.start();
                    // 03 - JMS Messaging with Message Driven Bean
                    //sendJMSMessageToQueueCheckoutNotification(customerAppointmentEntities.get(0).getCustomerEntity().getCustomerId(), "Name <name@comp.nus.edu.sg>", toEmailAddress);
                    //System.out.println("Reminder email sent successfully!\n");
                }
            } catch (CustomerNotFoundException ex) {
                System.out.println("An error has occurred while sending the reminder email: " + ex.getMessage() + "\n");
            }

        } while (customerId != 0);

    }

}
