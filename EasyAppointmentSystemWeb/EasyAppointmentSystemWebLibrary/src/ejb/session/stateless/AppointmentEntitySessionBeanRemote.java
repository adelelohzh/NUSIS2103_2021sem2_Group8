package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.Date;
import java.util.List;
import util.exception.AppointmentNotFoundException;

public interface AppointmentEntitySessionBeanRemote {
    
    public AppointmentEntity retrieveAppointmentByCustomerID(Long customerID) throws AppointmentNotFoundException;
    
    public AppointmentEntity retrieveAppointmentByAppointmentNumber(String appointmentNo) throws AppointmentNotFoundException;

    public void deleteAppointment(String appointmentNo) throws AppointmentNotFoundException;
    
    public List<AppointmentEntity> retrieveAppointmentsByDate(Date date, String serviceProviderName);
}
