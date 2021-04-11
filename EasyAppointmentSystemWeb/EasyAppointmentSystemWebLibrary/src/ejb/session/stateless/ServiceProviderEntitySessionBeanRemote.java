package ejb.session.stateless;

import entity.ServiceProviderEntity;
import java.util.Date;
import java.util.List;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.DeleteServiceProviderException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ServiceProviderBlockedException;
import util.exception.ServiceProviderEmailExistException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateServiceProviderException;

public interface ServiceProviderEntitySessionBeanRemote 
{
    public Long createNewServiceProvider(String businessCategoryName, ServiceProviderEntity newServiceProviderEntity) throws ServiceProviderEmailExistException, UnknownPersistenceException, InputDataValidationException, BusinessCategoryNotFoundException;
    
    public ServiceProviderEntity doServiceProviderLogin(String email, String password) throws InvalidLoginCredentialException, ServiceProviderBlockedException;

    public List<ServiceProviderEntity> retrieveAllServiceProviderEntity();

    public ServiceProviderEntity retrieveServiceProviderEntityByEmail(String email) throws ServiceProviderNotFoundException;

    public ServiceProviderEntity retrieveServiceProviderEntityById(Long serviceProviderId) throws ServiceProviderNotFoundException, ServiceProviderBlockedException;

    public void updateServiceProvider(ServiceProviderEntity serviceProviderEntity) throws UpdateServiceProviderException, ServiceProviderNotFoundException, InputDataValidationException;
     
    public void deleteServiceProvider(Long serivceProviderId) throws ServiceProviderNotFoundException, DeleteServiceProviderException, ServiceProviderBlockedException;

    public void registerServiceProvider(String name, int category, String businessRegNumber, String city, String phone, String addr, String email, String password) throws ServiceProviderEmailExistException, UnknownPersistenceException, InputDataValidationException;
    
    public List<ServiceProviderEntity> retrieveServiceProviderEntityBySearch(String businessCategory, String city) throws ServiceProviderNotFoundException;
    
    public ServiceProviderEntity retrieveServiceProviderEntityByName(String name) throws ServiceProviderNotFoundException;

    public void updateRating(double newRating, Long serviceProviderId) throws ServiceProviderNotFoundException, ServiceProviderBlockedException;

    public void approveServiceProvider(Long serviceProviderId) throws ServiceProviderNotFoundException, ServiceProviderBlockedException;

    public void blockServiceProvider(Long serviceProviderId) throws ServiceProviderNotFoundException, ServiceProviderBlockedException;
}
