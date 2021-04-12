
package ws.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebService(name = "BookingWebService", targetNamespace = "http://ws.session.ejb/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface BookingWebService {


    /**
     * 
     * @param password
     * @param serviceProviderId
     * @param customerId
     * @param email
     * @return
     *     returns java.lang.Long
     * @throws AppointmentNumberExistsException_Exception
     * @throws CustomerNotFoundException_Exception
     * @throws InputDataValidationException_Exception
     * @throws ServiceProviderBlockedException_Exception
     * @throws UnknownPersistenceException_Exception
     * @throws InvalidLoginCredentialException_Exception
     * @throws ServiceProviderNotFoundException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "doBooking", targetNamespace = "http://ws.session.ejb/", className = "ws.client.DoBooking")
    @ResponseWrapper(localName = "doBookingResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.DoBookingResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/doBookingRequest", output = "http://ws.session.ejb/BookingWebService/doBookingResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/doBooking/Fault/InvalidLoginCredentialException"),
        @FaultAction(className = UnknownPersistenceException_Exception.class, value = "http://ws.session.ejb/BookingWebService/doBooking/Fault/UnknownPersistenceException"),
        @FaultAction(className = InputDataValidationException_Exception.class, value = "http://ws.session.ejb/BookingWebService/doBooking/Fault/InputDataValidationException"),
        @FaultAction(className = AppointmentNumberExistsException_Exception.class, value = "http://ws.session.ejb/BookingWebService/doBooking/Fault/AppointmentNumberExistsException"),
        @FaultAction(className = CustomerNotFoundException_Exception.class, value = "http://ws.session.ejb/BookingWebService/doBooking/Fault/CustomerNotFoundException"),
        @FaultAction(className = ServiceProviderNotFoundException_Exception.class, value = "http://ws.session.ejb/BookingWebService/doBooking/Fault/ServiceProviderNotFoundException"),
        @FaultAction(className = ServiceProviderBlockedException_Exception.class, value = "http://ws.session.ejb/BookingWebService/doBooking/Fault/ServiceProviderBlockedException")
    })
    public Long doBooking(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "customerId", targetNamespace = "")
        Long customerId,
        @WebParam(name = "serviceProviderId", targetNamespace = "")
        Long serviceProviderId)
        throws AppointmentNumberExistsException_Exception, CustomerNotFoundException_Exception, InputDataValidationException_Exception, InvalidLoginCredentialException_Exception, ServiceProviderBlockedException_Exception, ServiceProviderNotFoundException_Exception, UnknownPersistenceException_Exception
    ;

    /**
     * 
     * @param password
     * @param email
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "clearAppointmentCart", targetNamespace = "http://ws.session.ejb/", className = "ws.client.ClearAppointmentCart")
    @ResponseWrapper(localName = "clearAppointmentCartResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.ClearAppointmentCartResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/clearAppointmentCartRequest", output = "http://ws.session.ejb/BookingWebService/clearAppointmentCartResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/clearAppointmentCart/Fault/InvalidLoginCredentialException")
    })
    public void clearAppointmentCart(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param email
     * @return
     *     returns java.lang.String
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAppointmentNo", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetAppointmentNo")
    @ResponseWrapper(localName = "getAppointmentNoResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetAppointmentNoResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/getAppointmentNoRequest", output = "http://ws.session.ejb/BookingWebService/getAppointmentNoResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/getAppointmentNo/Fault/InvalidLoginCredentialException")
    })
    public String getAppointmentNo(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param appointmentNo
     * @param email
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setAppointmentNo", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetAppointmentNo")
    @ResponseWrapper(localName = "setAppointmentNoResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetAppointmentNoResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/setAppointmentNoRequest", output = "http://ws.session.ejb/BookingWebService/setAppointmentNoResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/setAppointmentNo/Fault/InvalidLoginCredentialException")
    })
    public void setAppointmentNo(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "appointmentNo", targetNamespace = "")
        String appointmentNo)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param email
     * @return
     *     returns java.lang.String
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getScheduledTime", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetScheduledTime")
    @ResponseWrapper(localName = "getScheduledTimeResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetScheduledTimeResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/getScheduledTimeRequest", output = "http://ws.session.ejb/BookingWebService/getScheduledTimeResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/getScheduledTime/Fault/InvalidLoginCredentialException")
    })
    public String getScheduledTime(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param scheduledTime
     * @param email
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setScheduledTime", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetScheduledTime")
    @ResponseWrapper(localName = "setScheduledTimeResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetScheduledTimeResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/setScheduledTimeRequest", output = "http://ws.session.ejb/BookingWebService/setScheduledTimeResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/setScheduledTime/Fault/InvalidLoginCredentialException")
    })
    public void setScheduledTime(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "scheduledTime", targetNamespace = "")
        String scheduledTime)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param email
     * @return
     *     returns java.lang.String
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getScheduledDate", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetScheduledDate")
    @ResponseWrapper(localName = "getScheduledDateResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetScheduledDateResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/getScheduledDateRequest", output = "http://ws.session.ejb/BookingWebService/getScheduledDateResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/getScheduledDate/Fault/InvalidLoginCredentialException")
    })
    public String getScheduledDate(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param setScheduledDate
     * @param password
     * @param email
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setScheduledDate", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetScheduledDate")
    @ResponseWrapper(localName = "setScheduledDateResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetScheduledDateResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/setScheduledDateRequest", output = "http://ws.session.ejb/BookingWebService/setScheduledDateResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/setScheduledDate/Fault/InvalidLoginCredentialException")
    })
    public void setScheduledDate(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "setScheduledDate", targetNamespace = "")
        String setScheduledDate)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param email
     * @return
     *     returns ws.client.CustomerEntity
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getCustomerEntity", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetCustomerEntity")
    @ResponseWrapper(localName = "getCustomerEntityResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetCustomerEntityResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/getCustomerEntityRequest", output = "http://ws.session.ejb/BookingWebService/getCustomerEntityResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/getCustomerEntity/Fault/InvalidLoginCredentialException")
    })
    public CustomerEntity getCustomerEntity(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param email
     * @param newCustomerEntity
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setCustomerEntity", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetCustomerEntity")
    @ResponseWrapper(localName = "setCustomerEntityResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetCustomerEntityResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/setCustomerEntityRequest", output = "http://ws.session.ejb/BookingWebService/setCustomerEntityResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/setCustomerEntity/Fault/InvalidLoginCredentialException")
    })
    public void setCustomerEntity(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "newCustomerEntity", targetNamespace = "")
        CustomerEntity newCustomerEntity)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param email
     * @return
     *     returns ws.client.ServiceProviderEntity
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getServiceProviderEntity", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetServiceProviderEntity")
    @ResponseWrapper(localName = "getServiceProviderEntityResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetServiceProviderEntityResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/getServiceProviderEntityRequest", output = "http://ws.session.ejb/BookingWebService/getServiceProviderEntityResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/getServiceProviderEntity/Fault/InvalidLoginCredentialException")
    })
    public ServiceProviderEntity getServiceProviderEntity(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param email
     * @param serviceProviderEntity
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setServiceProviderEntity", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetServiceProviderEntity")
    @ResponseWrapper(localName = "setServiceProviderEntityResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetServiceProviderEntityResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/setServiceProviderEntityRequest", output = "http://ws.session.ejb/BookingWebService/setServiceProviderEntityResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/setServiceProviderEntity/Fault/InvalidLoginCredentialException")
    })
    public void setServiceProviderEntity(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "serviceProviderEntity", targetNamespace = "")
        ServiceProviderEntity serviceProviderEntity)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param email
     * @return
     *     returns ws.client.BusinessCategoryEntity
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getBusinessCategoryEntity", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetBusinessCategoryEntity")
    @ResponseWrapper(localName = "getBusinessCategoryEntityResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.GetBusinessCategoryEntityResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/getBusinessCategoryEntityRequest", output = "http://ws.session.ejb/BookingWebService/getBusinessCategoryEntityResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/getBusinessCategoryEntity/Fault/InvalidLoginCredentialException")
    })
    public BusinessCategoryEntity getBusinessCategoryEntity(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param password
     * @param businessCategoryEntity
     * @param email
     * @throws InvalidLoginCredentialException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setBusinessCategoryEntity", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetBusinessCategoryEntity")
    @ResponseWrapper(localName = "setBusinessCategoryEntityResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.SetBusinessCategoryEntityResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/setBusinessCategoryEntityRequest", output = "http://ws.session.ejb/BookingWebService/setBusinessCategoryEntityResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/setBusinessCategoryEntity/Fault/InvalidLoginCredentialException")
    })
    public void setBusinessCategoryEntity(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "businessCategoryEntity", targetNamespace = "")
        BusinessCategoryEntity businessCategoryEntity)
        throws InvalidLoginCredentialException_Exception
    ;

    /**
     * 
     * @param customerEntity
     * @param password
     * @param scheduledTime
     * @param scheduledDate
     * @param businessCategoryEntity
     * @param appointmentNo
     * @param email
     * @param serviceProviderEntity
     * @throws InvalidLoginCredentialException_Exception
     * @throws ServiceProviderNotFoundException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "addAppointment", targetNamespace = "http://ws.session.ejb/", className = "ws.client.AddAppointment")
    @ResponseWrapper(localName = "addAppointmentResponse", targetNamespace = "http://ws.session.ejb/", className = "ws.client.AddAppointmentResponse")
    @Action(input = "http://ws.session.ejb/BookingWebService/addAppointmentRequest", output = "http://ws.session.ejb/BookingWebService/addAppointmentResponse", fault = {
        @FaultAction(className = InvalidLoginCredentialException_Exception.class, value = "http://ws.session.ejb/BookingWebService/addAppointment/Fault/InvalidLoginCredentialException"),
        @FaultAction(className = ServiceProviderNotFoundException_Exception.class, value = "http://ws.session.ejb/BookingWebService/addAppointment/Fault/ServiceProviderNotFoundException")
    })
    public void addAppointment(
        @WebParam(name = "email", targetNamespace = "")
        String email,
        @WebParam(name = "password", targetNamespace = "")
        String password,
        @WebParam(name = "appointmentNo", targetNamespace = "")
        String appointmentNo,
        @WebParam(name = "scheduledTime", targetNamespace = "")
        String scheduledTime,
        @WebParam(name = "scheduledDate", targetNamespace = "")
        String scheduledDate,
        @WebParam(name = "customerEntity", targetNamespace = "")
        CustomerEntity customerEntity,
        @WebParam(name = "serviceProviderEntity", targetNamespace = "")
        ServiceProviderEntity serviceProviderEntity,
        @WebParam(name = "businessCategoryEntity", targetNamespace = "")
        BusinessCategoryEntity businessCategoryEntity)
        throws InvalidLoginCredentialException_Exception, ServiceProviderNotFoundException_Exception
    ;

}