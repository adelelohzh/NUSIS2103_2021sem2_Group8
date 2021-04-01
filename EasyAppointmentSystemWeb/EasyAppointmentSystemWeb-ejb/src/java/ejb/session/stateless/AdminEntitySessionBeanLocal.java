package ejb.session.stateless;

import entity.AdminEntity;
import java.util.List;

public interface AdminEntitySessionBeanLocal {
    
    public List<AdminEntity> retrieveAllAdmins();
    
}
