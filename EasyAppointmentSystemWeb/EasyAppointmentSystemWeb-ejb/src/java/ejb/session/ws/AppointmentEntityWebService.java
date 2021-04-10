/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateful.AppointmentEntitySessionBeanLocal;
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
import util.exception.InvalidLoginCredentialException;
import util.exception.ServiceProviderNotFoundException;

/**
 *
 * @author adele
 */
@WebService(serviceName = "AppointmentEntityWebService")
@Stateless()
public class AppointmentEntityWebService 
{

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;
    

    @WebMethod(operationName = "retrieveSortedAppointmentsByDate")
    public List<AppointmentEntity> retrieveSortedAppointmentsByDate(@WebParam(name = "email") String emailAddr,
                                                                        @WebParam(name = "password") String password,
                                                                        @WebParam(name = "date") LocalDate date,
                                                                        @WebParam(name = "serviceProviderId") Long serviceProviderId)                                                      
                                                    throws InvalidLoginCredentialException, ServiceProviderNotFoundException
    {
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
                                throws InvalidLoginCredentialException, AppointmentNotFoundException
    {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAppointmentByAppointmentNumber(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
        
        return appointmentEntitySessionBeanLocal.retrieveAppointmentByAppointmentNumber(apptNo);
    }
}
