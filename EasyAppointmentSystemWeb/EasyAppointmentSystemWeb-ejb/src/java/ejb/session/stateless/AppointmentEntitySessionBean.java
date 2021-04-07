package ejb.session.stateless;

import entity.AppointmentEntity;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AppointmentNotFoundException;


@Stateless
@Local(AppointmentEntitySessionBeanLocal.class)
@Remote(AppointmentEntitySessionBeanRemote.class)
public class AppointmentEntitySessionBean implements AppointmentEntitySessionBeanRemote, AppointmentEntitySessionBeanLocal 
{

    @PersistenceContext(unitName = "EasyAppointmentSystemWeb-ejbPU")
    private EntityManager em;
    
    @Override
    public AppointmentEntity retrieveAppointmentByCustomerID(Long customerID) throws AppointmentNotFoundException {
   
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.customerID = :inCustomerID");
        query.setParameter("inCustomerID", customerID);
        
        try
        {
            return (AppointmentEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AppointmentNotFoundException("Appointment of customer " + customerID + " does not exist!");
        }
    }
    
    @Override
    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId) throws AppointmentNotFoundException {
   
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.appointmentId = :inAppointmentId");
        query.setParameter("inAppointmentId", appointmentId);
        
        try
        {
            return (AppointmentEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AppointmentNotFoundException("Appointment Id: " + appointmentId + " does not exist!");
        }
    }
    
    @Override
    public AppointmentEntity retrieveAppointmentByAppointmentNumber(String appointmentNo) throws AppointmentNotFoundException {
   
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.appointmentNo = :inAppointmentNo");
        query.setParameter("inAppointmentNo", appointmentNo);
        
        try
        {
            return (AppointmentEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new AppointmentNotFoundException("Appointment number: " + appointmentNo + " does not exist!");
        }
    }
    
    @Override
    public List<AppointmentEntity> retrieveAppointmentsByDate(Date date, String serviceProviderName)
    {
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.date = :inDate and a.serviceProviderEntity.name := inServiceProviderName");
        query.setParameter("inDate", date);
        query.setParameter("inServiceProviderName", serviceProviderName);
        
        return query.getResultList();
    }
    
    @Override
    public List<AppointmentEntity> retrieveSortedAppointmentsByDate(Date date, Long serviceProviderId) {
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.date = :inDate and a.serviceProviderEntityId := inServiceProviderId ORDER BY a.time");
        query.setParameter("inDate", date);
        query.setParameter("inServiceProviderId", serviceProviderId);
        
        return query.getResultList();
    }


    @Override
    public void deleteAppointment(Long appointmentId) throws AppointmentNotFoundException
    {

        AppointmentEntity appointmentEntity = retrieveAppointmentByAppointmentId(appointmentId);

        em.remove(appointmentEntity);
        em.flush();
        
        // are there any exception cases?
    }
    
    
}
