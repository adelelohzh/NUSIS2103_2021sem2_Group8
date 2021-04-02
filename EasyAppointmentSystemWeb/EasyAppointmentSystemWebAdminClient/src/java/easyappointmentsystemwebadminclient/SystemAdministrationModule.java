package easyappointmentsystemwebadminclient;

import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;


public class SystemAdministrationModule {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    
    private AdminEntity currentAdminEntity;

    
    
    public SystemAdministrationModule()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    
    
    public SystemAdministrationModule(CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, AdminEntity currentAdminEntity) 
    {
        this();
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentAdminEntity = currentAdminEntity;
    }
    
    
}
    