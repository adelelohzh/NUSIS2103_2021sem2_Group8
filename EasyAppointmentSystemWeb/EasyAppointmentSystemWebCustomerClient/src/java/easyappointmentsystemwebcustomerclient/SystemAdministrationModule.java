/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemwebcustomerclient;

import ejb.session.stateful.BookingSessionBeanRemote;
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
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import util.exception.BusinessCategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAddAppointmentException;
import util.exception.ServiceProviderBlockedException;
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
    private BookingSessionBeanRemote bookingSessionBeanRemote;

    private AdminEntity currentAdminEntity;

    private Queue queueCheckoutNotification;
    private ConnectionFactory queueCheckoutNotificationFactory;

    public SystemAdministrationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SystemAdministrationModule(CustomerEntity customerEntity, AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, BookingSessionBeanRemote bookingSessionBeanRemote, CustomerEntity currentCustomerEntity, Queue queueCheckoutNotification, ConnectionFactory queueCheckoutNotificationFactory) {
        this();
        this.currentCustomerEntity = customerEntity;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.businessCategoryEntitySessionBeanRemote = businessCategoryEntitySessionBeanRemote;
        this.bookingSessionBeanRemote = bookingSessionBeanRemote;
        
        this.currentAdminEntity = currentAdminEntity;
        
        this.queueCheckoutNotification = queueCheckoutNotification;
        this.queueCheckoutNotificationFactory = queueCheckoutNotificationFactory;
    }

    public void searchOperation() throws ParseException {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Search Operation ***\n");
        String response = "";
        String firstAvailableTime = "";

        List<BusinessCategoryEntity> businessCategoryEntities = businessCategoryEntitySessionBeanRemote.retrieveAllBusinessCategories();
        for (BusinessCategoryEntity businessCategory : businessCategoryEntities) {
            System.out.printf("%-3s%-10s", businessCategory.getBusinessCategoryId(), businessCategory.getCategory() + " |");
        }

        do {
            try {
                System.out.print("Enter Business category> ");
                String input = sc.nextLine();
                Long businessCategoryId = Long.parseLong(input);
                System.out.print("Enter City> ");
                String city = sc.nextLine().trim();

                System.out.print("Enter Date (YYYY-MM-DD)> ");
                String currentDate = sc.nextLine().trim();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(currentDate, formatter);

                System.out.printf("%-19s%-6s%-22s%-9s%-16s\n", "Service Provider Id", "| Name", "| First available Time", "| Address", "| Overall rating");

                BusinessCategoryEntity businessCategory = businessCategoryEntitySessionBeanRemote.retrieveBusinessCategoriesById(businessCategoryId);

                List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityBySearch(businessCategory.getCategory(), city);

                for (ServiceProviderEntity s : serviceProviders) {
                    //each service provider, retrieve their appointment entities for a particular date, sorted by time
                    List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveSortedAppointmentsByDate(date, s.getServiceProviderId());
                    if (appointmentEntities.size() == 11) { //full slots
                        continue;
                    } else {
                        if (appointmentEntities.size() == 0) {
                            firstAvailableTime = "08:30";

                        } else {
                            List<String> times = Arrays.asList("08:30", "09:30", "10:30", "11.30", "12:30", "13.30", "14:30", "15:30", "16:30", "17.30", "18.30");
                            List<String> timeSlots = new ArrayList<>();
                            timeSlots.addAll(times);

                            int i = 0;
                            for (AppointmentEntity appointment : appointmentEntities) {
                                
                                while (appointment.getScheduledTime().toString().equals(timeSlots.get(i)) | (appointment.getIsCancelled().equals(Boolean.TRUE) && !appointment.getScheduledTime().toString().equals(timeSlots.get(i)))) {
                                    i++;
                                //    break; //found the index
                                }
                                firstAvailableTime = timeSlots.get(i);
                            //    i++;
                            }
                        }

                        System.out.println(s.getServiceProviderId() + "| " + s.getName() + "| " + firstAvailableTime + "| " + s.getBusinessAddress() + "| " + s.getRating());
                    }

                }
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("Service Provider cannot be found!\n");
            } catch (BusinessCategoryNotFoundException ex) {
                System.out.println("Business Cateogry cannot be found!\n");
            } catch (DateTimeParseException ex) {
                System.out.println("Please input a valid date in YYYY-MM-DD.\n");
            } catch (InputMismatchException | NumberFormatException ex) {
                System.out.println("Please input a valid Business Category Id!\n");
            }

            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Exit> ");
            response = sc.nextLine().trim();
        } while (!response.equals("0"));
    }

    public void addAppointment() throws UnknownPersistenceException, InputDataValidationException, AppointmentNumberExistsException, ServiceProviderNotFoundException {

        Scanner sc = new Scanner(System.in);
        List<String> times = Arrays.asList("08:30", "09:30", "10:30", "11.30", "12:30", "13.30", "14:30", "15:30", "16:30", "17:30", "18:30");
        System.out.println("*** Customer terminal :: Add Appointment ***\n");
        List<BusinessCategoryEntity> businessCategoryEntities = businessCategoryEntitySessionBeanRemote.retrieveAllBusinessCategories();
        for (BusinessCategoryEntity businessCategory : businessCategoryEntities) {
            System.out.printf("%-3s%-10s", businessCategory.getBusinessCategoryId(), businessCategory.getCategory() + " |");
        }
        System.out.println();
        String response = "";
        String firstAvailableTime = "";

        do {
            try {
                System.out.print("Enter Business category> ");
                Long input = sc.nextLong(); //catch inputmismatch
                sc.nextLine();
                System.out.print("Enter City> ");
                String city = sc.nextLine().trim();

                System.out.print("Enter Date (YYYY-MM-DD)> ");
                String currentDate = sc.nextLine().trim();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(currentDate, formatter);
                String day = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);

                System.out.printf("%-19s%-6s%-22s%-9s%-16s\n", "Service Provider Id", "| Name", "| First available Time", "| Address", "| Overall rating");

                if (!day.equals("Sunday")) { //insert invalid add appt

                    BusinessCategoryEntity businessCategory = businessCategoryEntitySessionBeanRemote.retrieveBusinessCategoriesById(input);

                    List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityBySearch(businessCategory.getCategory(), city);

                    for (ServiceProviderEntity s : serviceProviders) {
                        List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveSortedAppointmentsByDate(date, s.getServiceProviderId());
                        if (appointmentEntities.size() == 11) {
                            continue;
                        } else {
                            if (appointmentEntities.size() == 0) {
                                firstAvailableTime = "08:30";
                            } else {
                                List<String> timeSlots = new ArrayList<>();
                                timeSlots.addAll(times);

                                int i = 0;
                                for (AppointmentEntity appointment : appointmentEntities) {
                                    if (!appointment.getScheduledTime().toString().equals(timeSlots.get(i))) {
                                        if (appointment.getIsCancelled().equals(Boolean.TRUE) && appointment.getScheduledTime().toString().equals(timeSlots.get(i))) {

                                            firstAvailableTime = timeSlots.get(i);
                                            break; //found the index
                                        }
                                        i++;
                                    }
                                }
                            }
                        }

                        Double rating = s.getRating();
                        if (rating == null) {
                            System.out.println(s.getServiceProviderId() + "| " + s.getName() + "| " + firstAvailableTime + "| " + s.getBusinessAddress() + "| N.A");
                        } else {
                            System.out.println(s.getServiceProviderId() + "| " + s.getName() + "| " + firstAvailableTime + "| " + s.getBusinessAddress() + "| " + rating);
                        }
                    }

                    System.out.println("Enter 0 to go back to the previous menu.");
                    System.out.print("Service provider Id> ");
                    response = sc.nextLine().trim();

                    if (!response.equals("0")) {
                        Long serviceProviderId = Long.parseLong(response);

                        List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveSortedAppointmentsByDate(date, serviceProviderId);
                        List<String> availableTimings = new ArrayList<String>();

                        //want to find out when he is not available
                        //for each appointment, get the scheduled time. he is not available
                        List<String> timeslots2 = new ArrayList<String>();
                        timeslots2.addAll(times);

                        int index = 0;
                        for (AppointmentEntity a : appointmentEntities) {
                            String scheduledTime = a.getScheduledTime().toString();
                            if (a.getIsCancelled().equals(Boolean.FALSE)) {
                                if (timeslots2.contains(scheduledTime) && !timeslots2.isEmpty()) {
                                    index = timeslots2.indexOf(scheduledTime);
                                    System.out.println("Index is " + index + " Timing is " + scheduledTime);
                                    timeslots2.remove(index);
                                }
                            }
                        }

                        System.out.println("Available Appointment Slots:");

                        for (String timings : timeslots2) {
                            System.out.print(timings + " | ");
                        }

                        System.out.println("\n");

                        System.out.println("Enter 0 to go back to the previous menu.");
                        System.out.print("Enter Time> ");
                        response = sc.nextLine().trim();

                        System.out.println("You have inputted " + response);

                        if (!response.equals("0")) {
                            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
                            LocalTime time = LocalTime.parse(response, fmt);

                            // check whether at least 2 hours before appointment first
                            String currDate = LocalDate.now().toString();
                            LocalDate todayDate = LocalDate.parse(currDate, formatter);
                            LocalDate appointmentDate = date;  //date of appointment to be scheduled

                            String currentTime = LocalTime.now().format(fmt);
                            LocalTime todayTime = LocalTime.parse(currentTime, fmt);
                            LocalTime appointmentTime = time; //what they have inputted

                            int comparison = appointmentDate.compareTo(todayDate);
                            int compare = appointmentTime.compareTo(todayTime);

                            if (comparison == 0) {
                                if (compare < 2) {
                                    System.out.println("Appointment cannot be made!");
                                    return;
                                }
                            }

                            if (comparison < 0) {
                                System.out.println("Appointment cannot be made!");
                                return;
                            }

                            boolean validTime = true;
                            System.out.println("Here now is it valid: " + validTime);
                            System.out.println("Appointment Entities got " + appointmentEntities.size());

                            if (!timeslots2.contains(time.toString())) { //means not available
                                System.out.println(timeslots2);
                                System.out.println("Is 09:30 taken? ");
                                System.out.println("Time slot is already full!");
                                validTime = false;
                            }

                            if (validTime == Boolean.TRUE) {
                                try {
                                    AppointmentEntity appointmentEntity = new AppointmentEntity();
                                    String serviceProviderUIN = serviceProviderId.toString();
                                    String chosenDate = appointmentDate.toString();
                                    String appointmentNumber = serviceProviderUIN + chosenDate.substring(5, 7) + chosenDate.substring(8, 10);
                                    appointmentNumber += appointmentTime.toString().substring(0, 2) + appointmentTime.toString().substring(3, 5);
                                    appointmentEntity.setAppointmentNo(appointmentNumber);
                                    appointmentEntity.setScheduledTime(appointmentTime);
                                    appointmentEntity.setScheduledDate(appointmentDate);
                                    appointmentEntity.setCustomerEntity(currentCustomerEntity);
                                    appointmentEntity.setServiceProviderEntity(serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId));
                                    appointmentEntity.setBusinessCategoryEntity(businessCategoryEntitySessionBeanRemote.retrieveBusinessCategoriesById(input));
                                    appointmentEntitySessionBeanRemote.createNewAppointment(currentCustomerEntity.getCustomerId(), serviceProviderId, appointmentEntity);

                                    //currentCustomerEntity.getAppointmentEntities().add(appointmentEntity);
                                    //serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId).getAppointmentEntities().add(appointmentEntity);
                                    System.out.println("Appointment " + appointmentNumber + " added successfully!");

                                } catch (CustomerNotFoundException ex) {
                                    System.out.println("Customer with customer id " + currentCustomerEntity.getCustomerId() + " not found");
                                } catch (ServiceProviderNotFoundException ex) {
                                    System.out.println("Service with service provider id " + serviceProviderId + " not found");
                                } catch (ServiceProviderBlockedException ex) {
                                    System.out.println("Service with service provider id " + serviceProviderId + " is blocked!");
                                }
                            }
                            // if timeslot exists, confirm appointment
                            // System.out.println("The appointment with " + serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityById(serviceProviderId).getName() + " at " + time + " on " + currentDate + " is confirmed.");
                            System.out.println("Enter 0 to go back to the previous menu.");
                            System.out.print("Exit>");
                            response = sc.nextLine().trim();
                        }
                    }
                }
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("Service Provider cannot be found!");
            } catch (BusinessCategoryNotFoundException ex) {
                System.out.println("BusinessCategory cannot be found!");
            } catch (DateTimeParseException ex) {
                System.out.println("Please input a valid date in YYYY-MM-DD, and a valid time in HH-MM.\n");
            } catch (InputMismatchException ex) {
                System.out.println("Please input a valid Business Category Id!\n");
            }
        } while (!response.equals("0"));
    }

    public void viewAppointments() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: View Appointments ***\n");
        String response = "";

        List<AppointmentEntity> appointments = currentCustomerEntity.getAppointmentEntities();
        appointments.size();

        System.out.println("*** Customer terminal :: View Appointments ***\n");
        System.out.println("Appointments: ");
        System.out.printf("%-15s%-13s%-8s%-15s\n", "Name", "| Date", "| Time", "| Appointment No.");

        for (AppointmentEntity appointment : appointments) {
            if (appointment.getIsCancelled().equals(Boolean.FALSE)) {
                System.out.printf("%-15s%-13s%-8s%-15s\n", currentCustomerEntity.getFullName(), "| " + appointment.getScheduledDate(), "| " + appointment.getScheduledTime(), "| " + appointment.getAppointmentNo());
            }
        }

        while (!response.equals("0")) {
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print(">");
            response = sc.nextLine().trim();
        }
    }

    public void cancelAppointment() {

        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Cancel Appointment ***\n");
        String response;

        List<AppointmentEntity> appointmentEntities = currentCustomerEntity.getAppointmentEntities();
        System.out.println("Appointments: ");
        System.out.printf("%-15s%-13s%-8s%-15s\n", "Name", "| Date", "| Time", "| Appointment No.");

        for (AppointmentEntity appointment : appointmentEntities) {
            if (appointment.getIsCancelled().equals(Boolean.FALSE)) {
                System.out.printf("%-15s%-13s%-8s%-15s\n", currentCustomerEntity.getFullName(), "| " + appointment.getScheduledDate(), "| " + appointment.getScheduledTime(), "| " + appointment.getAppointmentNo());
            }
        }

        System.out.println("Enter 0 to go back to the previous menu.");
        System.out.print("Enter Appointment Id to cancel> ");
        response = sc.nextLine().trim();
        System.out.println();
        String appointmentNo = "";

        while (!response.equals("0")) {
            try {
                appointmentNo = response;
                AppointmentEntity appointmentEntity = appointmentEntitySessionBeanRemote.retrieveAppointmentByAppointmentNumber(appointmentNo);

                if (appointmentEntity.getIsCancelled() == false) {
                    LocalDate todayDate = LocalDate.now();
                    LocalDate appointmentDate = appointmentEntity.getScheduledDate();

                    LocalTime todayTime = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    todayTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime appointmentTime = appointmentEntity.getScheduledTime();

                    int comparison = appointmentDate.compareTo(todayDate);
                    int compare = appointmentTime.compareTo(todayTime);
                    System.out.println("comparison: " + comparison);
                    if (comparison > 1) { //appointmentDate is more than one day from now
                        appointmentEntitySessionBeanRemote.cancelAppointment(appointmentNo);
                        System.out.println("Appointment successfully cancelled!\n");
                    } else if (comparison == 1) {
                        if (compare >= 0) {
                            appointmentEntitySessionBeanRemote.cancelAppointment(appointmentNo);
                            System.out.println("Appointment successfully cancelled!\n");
                        } else {
                            System.out.println("Appointment cannot be cancelled!\n");
                        }
                    } else {
                        System.out.println("Appointment cannot be cancelled!\n");
                    }

                } else {
                    System.out.println("Appointment is already cancelled!\n");
                }

            } catch (AppointmentNotFoundException ex) {
                System.out.println("Appointment with id: " + appointmentNo + " does not exist!\n");
            }

            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter Appointment Id to cancel> ");
            response = sc.nextLine().trim();
        }
    }

    public void rateServiceProvider() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Rate Service Provider ***\n");
        String response = "";

        do {

            System.out.print("Enter Service Provider Name> ");
            String name = sc.nextLine().trim();

            try {
                ServiceProviderEntity serviceProvider = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityByName(name);
                System.out.println("You are rating " + name + ".\n");
                List<AppointmentEntity> apptList = appointmentEntitySessionBeanRemote.retrieveAppointmentByCustomer(currentCustomerEntity.getCustomerId(), serviceProvider.getServiceProviderId());

                if (apptList.isEmpty()) {
                    System.out.println("You do not have any appointment under " + name + "\n");
                } else if (apptList.get(0).getScheduledDate().compareTo(LocalDate.now()) > 0) {
                    System.out.println("You do not have any completed appointments under " + name + "\n");
                } else {
                    System.out.println("Enter 0 to go back to the previous menu.");
                    if (!response.equals("0")) {
                        System.out.print("Enter rating> ");
                        double rating = sc.nextDouble();
                        sc.nextLine();
                        if (rating != 0) {
                            if (rating > 5.0 | rating < 0.0) {
                                System.out.println("Please enter a number between 0.0 to 5.0!\n");
                            } else {
                                serviceProviderEntitySessionBeanRemote.updateRating(rating, serviceProvider.getServiceProviderId());
                                System.out.println("Rating successfully submitted!\n");
                            }
                        }
                    } else {
                        break;
                    }
                }
            } catch (ServiceProviderNotFoundException ex) {
                System.out.println("Service Provider does not exist!\n");
            } catch (InputMismatchException ex) {
                System.out.println("Please enter a number between 0.0 to 5.0!\n");
            } catch (ServiceProviderBlockedException ex) {
                System.out.println("Service Provider is blocked!\n");
            }

            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print(">");
            response = sc.nextLine().trim();
        } while (!response.equals("0"));

    }

}
