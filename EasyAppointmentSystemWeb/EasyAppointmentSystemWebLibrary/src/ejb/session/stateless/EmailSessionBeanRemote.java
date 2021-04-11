package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.concurrent.Future;

public interface EmailSessionBeanRemote 
{

    public Boolean emailCheckoutNotificationSync(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress);

    public Future<Boolean> emailCheckoutNotificationAsync(AppointmentEntity appointmentEntity, String fromEmailAddress, String toEmailAddress) throws InterruptedException;
    
}
