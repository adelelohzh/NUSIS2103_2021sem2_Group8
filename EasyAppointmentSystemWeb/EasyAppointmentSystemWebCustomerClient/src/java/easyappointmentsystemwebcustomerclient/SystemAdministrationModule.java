/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemwebcustomerclient;

import ejb.session.stateful.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AppointmentNotFoundException;
import util.exception.AppointmentNumberExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAddAppointmentException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;

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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(currentDate, formatter);

            System.out.printf("%-19s%-6s%-22s%-9s%-16s\n", "Service Provider Id", "| Name", "| First available Time", "| Address", "| Overall rating");

            try {
                List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityBySearch(businessCategory, city);

                for (ServiceProviderEntity s : serviceProviders) {
                    //each service provider, retrieve their appointment entities for a particular date, sorted by time
                    List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveSortedAppointmentsByDate(date, s.getServiceProviderId());
                    if (appointmentEntities.size() == 10) { //full slots
                        continue;
                    } else {
                        List<String> times = Arrays.asList("08:30", "09:30", "10:30", "11.30", "12:30", "13.30", "14:30", "15:30", "16:30", "17.30", "18.30");
                        List<String> timeSlots = new ArrayList<>();
                        timeSlots.addAll(times);

                        String firstAvailableTime = "";
                        int i = 0;
                        for (AppointmentEntity appointment : appointmentEntities) {
                            if (!appointment.getScheduledTime().equals(timeSlots.get(i))) {
                                firstAvailableTime = timeSlots.get(i);
                                break; //found the index
                            }
                            i++;
                        }

                        System.out.printf("%-19s%-6s%-22s%-9s%-16s\n", +s.getServiceProviderId() + " " + s.getName() + " " + firstAvailableTime + " " + s.getBusinessAddress() + " " + s.getRating());
                    }
                }
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("Service Provider cannot be found!");
            }
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Exit> ");
            businessCategory = sc.nextLine().trim();
        } while (!businessCategory.equals("0"));
    }

    public void addAppointment() throws ParseException, UnknownPersistenceException, InputDataValidationException, AppointmentNumberExistsException, InvalidAddAppointmentException {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Add Appointment ***\n");
        List<BusinessCategoryEntity> businessCategoryEntities = businessCategoryEntitySessionBeanRemote.retrieveAllBusinessCategories();
        for (BusinessCategoryEntity businessCategory : businessCategoryEntities) {
            System.out.printf("%-15s", businessCategory.getBusinessCategoryId() + businessCategory.getCategory() + " |");
        }
        System.out.println();
        String response = "";

        do {
            System.out.print("Enter Business category> ");
            String businessCategory = sc.nextLine().trim();

            System.out.print("Enter City> ");
            String city = sc.nextLine().trim();

            System.out.print("Enter Date (YYYY-MM-DD)> ");
            String currentDate = sc.nextLine().trim();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(currentDate, formatter);
            String day = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);

            if (day.equals("Sunday")) {
                throw new InvalidAddAppointmentException("Appointment cannot be made.");
            } else {
                try {
                    List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityBySearch(businessCategory, city);

                    System.out.printf("%-19s%-6s%-22s%-9s%-16s\n", "Service Provider Id", "| Name", "| First available Time", "| Address", "| Overall rating");

                    for (ServiceProviderEntity s : serviceProviders) {
                        List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveSortedAppointmentsByDate(date, s.getServiceProviderId());
                        if (appointmentEntities.size() == 10) {
                            continue;
                        }

                        List<String> times = Arrays.asList("08:30", "09:30", "10:30", "11.30", "12:30", "13.30", "14:30", "15:30", "16:30", "17.30", "18.30");
                        List<String> timeSlots = new ArrayList<>();
                        timeSlots.addAll(times);

                        String firstAvailableTime = "";
                        int i = 0;
                        for (AppointmentEntity appointment : appointmentEntities) {
                            if (!appointment.getScheduledTime().equals(timeSlots.get(i))) {
                                firstAvailableTime = timeSlots.get(i);
                                break; //found the index
                            }
                            i++;
                        }

                        System.out.println(s.getServiceProviderId() + "| " + s.getName() + "| " + firstAvailableTime + "| " + s.getBusinessAddress() + "| " + s.getRating());

                    }

                } catch (ServiceProviderNotFoundException ex) {
                    System.out.println("Service Provider cannot be found!");
                }

                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Service provider Id> ");
                response = sc.nextLine().trim();
                Long serviceProviderId = Long.parseLong(response);

                List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveSortedAppointmentsByDate(date, serviceProviderId);

                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter Time> ");
                response = sc.nextLine().trim();
                LocalTime time = LocalTime.parse(response, DateTimeFormatter.ofPattern("HH:mm"));

                // check whether at least 2 hours before appointment first
                LocalDate todayDate = LocalDate.now();
                LocalDate appointmentDate = date;  //date of appointment to be scheduled

                LocalTime todayTime = LocalTime.now();
                todayTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime appointmentTime = time;

                int comparison = appointmentDate.compareTo(todayDate);
                int compare = appointmentTime.compareTo(todayTime);

                if (comparison == 0) { // same day
                    if (compare < 2) { //cannot
                        System.out.println("Appointment cannot be made!");
                        return;
                    }
                }

                if (comparison < 0) {
                    System.out.println("Appointment cannot be made!");
                    return;
                }

                boolean validTime = true;
                for (AppointmentEntity appointment : appointmentEntities) {
                    LocalTime scheduledTime = appointment.getScheduledTime();
                    if (appointment.getScheduledTime() == time) {
                        System.out.println("Time slot is already full!");
                        validTime = false;
                        break;
                    }
                }

                if (validTime) {
                    try {
                        AppointmentEntity appointmentEntity = new AppointmentEntity();
                        String serviceProviderUIN = String.valueOf(serviceProviderId);
                        String appointmentNumber = serviceProviderUIN + response + currentDate;
                        appointmentEntity.setAppointmentNo(appointmentNumber);
                        appointmentEntity.setScheduledTime(time);
                        appointmentEntity.setScheduledDate(date);
                        appointmentEntitySessionBeanRemote.createNewAppointment(currentCustomerEntity.getCustomerId(), serviceProviderId, appointmentEntity);
                    } catch (CustomerNotFoundException ex) {
                        System.out.println("Customer with customer id " + currentCustomerEntity.getCustomerId() + " not found");
                    } catch (ServiceProviderNotFoundException ex) {
                        System.out.println("Service with service provider id " + serviceProviderId + " not found");
                    }
                }
                // if timeslot exists, confirm appointment
                // System.out.println("The appointment with " + serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId).getName() + " at " + time + " on " + currentDate + " is confirmed.");
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Exit>");
                response = sc.nextLine().trim();

            }

        } while (!response.equals(0));
    }

    public void viewAppointments() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: View Appointments ***\n");
        String response = "";

        List<AppointmentEntity> appointments = currentCustomerEntity.getAppointmentEntities();

        System.out.println("*** Customer terminal :: View Appointments ***\n");
        System.out.print("Appointments: ");
        System.out.printf("%-15s%-13s%-8s%-15s\n", "Name", "| Date", "| Time", "| Appointment No.");

        for (AppointmentEntity appointment : appointments) {
            System.out.printf("%-15s%-13s%-8s%-15s\n", currentCustomerEntity.getFullName(), appointment.getScheduledDate(), appointment.getScheduledTime(), appointment.getAppointmentNo());
        }

        while (response != "0") {
            System.out.println("Enter 0 to go back to the previous menu.");
            response = sc.nextLine().trim();
        }
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

                LocalDate todayDate = LocalDate.now();
                LocalDate appointmentDate = appointmentEntity.getScheduledDate();

                LocalTime todayTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                todayTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime appointmentTime = appointmentEntity.getScheduledTime();

                int comparison = appointmentDate.compareTo(todayDate);
                int compare = appointmentTime.compareTo(todayTime);
                if (comparison > 1) { //appointmentDate is more than one day from now
                    appointmentEntitySessionBeanRemote.deleteAppointment(appointmentNo);
                } else if (comparison == 1) {
                    if (compare >= 0) {
                        appointmentEntitySessionBeanRemote.deleteAppointment(appointmentNo);
                    }
                } else {
                    System.out.println("Appointment cannot be deleted!");
                }

                response = sc.nextLine().trim();

            } catch (AppointmentNotFoundException ex) {
                System.out.println("Appointment with id: " + appointmentNo + " does not exist!");
            }

        } while (!response.equals(0));
    }

    public void rateServiceProvider() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Rate Service Provider ***\n");
        String response = "";

        System.out.print("Enter Service Provider Name> ");
        String name = sc.nextLine().trim();

        try {
            ServiceProviderEntity serviceProvider = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityByName(name);
            System.out.println("You are rating " + name + ".\n");
            System.out.print("Enter rating> ");
            Long rating = sc.nextLong();
            if (rating > 5.0 | rating < 0.0) {
                System.out.println("Please enter a number between 0.0 to 5.0!");
            } else {
                serviceProviderEntitySessionBeanRemote.updateRating(rating, serviceProvider.getServiceProviderId());
                System.out.println("Rating successfully submitted!");
            }
        } catch (ServiceProviderNotFoundException ex) {
            System.out.println("Service Provider does not exist!");
        } catch (InputMismatchException ex) {
            System.out.println("Please enter a number between 0.0 to 5.0!");
        }

    }

}
