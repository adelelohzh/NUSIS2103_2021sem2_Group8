package ejb.session.stateless;

import entity.CustomerEntity;
import java.util.List;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

public interface CustomerEntitySessionBeanRemote {

    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Long createNewCustomer(CustomerEntity newCustomerEntity) throws CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public List<CustomerEntity> retrieveAllCustomers();

    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException;

}
