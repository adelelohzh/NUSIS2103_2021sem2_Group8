package ejb.session.stateful;

import entity.AppointmentEntity;


public interface BookingSessionBeanLocal {

    public Integer addAppointment(AppointmentEntity appointmentEntity, Integer quantity);

    public AppointmentEntity doBooking(Long customerId);

    public void clearShoppingCart();

    public Integer getTotalQuantity();

    public void setTotalQuantity(Integer totalQuantity);
    
}
