package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.List;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import util.email.EmailManager;



@Stateless
@Local(EmailSessionBeanLocal.class)
@Remote(EmailSessionBeanRemote.class)

public class EmailSessionBean implements EmailSessionBeanLocal, EmailSessionBeanRemote 
{
    private final String GMAIL_USERNAME = "<REPLACE_WITH_GMAIL_EMAIL>";
    private final String GMAIL_PASSWORD = "<REPLACE_WITH_GMAIL_PASSWORD>";
    
    
    @Override
    public Boolean emailCheckoutNotificationSync(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress)
    {
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailCheckoutNotification(appointmentEntity, fromEmailAddress, toEmailAddress);
        
        return result;
    } 
    
    
    
    @Asynchronous
    public Future<Boolean> emailCheckoutNotificationAsync(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress) throws InterruptedException
    {        
        EmailManager emailManager = new EmailManager(GMAIL_USERNAME, GMAIL_PASSWORD);
        Boolean result = emailManager.emailCheckoutNotification(appointmentEntity, fromEmailAddress, toEmailAddress);
        
        return new AsyncResult<>(result);
    }
}