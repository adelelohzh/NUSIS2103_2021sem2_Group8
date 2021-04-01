package ejb.session.stateless;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local(CustomerEntitySessionBeanLocal.class)
@Remote(CustomerEntitySessionBeanRemote.class)
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal 
{

    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;

    
}
