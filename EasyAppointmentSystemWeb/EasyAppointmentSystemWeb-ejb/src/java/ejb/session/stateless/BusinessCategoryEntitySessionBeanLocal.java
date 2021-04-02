package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import util.exception.CreateNewBusinessCategoryException;


public interface BusinessCategoryEntitySessionBeanLocal {
    
     public List<BusinessCategoryEntity> retrieveAllBusinessCategories();

     public BusinessCategoryEntity createNewBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws CreateNewBusinessCategoryException;
     
}
