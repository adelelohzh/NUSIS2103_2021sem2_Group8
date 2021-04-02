package ejb.session.stateless;

import entity.CustomerEntity;
import util.exception.CustomerNotFoundException;


public interface CustomerEntitySessionBeanLocal {

    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException;
    
}
