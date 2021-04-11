package ejb.session.stateful;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import entity.AppointmentEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;


@Stateful
@Local(BookingSessionBeanLocal.class)
@Remote(BookingSessionBeanRemote.class)
public class BookingSessionBean implements BookingSessionBeanRemote, BookingSessionBeanLocal 
{

    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;
   
    private Integer totalQuantity;    
    
    public BookingSessionBean() 
    {
        initialiseState();
    }
    
    
    
    @Remove
    public void remove()
    {        
    }
    
    
    
    private void initialiseState()
    {
        totalQuantity = 0;
    }
    
    @Override
    public Integer addAppointment(AppointmentEntity appointmentEntity, Integer quantity)
    {
        totalQuantity += quantity;
        
        return totalQuantity;
    }
    
//    @Override
//    public AppointmentEntity doBooking(Long customerId) 
//    {
//        AppointmentEntity newAppointmentEntity = appointmentEntitySessionBeanLocal.createNewAppointment(customerId, new AppointmentEntity());
//        initialiseState();
//        
//        return newAppointmentEntity;
//    }
    
    @Override
    public void clearShoppingCart()
    {
        initialiseState();
    }
    
    @Override
    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    @Override
    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public AppointmentEntity doBooking(Long customerId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
