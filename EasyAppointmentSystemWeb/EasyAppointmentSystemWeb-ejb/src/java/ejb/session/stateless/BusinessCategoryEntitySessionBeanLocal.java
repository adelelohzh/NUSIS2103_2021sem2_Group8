package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.CreateNewBusinessCategoryException;


public interface BusinessCategoryEntitySessionBeanLocal {
    
     public List<BusinessCategoryEntity> retrieveAllBusinessCategories();

     //public BusinessCategoryEntity createNewBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws CreateNewBusinessCategoryException;
     
     public String createNewBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws CreateNewBusinessCategoryException; 
             
     public void deleteBusinessCategory(String businessCategory) throws BusinessCategoryNotFoundException;
             
}
