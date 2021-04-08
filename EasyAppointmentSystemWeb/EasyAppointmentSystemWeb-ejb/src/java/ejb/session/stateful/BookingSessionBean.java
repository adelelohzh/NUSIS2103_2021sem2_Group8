package ejb.session.stateful;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import entity.AppointmentEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;


@Stateful
@Local(BookingSessionBeanLocal.class)
@Remote(BookingSessionBeanRemote.class)
public class BookingSessionBean implements BookingSessionBeanLocal, BookingSessionBeanRemote {

    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;
    
    private List<AppointmentEntity> appointmentEntities;
    
}
