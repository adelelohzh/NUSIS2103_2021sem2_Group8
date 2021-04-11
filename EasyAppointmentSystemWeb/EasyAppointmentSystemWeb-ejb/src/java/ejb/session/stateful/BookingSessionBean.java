package ejb.session.stateful;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import util.exception.AppointmentNumberExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderBlockedException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;


@Stateful
@Local(BookingSessionBeanLocal.class)
@Remote(BookingSessionBeanRemote.class)
public class BookingSessionBean implements BookingSessionBeanRemote, BookingSessionBeanLocal 
{

    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;
    
    private String appointmentNo;
    private LocalTime scheduledTime;
    private LocalDate scheduledDate;
    private CustomerEntity customerEntity;
    private ServiceProviderEntity serviceProviderEntity;
    private BusinessCategoryEntity businessCategoryEntity;
      
    
    public BookingSessionBean() 
    {
        initialiseState();
    }
    
    
    
    @Remove
    public void remove()
    {        
    }
    
    
    
    private void initialiseState()
    {
        
    }
    
   // @Override
   // public Integer addAppointment(AppointmentEntity appointmentEntity)
   // {
        
   // }
    
    @Override
    public Long doBooking(Long customerId, Long serviceProviderId) throws UnknownPersistenceException, InputDataValidationException, AppointmentNumberExistsException, CustomerNotFoundException, ServiceProviderNotFoundException, ServiceProviderBlockedException
    {

        Long newAppointmentEntityId = appointmentEntitySessionBeanLocal.createNewAppointment(customerId, serviceProviderId, new AppointmentEntity(appointmentNo, scheduledTime, scheduledDate, customerEntity, serviceProviderEntity, businessCategoryEntity));
        
        initialiseState();
        return newAppointmentEntityId;
    }
    
    @Override
    public void clearAppointmentCart()
    {
        initialiseState();
    }

    @Override
    public String getAppointmentNo() {
        return appointmentNo;
    }

    @Override
    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    @Override
    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    @Override
    public void setScheduledTime(LocalTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    @Override
    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    @Override
    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    @Override
    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    @Override
    public ServiceProviderEntity getServiceProviderEntity() {
        return serviceProviderEntity;
    }

    @Override
    public void setServiceProviderEntity(ServiceProviderEntity serviceProviderEntity) {
        this.serviceProviderEntity = serviceProviderEntity;
    }

    @Override
    public BusinessCategoryEntity getBusinessCategoryEntity() {
        return businessCategoryEntity;
    }

    @Override
    public void setBusinessCategoryEntity(BusinessCategoryEntity businessCategoryEntity) {
        this.businessCategoryEntity = businessCategoryEntity;
    }
}
