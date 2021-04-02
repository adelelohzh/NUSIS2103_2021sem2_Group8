package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Local(BusinessCategoryEntitySessionBeanLocal.class)
@Remote(BusinessCategoryEntitySessionBeanRemote.class)
public class BusinessCategoryEntitySessionBean implements BusinessCategoryEntitySessionBeanRemote, BusinessCategoryEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;
    
    public List<BusinessCategoryEntity> retrieveAllBusinessCategories()
    {
        Query query = em.createQuery("SELECT b FROM BusinessCategoryEntity s");
        
        return query.getResultList();
    }
    
}
