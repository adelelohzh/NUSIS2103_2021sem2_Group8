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
 * @author valenciateh
 */
@WebService(serviceName = "AppointmentEntityWebService")
@Stateless
public class AppointmentEntityWebService {

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB(name = "AppointmentEntitySessionBeanLocal")
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;

    @WebMethod(operationName = "createNewAppointment")
    public Long createNewAppointment(@WebParam(name = "email") String emailAddr,
                                                                     @WebParam(name = "password") String password,
                                                                     @WebParam(name = "customerId") Long customerId, 
                                                                     @WebParam(name = "serviceProviderId") Long serviceProviderId, 
                                                                     @WebParam(name = "newAppointmentEntity") AppointmentEntity newAppointmentEntity) 
            throws UnknownPersistenceException, InputDataValidationException, AppointmentNumberExistsException, CustomerNotFoundException, ServiceProviderNotFoundException, ServiceProviderBlockedException, InvalidLoginCredentialException
    {
        
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveServiceProviderEntityByName(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
        
        return appointmentEntitySessionBeanLocal.createNewAppointment(customerId, serviceProviderId, newAppointmentEntity);
    }
    

   @WebMethod(operationName = "retrieveAppointmentByCustomer")
    public List<AppointmentEntity> retrieveAppointmentByCustomer(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "customerId") Long customerId,
            @WebParam(name = "serviceProviderId") Long serviceProviderId) throws InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentsByCustomer(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveAppointmentByCustomer(customerId, serviceProviderId);
    }

    @WebMethod(operationName = "retrieveAppointmentsByServiceProviderId")
    public List<AppointmentEntity> retrieveAppointmentsByServiceProviderId(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "serviceProviderId") Long serviceProviderId) throws InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByServiceProviderId(): Customer "
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
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByDate(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveAppointmentsByDate(date, serviceProviderName);
    }
    
    @WebMethod(operationName = "retrievSortedeAppointmentsByDate")
    public List<AppointmentEntity> retrieveSortedAppointmentsByDate(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "date") LocalDate date,
            @WebParam(name = "serviceProviderId") Long serviceProviderId) throws InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveSortedAppointmentsByDate(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveSortedAppointmentsByDate(date, serviceProviderId);
    }

    @WebMethod(operationName = "retrieveAppointmentByAppointmentId")
    public AppointmentEntity retrieveAppointmentByAppointmentId(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "appointmentId") Long appointmentId) throws AppointmentNotFoundException, InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentId(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");

        return appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentId(appointmentId);

    }

    @WebMethod(operationName = "updateAppointmentEntity")
    public void updateAppointmentEntity(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "appointmentEntity") AppointmentEntity appointmentEntity) throws InvalidLoginCredentialException
    {
        
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.updateAppointmentEntity(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");
        
        appointmentEntitySessionBeanLocal.updateAppointmentEntity(appointmentEntity);
    }
    
    @WebMethod(operationName = "deleteAppointment")
    public void deleteAppointment(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "appointmentNumber") String appointmentNo) throws AppointmentNotFoundException, InvalidLoginCredentialException {

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.deleteAppointment(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");
        
        appointmentEntitySessionBeanLocal.deleteAppointment(appointmentNo);
        
    }

    @WebMethod(operationName = "retrieveAppointmentByAppointmentNumber")
    public AppointmentEntity retrieveAppointmentByAppointmentNumber(@WebParam(name = "email") String emailAddr,
            @WebParam(name = "password") String password,
            @WebParam(name = "appointmentNumber") String appointmentNo) throws AppointmentNotFoundException, InvalidLoginCredentialException {
        
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer "
                + customerEntity.getEmailAddress()
                + " login remotely via web service");
        
        return appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentNumber(appointmentNo);
        
    }



    
}