package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.CreateNewBusinessCategoryException;

@Stateless
@Local(BusinessCategoryEntitySessionBeanLocal.class)
@Remote(BusinessCategoryEntitySessionBeanRemote.class)
public class BusinessCategoryEntitySessionBean implements BusinessCategoryEntitySessionBeanRemote, BusinessCategoryEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;

    public List<BusinessCategoryEntity> retrieveAllBusinessCategories() {
        Query query = em.createQuery("SELECT b FROM BusinessCategoryEntity s");

        return query.getResultList();
    }

    public BusinessCategoryEntity createNewBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws CreateNewBusinessCategoryException {
        if (newBusinessCategoryEntity != null) {
            em.persist(newBusinessCategoryEntity);
            em.flush();
            return newBusinessCategoryEntity;
        } else {
            throw new CreateNewBusinessCategoryException("Business Category not created!");
        }
    }

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
}
