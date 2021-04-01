package easyappointmentsystemwebadminclient;


public class Main 
{
    @EJB
    private static AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    @EJB
    private static AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    @EJB
    private static BusinessCategoryEntitySessionBeanRemote businessCategoryEntitySessionBeanRemote;
    @EJB
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    @EJB
    private static ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    @EJB
    private static EmailSessionBeanRemote emailSessionBeanRemote;
    
    @Resource(mappedName = "jms/queueCheckoutNotification")
    private static Queue queueCheckoutNotification;
    @Resource(mappedName = "jms/queueCheckoutNotificationFactory")
    private static ConnectionFactory queueCheckoutNotificationFactory;


    public static void main(String[] args) 
    {
        MainApp mainApp = new MainApp(adminEntitySessionBeanRemote, appointmentEntitySessionBeanRemote, businessCategoryEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote, emailSessionBeanRemote, queueCheckoutNotification, queueCheckoutNotificationFactory);
        mainApp.runApp();
    }
}
