package ejb.session.stateless;

import entity.AppointmentEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AppointmentNotFoundException;
import util.exception.AppointmentNumberExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderBlockedException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;


public interface AppointmentEntitySessionBeanRemote 
{
    public Long createNewAppointment(Long customerId, Long serviceProviderId, AppointmentEntity newAppointmentEntity) throws UnknownPersistenceException, InputDataValidationException, AppointmentNumberExistsException, CustomerNotFoundException, ServiceProviderNotFoundException, ServiceProviderBlockedException;

    public AppointmentEntity retrieveAppointmentByCustomerID(Long customerID) throws AppointmentNotFoundException;

    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException;

    public AppointmentEntity retrieveAppointmentByAppointmentNumber(String appointmentNo) throws AppointmentNotFoundException;

    public List<AppointmentEntity> retrieveAppointmentsByDate(LocalDate date, String serviceProviderName) throws AppointmentNotFoundException ;

    public List<AppointmentEntity> retrieveSortedAppointmentsByDate(LocalDate date, Long serviceProviderId) throws AppointmentNotFoundException ;

    public void deleteAppointment(String appointmentNo) throws AppointmentNotFoundException;

    public void updateAppointmentEntity(AppointmentEntity appointmentEntity);
    
    public List<AppointmentEntity> retrieveAppointmentByCustomer(Long customerId, Long serviceProviderId) throws AppointmentNotFoundException;

    public List<AppointmentEntity> retrieveAppointmentsByServiceProviderId(Long serviceProviderId) throws AppointmentNotFoundException ;

    public List<AppointmentEntity> retrieveAppointmentsByCustomerEntityId(Long customerId) throws AppointmentNotFoundException ;

    public void updateAppointmentEntityRating(Long appointmentId) throws AppointmentNotFoundException;

}
