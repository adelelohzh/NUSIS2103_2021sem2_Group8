package ejb.session.stateless;

import entity.AppointmentEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AppointmentNotFoundException;
import util.exception.AppointmentNumberExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;


public interface AppointmentEntitySessionBeanRemote 
{
    public Long createNewAppointment(Long customerId, Long serviceProviderId, AppointmentEntity newAppointmentEntity) throws UnknownPersistenceException, InputDataValidationException, AppointmentNumberExistsException, CustomerNotFoundException, ServiceProviderNotFoundException;

    public AppointmentEntity retrieveAppointmentByCustomerID(Long customerID) throws AppointmentNotFoundException;

    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;

    public AppointmentEntity retrieveAppointmentByAppointmentNumber(String appointmentNo) throws AppointmentNotFoundException;

    public List<AppointmentEntity> retrieveAppointmentsByDate(LocalDate date, String serviceProviderName);

    public List<AppointmentEntity> retrieveSortedAppointmentsByDate(LocalDate date, Long serviceProviderId);

    public void deleteAppointment(String appointmentNo) throws AppointmentNotFoundException;

    public void cancelAppointment(String appointmentNo) throws AppointmentNotFoundException;

    public void updateAppointmentEntity(AppointmentEntity appointmentEntity);
    
}
