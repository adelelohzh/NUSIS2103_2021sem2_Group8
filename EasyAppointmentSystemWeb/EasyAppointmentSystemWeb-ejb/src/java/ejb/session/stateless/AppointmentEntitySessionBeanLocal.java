package ejb.session.stateless;

import entity.AppointmentEntity;
import java.util.Date;
import java.util.List;
import util.exception.AppointmentNotFoundException;


public interface AppointmentEntitySessionBeanLocal {

    public AppointmentEntity retrieveAppointmentByCustomerID(Long customerID) throws AppointmentNotFoundException;

    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;

    public void deleteAppointment(String appointmentNo) throws AppointmentNotFoundException;

    public List<AppointmentEntity> retrieveAppointmentsByDate(Date date, String serviceProviderName);
    
    public List<AppointmentEntity> retrieveSortedAppointmentsByDate(Date date, Long serviceProviderId);
    
    public AppointmentEntity retrieveAppointmentByAppointmentNumber(String appointmentNo) throws AppointmentNotFoundException;
    
}
