/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ServiceProviderEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ServiceProviderBlockedException;
import util.exception.ServiceProviderNotFoundException;

/**
 *
 * @author adele
 */
@WebService(serviceName = "ServiceProviderWebService")
@Stateless()
public class ServiceProviderWebService 
{

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB
    private ServiceProviderEntitySessionBeanLocal serviceProviderEntitySessionBeanLocal;
    

    @WebMethod(operationName = "retrieveServiceProviderEntityByName")
    public ServiceProviderEntity retrieveServiceProviderEntityByName(@WebParam(name = "email") String emailAddr,
                                                                     @WebParam(name = "password") String password,
                                                                     @WebParam(name = "name") String name) 
                                                            throws InvalidLoginCredentialException, ServiceProviderNotFoundException
    {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveServiceProviderEntityByName(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
        
        return serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityByName(name);
    }
    
    @WebMethod(operationName = "retrieveServiceProviderEntityBySearch")
    public List<ServiceProviderEntity> retrieveServiceProviderEntityBySearch(@WebParam(name = "email") String emailAddr,
                                                                             @WebParam(name = "password") String password,
                                                                             @WebParam(name = "business category") String businessCategory,
                                                                             @WebParam(name = "city") String city) 
                                throws InvalidLoginCredentialException, ServiceProviderNotFoundException
    {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveServiceProviderEntityBySearch(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
        
        return serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityBySearch(businessCategory, city);
    }
    
    @WebMethod(operationName = "updateRating")
    public void updateRating(@WebParam(name = "email") String emailAddr,
                                                        @WebParam(name = "password") String password,
                                                        @WebParam(name = "rating") Long rating,
                                                        @WebParam(name = "serviceProviderId") Long serviceProviderId) 
                                throws InvalidLoginCredentialException, ServiceProviderNotFoundException, ServiceProviderBlockedException
    {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.updateRating(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
        
        serviceProviderEntitySessionBeanLocal.updateRating(rating, serviceProviderId);
    }
    
    
}
