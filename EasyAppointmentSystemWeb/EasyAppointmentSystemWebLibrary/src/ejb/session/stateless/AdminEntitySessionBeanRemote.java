package ejb.session.stateless;

import entity.AdminEntity;
import java.util.List;
import util.exception.InvalidLoginCredentialException;

public interface AdminEntitySessionBeanRemote {

    public List<AdminEntity> retrieveAllAdmins();

    public AdminEntity adminLogin(String username, String password) throws InvalidLoginCredentialException;;
    
}
