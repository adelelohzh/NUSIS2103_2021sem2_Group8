package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.List;
import java.util.concurrent.Future;


public interface EmailSessionBeanLocal {
    
    public Boolean emailCheckoutNotificationSync(List<AppointmentEntity> customerAppointmentEntities, String name_namecompnusedusg, String toEmailAddress);

    public Future<Boolean> emailCheckoutNotificationAsync(List<AppointmentEntity> appointmentEntities, String fromEmailAddress, String toEmailAddress) throws InterruptedException;
    
}
