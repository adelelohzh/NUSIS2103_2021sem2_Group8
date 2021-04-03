package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import util.exception.CreateNewBusinessCategoryException;


public interface BusinessCategoryEntitySessionBeanRemote {
    
    public List<BusinessCategoryEntity> retrieveAllBusinessCategories();

    public BusinessCategoryEntity createNewBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws CreateNewBusinessCategoryException;
}
