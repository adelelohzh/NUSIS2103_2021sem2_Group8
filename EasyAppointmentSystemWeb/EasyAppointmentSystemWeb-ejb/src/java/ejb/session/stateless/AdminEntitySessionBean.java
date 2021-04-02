package ejb.session.stateless;

import entity.AdminEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AdminNotFoundException;
import util.exception.InvalidLoginCredentialException;


@Stateless
@Local(AdminEntitySessionBeanLocal.class)
@Remote(AdminEntitySessionBeanRemote.class)
public class AdminEntitySessionBean implements AdminEntitySessionBeanRemote, AdminEntitySessionBeanLocal 
{
    
    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;
    
    
    @Override
    public AdminEntity adminLogin(String email, String password) throws InvalidLoginCredentialException
    {
        try
        {
            AdminEntity adminEntity = retrieveAdminByEmail(email);
            
            if(adminEntity.getPassword().equals(password))
            {
                //adminEntity.getServiceProviderEntities().size();                
                return adminEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Email Address does not exist or invalid password!");
            }
        }
        catch(AdminNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public List<AdminEntity> retrieveAllAdmins()
    {
        Query query = em.createQuery("SELECT a FROM AdminEntity a");
        
        return query.getResultList();
    }

    
    @Override
    public AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException {
   
        Query query = em.createQuery("SELECT a FROM AdminEntity a WHERE a.email = :inEmail");
        query.setParameter("inEmail", email);
        
        try
        {
            return (AdminEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AdminNotFoundException("Admin Email Address " + email + " does not exist!");
        }
    }
    
   
}
