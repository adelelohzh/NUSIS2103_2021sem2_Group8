package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;


public interface BusinessCategoryEntitySessionBeanLocal {
    
     public List<BusinessCategoryEntity> retrieveAllBusinessCategories();

}
