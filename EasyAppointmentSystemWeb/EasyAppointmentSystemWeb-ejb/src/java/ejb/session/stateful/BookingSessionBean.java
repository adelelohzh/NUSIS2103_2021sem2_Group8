package ejb.session.stateful;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import entity.AppointmentEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;


@Stateful
@Local(BookingSessionBeanLocal.class)
@Remote(BookingSessionBeanRemote.class)
public class BookingSessionBean implements BookingSessionBeanLocal, BookingSessionBeanRemote {

    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;
    
    private List<AppointmentEntity> appointmentEntities;
    private Integer totalAppointments;   

    public BookingSessionBean() {
    }
    
    @Remove
    public void remove()
    {        
    }

    
    @Override
    public List<AppointmentEntity> getAppointmentEntities() {
        return appointmentEntities;
    }

    @Override
    public void setAppointmentEntities(List<AppointmentEntity> appointmentEntities) {
        this.appointmentEntities = appointmentEntities;
    }

    @Override
    public Integer getTotalAppointments() {
        return totalAppointments;
    }

    @Override
    public void setTotalAppointments(Integer totalAppointments) {
        this.totalAppointments = totalAppointments;
    }
    
}
