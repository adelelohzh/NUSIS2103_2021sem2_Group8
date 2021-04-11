/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.AppointmentEntity;
import entity.CustomerEntity;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.AppointmentNotFoundException;
import util.exception.AppointmentNumberExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ServiceProviderBlockedException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author adele
 */
@WebService(serviceName = "AppointmentEntityWebService")
@Stateless()
public class AppointmentEntityWebService {

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;

    @WebMethod(operationName = "retrieveSortedAppointmentsByDate")
    public List<AppointmentEntity> retrieveSortedAppointmentsByDate(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "date") LocalDate date,
            @WebParam(name = "serviceProviderId") Long serviceProviderId)
            throws InvalidLoginCredentialException, ServiceProviderNotFoundException, InvalidLoginCredentialException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveSortedAppointmentsByDate(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveSortedAppointmentsByDate(date, serviceProviderId);
    }

    @WebMethod(operationName = "retrieveAppointmentByAppointmentNumber")
    public AppointmentEntity retrieveAppointmentByAppointmentNumber(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "appointmentNumber") String apptNo)
            throws InvalidLoginCredentialException, AppointmentNotFoundException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentNumber(apptNo);
    }

    @WebMethod(operationName = "updateAppointmentEntity")
    public void updateAppointmentEntity(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "appointmentEntity") AppointmentEntity appointmentEntity) throws InvalidLoginCredentialException, InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

    }

    @WebMethod(operationName = "deleteAppointment")
    public void deleteAppointment(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "appointmentNumber") String appointmentNo) throws AppointmentNotFoundException, InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");
    }

    @WebMethod(operationName = "retrieveAppointmentByCustomer")
    public List<AppointmentEntity> retrieveAppointmentByCustomer(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "customerId") Long customerId,
            @WebParam(name = "serviceProviderId") Long serviceProviderId) throws InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveAppointmentByCustomer(customerId, serviceProviderId);
    }

    @WebMethod(operationName = "retrieveAppointmentsByServiceProviderId")
    public List<AppointmentEntity> retrieveAppointmentsByServiceProviderId(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "serviceProviderId") Long serviceProviderId) throws InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveAppointmentsByServiceProviderId(serviceProviderId);
    }

    @WebMethod(operationName = "retrieveAppointmentsByDate")
    public List<AppointmentEntity> retrieveAppointmentsByDate(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "date") LocalDate date,
            @WebParam(name = "serviceProviderName") String serviceProviderName) throws InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveAppointmentsByDate(date, serviceProviderName);
    }

    @WebMethod(operationName = "retrieveAppointmentByCustomerID")
    public AppointmentEntity retrieveAppointmentByCustomerID(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "customerId") Long customerID) throws AppointmentNotFoundException, InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveAppointmentByCustomerID(customerID);
    }

    @WebMethod(operationName = "retrieveAppointmentByAppointmentId")
    public AppointmentEntity retrieveAppointmentByAppointmentId(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "appointmentId") Long appointmentId) throws AppointmentNotFoundException, InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(appointmentId);

    }

    public Long createNewAppointment(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "customerId") Long customerId, 
            @WebParam(name = "serviceProviderId") Long serviceProviderId, 
            @WebParam(name = "appointmentEntity") AppointmentEntity newAppointmentEntity) throws InvalidLoginCredentialException, UnknownPersistenceException, InputDataValidationException, AppointmentNumberExistsException, AppointmentNumberExistsException, CustomerNotFoundException, ServiceProviderNotFoundException, ServiceProviderBlockedException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.createNewAppointment(customerId, serviceProviderId, newAppointmentEntity);
    }
}
