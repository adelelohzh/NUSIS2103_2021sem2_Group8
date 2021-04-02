package ejb.session.stateless;

import entity.ServiceProviderEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UpdateServiceProviderException;

@Stateless
@Local(ServiceProviderEntitySessionBeanLocal.class)
@Remote(ServiceProviderEntitySessionBeanRemote.class)
public class ServiceProviderEntitySessionBean implements ServiceProviderEntitySessionBeanRemote, ServiceProviderEntitySessionBeanLocal 
{
    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;
    
    public ServiceProviderEntitySessionBean()
    {
    }
    
    public Long createNewServiceProvider(ServiceProviderEntity newServiceProviderEntity)
    {
        em.persist(newServiceProviderEntity);
        em.flush();
        
        return newServiceProviderEntity.getServiceProviderId();
    }
               
    
    public List<ServiceProviderEntity> retrieveAllServiceProviderEntity()
    {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s");
        
        return query.getResultList();
    }
    
    public ServiceProviderEntity retrieveServiceProviderEntityById(Long serviceProviderId) throws ServiceProviderNotFoundException
    {   
        ServiceProviderEntity serviceProvider = em.find(ServiceProviderEntity.class, serviceProviderId);

        if (serviceProvider != null)
        {
            return serviceProvider;
        }
        else
        {
            throw new ServiceProviderNotFoundException("Serivce Provider ID" + serviceProviderId + " does not exist!");
        }
    }
    
    public void updateServiceProvider(ServiceProviderEntity serviceProviderEntity) throws UpdateServiceProviderException
    {
        Long serviceProviderId = serviceProviderEntity.getServiceProviderId();
        
        if (serviceProviderEntity != null && serviceProviderId != null)
        {
            try 
            {
                ServiceProviderEntity serviceProviderToUpdate = retrieveServiceProviderEntityById(serviceProviderId);
                
                if (serviceProviderToUpdate.getServiceProviderId() != serviceProviderEntity.getServiceProviderId())
                {
                    serviceProviderToUpdate.setAppointmentEntities(serviceProviderEntity.getAppointmentEntities());
                    serviceProviderToUpdate.setBusinessAddress(serviceProviderEntity.getBusinessAddress());
                    serviceProviderToUpdate.setBusinessCategory(serviceProviderEntity.getBusinessCategory());
                    serviceProviderToUpdate.setBusinessRegistrationNumber(serviceProviderEntity.getBusinessRegistrationNumber());
                    serviceProviderToUpdate.setCity(serviceProviderEntity.getCity());
                    serviceProviderToUpdate.setEmailAddress(serviceProviderEntity.getEmailAddress());
                    serviceProviderToUpdate.setName(serviceProviderEntity.getName());
                    serviceProviderToUpdate.setPassword(serviceProviderEntity.getPassword());
                    serviceProviderToUpdate.setPhoneNumber(serviceProviderEntity.getPhoneNumber());
                }
                else
                {
                    throw new UpdateServiceProviderException("Service Provider ID does not match records!");
                }
            }
            catch (ServiceProviderNotFoundException ex)
            { 
                 System.out.println("Service Provider Record cannot be found!");
            }
        }
    }
    
    public void deleteServiceProvider(Long serivceProviderId) throws ServiceProviderNotFoundException
    {
        try
        {
            ServiceProviderEntity serviceProvider = retrieveServiceProviderEntityById(serivceProviderId);
            em.remove(serviceProvider);
            em.flush();
        }
        catch (ServiceProviderNotFoundException ex)
        {
            System.out.println("Service Provider Record cannot be found!");
        }
              
        
    }
    
    
    
}
