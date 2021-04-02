package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;


public interface BusinessCategoryEntitySessionBeanRemote {
    
     public List<BusinessCategoryEntity> retrieveAllBusinessCategories();
}
