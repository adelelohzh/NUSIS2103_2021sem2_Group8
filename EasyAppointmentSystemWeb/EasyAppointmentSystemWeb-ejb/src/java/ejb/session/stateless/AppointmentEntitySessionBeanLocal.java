package ejb.session.stateless;

import entity.AppointmentEntity;
import util.exception.AppointmentNotFoundException;


public interface AppointmentEntitySessionBeanLocal {

    public AppointmentEntity retrieveAppointmentByCustomerID(Long customerID) throws AppointmentNotFoundException;

    public AppointmentEntity retrieveAppointmentByAppointmentNumber(String appointmentNo) throws AppointmentNotFoundException;

    public void deleteAppointment(String appointmentNo) throws AppointmentNotFoundException;
    
}
