package ejb.session.stateless;

import entity.AdminEntity;
import java.util.List;
import util.exception.AdminNotFoundException;
import util.exception.InvalidLoginCredentialException;

public interface AdminEntitySessionBeanLocal {
    
    public List<AdminEntity> retrieveAllAdmins();
    
    public AdminEntity adminLogin(String email, String password) throws InvalidLoginCredentialException;

    public AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException;
    
}
