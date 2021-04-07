/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemwebcustomerclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AppointmentNotFoundException;
import util.exception.ServiceProviderNotFoundException;

/**
 *
 * @author adele
 */
public class SystemAdministrationModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private CustomerEntity currentCustomerEntity;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote;
    private EmailSessionBeanRemote emailSessionBeanRemote;
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;

    private AdminEntity currentAdminEntity;

    private Queue queueCheckoutNotification;
    private ConnectionFactory queueCheckoutNotificationFactory;

    public SystemAdministrationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SystemAdministrationModule(CustomerEntity customerEntity, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, CustomerEntity currentCustomerEntity, Queue queueCheckoutNotification, ConnectionFactory queueCheckoutNotificationFactory) {
        this();
        this.currentCustomerEntity = customerEntity;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentAdminEntity = currentAdminEntity;
        this.queueCheckoutNotification = queueCheckoutNotification;
        this.queueCheckoutNotificationFactory = queueCheckoutNotificationFactory;
    }

    public void searchOperation() throws ParseException {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Search Operation ***\n");
        String businessCategory;
        
        do {
            System.out.print("Enter Business category> ");
            businessCategory = sc.nextLine().trim();

            System.out.print("Enter City> ");
            String city = sc.nextLine().trim();

            System.out.print("Enter Date (YYYY-MM-DD)> ");
            String currentDate = sc.nextLine().trim();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(currentDate);

            try {
                List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityBySearch(businessCategory, city);
                //list of service providers in the category and city
                
                for (ServiceProviderEntity s : serviceProviders) {
                    //each service provider, retrieve their appointment entities for a particular date, sorted by time
                    List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveSortedAppointmentsByDate(date, s.getServiceProviderId());
                    if (appointmentEntities.size() == 10) { //full slots
                        continue;
                    }
                    
                    System.out.printf("%-19s%-6s%-22s%-9s%-16s\n", "Service Provider Id", "| Name", "| First available Time", "| Address", "| Overall rating");
                    //find earliest time: I was thinking just have an arraylist of timeslots 0, 1, 2, ...
                    //then we simply find which is the first index that is empty
                    //then add 8.5 hours to the index and we get the timing
                    //System.out.printf("%-19s%-6s%-22s%-9s%-16s\n", s.getServiceProviderId(), "| " + s.getName(), "| " + earliestTime, "| " + s.getBusinessAddress(), "| " + s.getRating());
                }
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("Service Provider cannot be found!");
            }
            System.out.println("Enter 0 to go back to the previous menu.");
            
        } while (!businessCategory.equals("0"));
    }

    public void addAppointment() throws ParseException {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Add Appointment ***\n");
        String response;
        
        do {
            System.out.print("Enter Business category> ");
            String businessCategory = sc.nextLine().trim();

            System.out.print("Enter City> ");
            String city = sc.nextLine().trim();

            System.out.print("Enter Date (YYYY-MM-DD)> ");
            String currentDate = sc.nextLine().trim();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(currentDate);
            
            try {
                List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityBySearch(businessCategory, city);

                System.out.printf("%-19s%-6s%-22s%-9s%-16s\n", "Service Provider Id", "| Name", "| First available Time", "| Address", "| Overall rating");
                
                for (ServiceProviderEntity s : serviceProviders) {
                    List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveSortedAppointmentsByDate(date, s.getServiceProviderId());
                    if (appointmentEntities.size() == 10) { //full slots
                        continue;
                    }
                    //as above (search), print out first available timing
                }
                
            }
            catch (ServiceProviderNotFoundException ex) {
                System.out.println("Service Provider cannot be found!");
            }
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Service provider Id> ");
            response = sc.nextLine().trim();
            Long serviceProviderId = Long.parseLong(response);

            List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveSortedAppointmentsByDate(date, serviceProviderId);
            //get the available appointment slots
            
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter Time> ");
            response = sc.nextLine().trim();
            // Time time = 
            // if timeslot exists, confirm appointment
            // System.out.println("The appointment with " + serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId).getName() + " at " + time + " on " + currentDate + " is confirmed.");
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Exit>");
            response = sc.nextLine().trim();
            
        } while (!response.equals(0));
    }
    
    public void cancelAppointment() {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Cancel Appointment ***\n");
        String response;
        
        do {
            List<AppointmentEntity> appointmentEntities = currentCustomerEntity.getAppointmentEntities();
            
            System.out.print("Enter Appointment Id to cancel>");
            response = sc.nextLine().trim();
            String appointmentNo = response;
            
            try {
                AppointmentEntity appointmentEntity = appointmentEntitySessionBeanRemote.retrieveAppointmentByAppointmentNumber(appointmentNo);
                // do cancel
            }
            catch (AppointmentNotFoundException ex) {
                System.out.println("Appointment with id: " + appointmentNo + " does not exist!");
            }
            
        } while (!response.equals(0));
    }
    
    
}
