package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import util.exception.BusinessCategoryExistException;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.CreateNewBusinessCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;


public interface BusinessCategoryEntitySessionBeanRemote {
    
    public List<BusinessCategoryEntity> retrieveAllBusinessCategories();

    //public BusinessCategoryEntity createNewBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws CreateNewBusinessCategoryException;

    public String createNewBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws CreateNewBusinessCategoryException, BusinessCategoryExistException, UnknownPersistenceException, InputDataValidationException;
    
    public void deleteBusinessCategory(String businessCategory) throws BusinessCategoryNotFoundException;
}
