package ejb.session.ws;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.EmailSessionBeanLocal;
import entity.AppointmentEntity;
import entity.CustomerEntity;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;


@WebService(serviceName = "EmailWebService")
@Stateless
public class EmailWebService {

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB
    private EmailSessionBeanLocal emailSessionBeanLocal;

    @WebMethod(operationName = "emailCheckoutNotificationSync")
    public Boolean emailCheckoutNotificationSync(@WebParam(name = "email") String emailAddr,
                                                 @WebParam(name = "password") String password,
                                                 @WebParam(name = "appointmentEntity") AppointmentEntity appointmentEntity,
                                                 @WebParam(name = "fromEmailAddress") String fromEmailAddress,
                                                 @WebParam(name = "toEmailAddress") String toEmailAddress)                                                 
                                        throws InvalidLoginCredentialException
   {
       
       CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
       System.out.println("********** EasyAppointmentSystem.emailCheckoutNotificationSync(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
       
       return emailSessionBeanLocal.emailCheckoutNotificationSync(appointmentEntity, fromEmailAddress, toEmailAddress);
   }
    
    @WebMethod(operationName = "emailCheckoutNotificationAsync") 
    public Future<Boolean> emailCheckoutNotificationAsync(@WebParam(name = "email") String emailAddr,
                                                 @WebParam(name = "password") String password,
                                                 @WebParam(name = "appointmentEntity") AppointmentEntity appointmentEntity,
                                                 @WebParam(name = "fromEmailAddress") String fromEmailAddress,
                                                 @WebParam(name = "toEmailAddress") String toEmailAddress)                                                 
                                        throws InvalidLoginCredentialException, InterruptedException
   {
       
       CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(emailAddr, password);
       System.out.println("********** EasyAppointmentSystem.emailCheckoutNotificationAsync(): Customer " 
                            + customerEntity.getEmailAddress() 
                            + " login remotely via web service");
       
       return emailSessionBeanLocal.emailCheckoutNotificationAsync(appointmentEntity, fromEmailAddress, toEmailAddress);
   }  
}
