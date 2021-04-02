package ejb.session.stateless;

import entity.AppointmentEntity;
import util.exception.AppointmentNotFoundException;


public interface AppointmentEntitySessionBeanLocal {

    public AppointmentEntity retrieveAppointmentByCustomerID(Long customerID) throws AppointmentNotFoundException;
    
}
