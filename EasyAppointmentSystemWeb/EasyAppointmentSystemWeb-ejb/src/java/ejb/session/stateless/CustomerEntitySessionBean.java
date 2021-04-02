package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CustomerNotFoundException;

@Stateless
@Local(CustomerEntitySessionBeanLocal.class)
@Remote(CustomerEntitySessionBeanRemote.class)
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal 
{

    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;

    @Override
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException
    {   
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);

        if (customer != null)
        {
            return customer;
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID: " + customerId + " does not exist!");
        }
    }
}
