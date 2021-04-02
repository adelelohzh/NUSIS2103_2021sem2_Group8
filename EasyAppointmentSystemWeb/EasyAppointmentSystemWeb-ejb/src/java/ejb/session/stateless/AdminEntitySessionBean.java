package ejb.session.stateless;

import entity.AdminEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
@Local(AdminEntitySessionBeanLocal.class)
@Remote(AdminEntitySessionBeanRemote.class)
public class AdminEntitySessionBean implements AdminEntitySessionBeanRemote, AdminEntitySessionBeanLocal 
{
    
    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;
    
    @Override
    public List<AdminEntity> retrieveAllAdmins()
    {
        Query query = em.createQuery("SELECT a FROM AdminEntity a");
        
        return query.getResultList();
    }
    
    
   
}
