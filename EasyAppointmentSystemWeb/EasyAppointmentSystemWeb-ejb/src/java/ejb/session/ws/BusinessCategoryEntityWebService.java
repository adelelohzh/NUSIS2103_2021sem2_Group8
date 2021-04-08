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
import util.exception.InvalidLoginCredentialException;


@WebService(serviceName = "BusinessCategoryEntityWebService")
@Stateless()
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
            
}
