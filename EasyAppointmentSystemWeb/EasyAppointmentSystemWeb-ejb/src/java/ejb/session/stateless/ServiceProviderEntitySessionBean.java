package ejb.session.stateless;

import entity.ServiceProviderEntity;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
import util.enumeration.StatusEnum;
import util.exception.DeleteServiceProviderException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ServiceProviderBlockedException;
import util.exception.ServiceProviderEmailExistException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateServiceProviderException;

@Stateless
@Local(ServiceProviderEntitySessionBeanLocal.class)
@Remote(ServiceProviderEntitySessionBeanRemote.class)
public class ServiceProviderEntitySessionBean implements ServiceProviderEntitySessionBeanRemote, ServiceProviderEntitySessionBeanLocal 
{
    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ServiceProviderEntitySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewServiceProvider(ServiceProviderEntity newServiceProviderEntity) throws ServiceProviderEmailExistException, UnknownPersistenceException, InputDataValidationException
    {
       try
        {
            Set<ConstraintViolation<ServiceProviderEntity>>constraintViolations = validator.validate(newServiceProviderEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newServiceProviderEntity);
                em.flush();

                return newServiceProviderEntity.getServiceProviderId();
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
                    throw new ServiceProviderEmailExistException(); // or isit biz reg number
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
    public ServiceProviderEntity doServiceProviderLogin(String email, String password) throws InvalidLoginCredentialException
    {
        try
        {
            ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderEntityByEmail(email);
            
            if(serviceProviderEntity.getPassword().equals(password))
            {
                serviceProviderEntity.getAppointmentEntities().size();                
                return serviceProviderEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(ServiceProviderNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
               
    @Override
    public List<ServiceProviderEntity> retrieveAllServiceProviderEntity()
    {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s");
        
        return query.getResultList();
    }
    
    
    @Override
    public ServiceProviderEntity retrieveServiceProviderEntityByEmail(String email) throws ServiceProviderNotFoundException
    {   
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.emailAddress = :inEmail and s.statusEnum = util.enumeration.StatusEnum.Approved");
        query.setParameter("inEmail", email);

        try
        {
            ServiceProviderEntity serviceProviderEntity = (ServiceProviderEntity)query.getSingleResult();
            serviceProviderEntity.getAppointmentEntities().size();
            return serviceProviderEntity;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new ServiceProviderNotFoundException("Service Provider Email " + email + " does not exist!");
        }
    }
    
     @Override
    public ServiceProviderEntity retrieveServiceProviderEntityByName(String name) throws ServiceProviderNotFoundException
    {   
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.name = :inName and s.statusEnum = util.enumeration.StatusEnum.Approved");
        query.setParameter("inName", name);
        
        try
        {
            ServiceProviderEntity serviceProviderEntity = (ServiceProviderEntity)query.getSingleResult();
            serviceProviderEntity.getAppointmentEntities().size();
            return serviceProviderEntity;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new ServiceProviderNotFoundException("Service Provider Name " + name + " does not exist!");
        }
    }
    
    @Override
    public List<ServiceProviderEntity> retrieveServiceProviderEntityBySearch(String businessCategory, String city) throws ServiceProviderNotFoundException
    {   
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.city = :inCity and s.businessCategory = :inBusinessCategory and s.statusEnum = util.enumeration.StatusEnum.Approved");
        query.setParameter("inCity", city);
        query.setParameter("inBusinessCategory", businessCategory);

        
        return query.getResultList();    
    }
    
    
    @Override
    public ServiceProviderEntity retrieveServiceProviderEntityById(Long serviceProviderId) throws ServiceProviderNotFoundException, ServiceProviderBlockedException
    {   
        ServiceProviderEntity serviceProviderEntity = em.find(ServiceProviderEntity.class, serviceProviderId);

        if (serviceProviderEntity != null)
        {
            serviceProviderEntity.getAppointmentEntities().size();
            return serviceProviderEntity;
        }
        else if (serviceProviderEntity.getStatusEnum().equals(StatusEnum.Blocked))
        {
            throw new ServiceProviderBlockedException("Serivce Provider ID: " + serviceProviderId + " is blocked!");
        }
        {
            throw new ServiceProviderNotFoundException("Serivce Provider ID: " + serviceProviderId + " does not exist!");
        }
    }
    
    @Override
    public void updateServiceProvider(ServiceProviderEntity serviceProviderEntity) throws UpdateServiceProviderException, ServiceProviderNotFoundException, InputDataValidationException
    {
        String serviceProviderEmail = serviceProviderEntity.getEmailAddress();
        
        if (serviceProviderEntity != null && serviceProviderEmail != null)
        {
            
            Set<ConstraintViolation<ServiceProviderEntity>>constraintViolations = validator.validate(serviceProviderEntity);
            
            if (constraintViolations.isEmpty())
            {
                ServiceProviderEntity serviceProviderToUpdate = retrieveServiceProviderEntityByEmail(serviceProviderEmail);
                
                if (serviceProviderToUpdate.getServiceProviderId() != serviceProviderEntity.getServiceProviderId())
                {
                    serviceProviderToUpdate.setAppointmentEntities(serviceProviderEntity.getAppointmentEntities());
                    serviceProviderToUpdate.setBusinessAddress(serviceProviderEntity.getBusinessAddress());
                    serviceProviderToUpdate.setBusinessCategory(serviceProviderEntity.getBusinessCategory());
                    serviceProviderToUpdate.setBusinessRegistrationNumber(serviceProviderEntity.getBusinessRegistrationNumber());
                    serviceProviderToUpdate.setCity(serviceProviderEntity.getCity());
                    serviceProviderToUpdate.setEmailAddress(serviceProviderEntity.getEmailAddress());
                    serviceProviderToUpdate.setName(serviceProviderEntity.getName());
                    serviceProviderToUpdate.setPassword(serviceProviderEntity.getPassword());
                    serviceProviderToUpdate.setPhoneNumber(serviceProviderEntity.getPhoneNumber());
                }
                else
                {
                    throw new UpdateServiceProviderException("Service Provider ID does not match records!");
                }
            }
            else 
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else    
        { 
            throw new ServiceProviderNotFoundException("Staff ID not provided for staff to be updated");
        }
     
    }
    
    @Override
    public void deleteServiceProvider(Long serivceProviderId) throws ServiceProviderNotFoundException, DeleteServiceProviderException, ServiceProviderBlockedException
    {
        try
        {
            ServiceProviderEntity serviceProvider = retrieveServiceProviderEntityById(serivceProviderId);
            if (serviceProvider.getAppointmentEntities().isEmpty())
            {
                em.remove(serviceProvider);
                em.flush();
            }
            else 
            {
                throw new DeleteServiceProviderException("Service Provider ID " + serivceProviderId + " is associated with existing Appointment record(s) and cannot be deleted!");
            }
        }
        catch (ServiceProviderNotFoundException ex)
        {
            System.out.println("Service Provider Record cannot be found!");
        }
    }
    
    @Override
    public void approveServiceProvider(Long serviceProviderId) throws ServiceProviderNotFoundException, ServiceProviderBlockedException
    {   
        ServiceProviderEntity serviceProvider = retrieveServiceProviderEntityById(serviceProviderId);
        serviceProvider.setStatusEnum(StatusEnum.Approved);

    }
    
    @Override
    public void blockServiceProvider(Long serviceProviderId) throws ServiceProviderNotFoundException, ServiceProviderBlockedException
    {   
        ServiceProviderEntity serviceProvider = retrieveServiceProviderEntityById(serviceProviderId);
        serviceProvider.setStatusEnum(StatusEnum.Blocked);
        

    }
    
    public void registerServiceProvider(String name, int category, String businessRegNumber, String city, String phone, String addr, String email, String password) throws ServiceProviderEmailExistException, UnknownPersistenceException, InputDataValidationException
    {
        String businessCategory = "";
        
        if (category == 1)
        {
            businessCategory = "Health";
        }
        else if (category == 2)
        {
            businessCategory = "Fashion";
        }
        else 
        {
            businessCategory = "Education";
        }
        
        ServiceProviderEntity newServiceProvider = new ServiceProviderEntity();
        newServiceProvider.setName(name);
        newServiceProvider.setBusinessAddress(addr);
        newServiceProvider.setBusinessCategory(businessCategory);
        newServiceProvider.setBusinessRegistrationNumber(businessRegNumber);
        newServiceProvider.setCity(city);
        newServiceProvider.setPhoneNumber(phone);
        newServiceProvider.setEmailAddress(email);
        newServiceProvider.setPassword(password);
        
        createNewServiceProvider(newServiceProvider);
    }
    
    @Override
    public void updateRating(double newRating, Long serviceProviderId) throws ServiceProviderNotFoundException, ServiceProviderBlockedException
    {
        ServiceProviderEntity serviceProvider = retrieveServiceProviderEntityById(serviceProviderId);
        double currentRating = serviceProvider.getRating();
        int numberOfRatings = serviceProvider.getNumberOfRatings();
        double rating = (double)(currentRating * numberOfRatings + newRating)/(numberOfRatings+1);
        serviceProvider.setNumberOfRatings(numberOfRatings+1);
        serviceProvider.setRating(rating);
    }
     
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ServiceProviderEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
    
}
