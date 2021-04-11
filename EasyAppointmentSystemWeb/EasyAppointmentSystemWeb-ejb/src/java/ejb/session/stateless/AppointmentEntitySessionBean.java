package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AppointmentNotFoundException;
import util.exception.AppointmentNumberExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderBlockedException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;


@Stateless
@Local(ejb.session.stateless.AppointmentEntitySessionBeanLocal.class)
@Remote(ejb.session.stateless.AppointmentEntitySessionBeanRemote.class)
public class AppointmentEntitySessionBean implements AppointmentEntitySessionBeanRemote, AppointmentEntitySessionBeanLocal 
{
    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private ServiceProviderEntitySessionBeanLocal serviceProviderEntitySessionBeanLocal;
    

    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    
    public AppointmentEntitySessionBean() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewAppointment(Long customerId, Long serviceProviderId, AppointmentEntity newAppointmentEntity) throws UnknownPersistenceException, InputDataValidationException, AppointmentNumberExistsException, CustomerNotFoundException, ServiceProviderNotFoundException, ServiceProviderBlockedException
    {
        try
        {
            Set<ConstraintViolation<AppointmentEntity>>constraintViolations = validator.validate(newAppointmentEntity);
        
            if(constraintViolations.isEmpty())
            { 
                CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerEntityByCustomerId(customerId);
                ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityById(serviceProviderId);
                newAppointmentEntity.setCustomerEntity(customerEntity);
                newAppointmentEntity.setServiceProviderEntity(serviceProviderEntity);
                customerEntity.getAppointmentEntities().add(newAppointmentEntity);
                serviceProviderEntity.getAppointmentEntities().add(newAppointmentEntity);

                em.persist(newAppointmentEntity);
                em.flush();

                return newAppointmentEntity.getAppointmentId();
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }            
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new AppointmentNumberExistsException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public AppointmentEntity retrieveAppointmentByCustomerID(Long customerID) throws AppointmentNotFoundException {
   
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.customerID = :inCustomerID");
        query.setParameter("inCustomerID", customerID);
        
        try
        {
            AppointmentEntity appointment = (AppointmentEntity)query.getSingleResult();
            appointment.getCustomerEntity();
            appointment.getBusinessCategoryEntity();
            appointment.getServiceProviderEntity();
            return appointment;
            
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AppointmentNotFoundException("Appointment of customer " + customerID + " does not exist!");
        }
    }
    
    @Override
    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException {
        
        AppointmentEntity appointmentEntity = em.find(AppointmentEntity.class, appointmentId);
        
        if(appointmentEntity != null)
        {
            appointmentEntity.getCustomerEntity();
            appointmentEntity.getBusinessCategoryEntity();
            appointmentEntity.getServiceProviderEntity();
            return appointmentEntity;
        }
        else
        {
            throw new AppointmentNotFoundException("Appointment Id: " + appointmentId + " does not exist!");
        }
    }
    
    @Override
    public AppointmentEntity retrieveAppointmentByAppointmentNumber(String appointmentNo) throws AppointmentNotFoundException {
   
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.appointmentNo = :inAppointmentNo");
        query.setParameter("inAppointmentNo", appointmentNo);
        
        try
        {
            AppointmentEntity appointmentEntity = (AppointmentEntity)query.getSingleResult();
            appointmentEntity.getCustomerEntity();
            appointmentEntity.getBusinessCategoryEntity();
            appointmentEntity.getServiceProviderEntity();
            return appointmentEntity;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AppointmentNotFoundException("Appointment number: " + appointmentNo + " does not exist!");
        }
    }
    
    
    @Override
    public List<AppointmentEntity> retrieveAppointmentsByDate(LocalDate date, String serviceProviderName)
    {
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.date = :inDate and a.serviceProviderEntity.name = :inServiceProviderName");
        query.setParameter("inDate", date);
        query.setParameter("inServiceProviderName", serviceProviderName);
        
        List<AppointmentEntity> apptList = query.getResultList();
        for (AppointmentEntity a : apptList)
        {
            a.getCustomerEntity();
            a.getBusinessCategoryEntity();
            a.getServiceProviderEntity();
        }
        return apptList;
    }
    
    @Override
    public List<AppointmentEntity> retrieveSortedAppointmentsByDate(LocalDate date, Long serviceProviderId) 
    {
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.scheduledDate = :inDate and a.serviceProviderEntity.serviceProviderId = :inServiceProviderId ORDER BY a.scheduledTime");
        query.setParameter("inDate", date);
        query.setParameter("inServiceProviderId", serviceProviderId);
        
        List<AppointmentEntity> apptList = query.getResultList();
        for (AppointmentEntity a : apptList)
        {
            a.getCustomerEntity();
            a.getBusinessCategoryEntity();
            a.getServiceProviderEntity();
        }
        return apptList;
    }


    @Override
    public void deleteAppointment(String appointmentNo) throws AppointmentNotFoundException
    {

        AppointmentEntity appointmentEntity = retrieveAppointmentByAppointmentNumber(appointmentNo);

        em.remove(appointmentEntity);
        em.flush();
        
        // are there any exception cases?
    }
    
    @Override
    public void cancelAppointment(String appointmentNo) throws AppointmentNotFoundException
    {

        AppointmentEntity appointmentEntity = retrieveAppointmentByAppointmentNumber(appointmentNo);
        appointmentEntity.setIsCancelled(Boolean.TRUE);
        
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AppointmentEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    @Override
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity)
    {
        em.merge(appointmentEntity);
    }
}

