package ejb.session.stateless;

import entity.AppointmentEntity;
import util.exception.AppointmentNotFoundException;

public interface AppointmentEntitySessionBeanRemote {
    
    public AppointmentEntity retrieveAppointmentByCustomerID(Long customerID) throws AppointmentNotFoundException;
}
