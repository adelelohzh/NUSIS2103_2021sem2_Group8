package ejb.session.stateless;

import entity.CustomerEntity;
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
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

@Stateless
@Local(CustomerEntitySessionBeanLocal.class)
@Remote(CustomerEntitySessionBeanRemote.class)
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal 
{

    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewCustomer(CustomerEntity newCustomerEntity) throws CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException
    {
        try
        {
            Set<ConstraintViolation<CustomerEntity>>constraintViolations = validator.validate(newCustomerEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newCustomerEntity);
                em.flush();

                return newCustomerEntity.getCustomerId();
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
                    throw new CustomerUsernameExistException();
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
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException
    {   
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);

        if (customer != null)
        {
            return customer;
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID: " + customerId + " does not exist!");
        }
    }
    
    @Override
    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException
    {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);
        
        try
        {
            return (CustomerEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer Email " + email + " does not exist!");
        }
    }
    
    @Override
    public List<CustomerEntity> retrieveAllCustomers()
    {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c");
        
        return query.getResultList();
    }
    
    @Override
    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException
    {
        try
        {
            CustomerEntity customerEntity = retrieveCustomerByEmail(email);
            
            if(customerEntity.getPassword().equals(password))
            {
                customerEntity.getAppointmentEntities().size();                
                return customerEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
            }
        }
        catch(CustomerNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
}
