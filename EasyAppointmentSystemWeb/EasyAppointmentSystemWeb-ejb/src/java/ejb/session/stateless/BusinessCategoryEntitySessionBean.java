package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.BusinessCategoryExistException;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

@Stateless
@Local(BusinessCategoryEntitySessionBeanLocal.class)
@Remote(BusinessCategoryEntitySessionBeanRemote.class)
public class BusinessCategoryEntitySessionBean implements BusinessCategoryEntitySessionBeanRemote, BusinessCategoryEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public BusinessCategoryEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    @Override
    public String createNewBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws BusinessCategoryExistException, UnknownPersistenceException, InputDataValidationException
    {
        try
        {
            Set<ConstraintViolation<BusinessCategoryEntity>>constraintViolations = validator.validate(newBusinessCategoryEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newBusinessCategoryEntity);
                em.flush();

                return newBusinessCategoryEntity.getCategory();
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
                    throw new BusinessCategoryExistException();
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
    public List<BusinessCategoryEntity> retrieveAllBusinessCategories() {
        Query query = em.createQuery("SELECT b FROM BusinessCategoryEntity s");

        return query.getResultList();
    }

    @Override
    public void deleteBusinessCategory(String businessCategory) throws BusinessCategoryNotFoundException {

        boolean exist = false;
        List<BusinessCategoryEntity> businessCategoryEntities = retrieveAllBusinessCategories();
        for (BusinessCategoryEntity currentBusinessCategoryEntity : businessCategoryEntities) {
            if (currentBusinessCategoryEntity.getCategory().equals(businessCategory)) {
                em.remove(businessCategory);
                em.flush();
                exist = true;
                break;
            }
        }
        if (exist == false) {
            throw new BusinessCategoryNotFoundException("Business Category not found!");
        }
    }
    
         private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<BusinessCategoryEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
