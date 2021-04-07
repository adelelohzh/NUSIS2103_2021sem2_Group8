/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemwebcustomerclient;

import ejb.session.stateless.BusinessCategoryEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import entity.CustomerEntity;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author adele
 */
public class SystemAdministrationModule 
{
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote;
    private EmailSessionBeanRemote emailSessionBeanRemote;

    private AdminEntity currentAdminEntity;

    private Queue queueCheckoutNotification;
    private ConnectionFactory queueCheckoutNotificationFactory;

    public SystemAdministrationModule() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public SystemAdministrationModule(CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, CustomerEntity currentCustomerEntity, Queue queueCheckoutNotification, ConnectionFactory queueCheckoutNotificationFactory) {
        this();
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentAdminEntity = currentAdminEntity;
        this.queueCheckoutNotification = queueCheckoutNotification;
        this.queueCheckoutNotificationFactory = queueCheckoutNotificationFactory;
    }
    
}
