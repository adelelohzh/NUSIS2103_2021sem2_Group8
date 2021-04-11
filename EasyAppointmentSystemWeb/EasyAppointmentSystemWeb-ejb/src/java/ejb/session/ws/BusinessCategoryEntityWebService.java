package ejb.session.ws;

import ejb.session.stateless.BusinessCategoryEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.DeleteBusinessCategoryException;
import util.exception.InvalidLoginCredentialException;


@WebService(serviceName = "BusinessCategoryEntityWebService")
@Stateless
public class BusinessCategoryEntityWebService 
{

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB
    private BusinessCategoryEntitySessionBeanLocal businessCategoryEntitySessionBeanLocal;
    

    @WebMethod(operationName = "retrieveAllBusinessCategories")
    public List<BusinessCategoryEntity> retrieveAllBusinessCategories(@WebParam(name = "email") String emailAddr,
                                                                      @WebParam(name = "password") String password)                                                      
                                                            throws InvalidLoginCredentialException
    {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveAllBusinessCategories(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
        
        return businessCategoryEntitySessionBeanLocal.retrieveAllBusinessCategories();
    }
    
    @WebMethod(operationName = "retrieveBusinessCategoriesByName")
    public BusinessCategoryEntity retrieveBusinessCategoriesByName(@WebParam(name = "email") String emailAddr,
                                                                         @WebParam(name = "password") String password,
                                                                         @WebParam(name = "businessCategoryName") String businessCategoryName)                                                      
                                                            throws InvalidLoginCredentialException, BusinessCategoryNotFoundException
    {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveBusinessCategoriesByName(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
        
        return businessCategoryEntitySessionBeanLocal.retrieveBusinessCategoriesByName(businessCategoryName);
    }
    
    @WebMethod(operationName = "retrieveBusinessCategoriesById")
    public BusinessCategoryEntity retrieveBusinessCategoriesById(@WebParam(name = "email") String emailAddr,
                                                                 @WebParam(name = "password") String password,
                                                                 @WebParam(name = "businessCategoryId") Long businessCategoryId)                                                      
                                                            throws InvalidLoginCredentialException, BusinessCategoryNotFoundException
    {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.retrieveBusinessCategoriesById(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
        
        return businessCategoryEntitySessionBeanLocal.retrieveBusinessCategoriesById(businessCategoryId);
    }
    
    @WebMethod(operationName = "deleteBusinessCategory")
    public void deleteBusinessCategory(@WebParam(name = "email") String emailAddr,
                                       @WebParam(name = "password") String password,
                                       @WebParam(name = "businessCategoryId") Long businessCategoryId)                                                      
                                throws InvalidLoginCredentialException, BusinessCategoryNotFoundException, DeleteBusinessCategoryException
    {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
        System.out.println("********** EasyAppointmentSystem.deleteBusinessCategory(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
    }       
}
