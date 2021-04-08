/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.AppointmentEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jieyingma
 */
@Local
public interface BookingSessionBeanLocal {
    
    public List<AppointmentEntity> getAppointmentEntities();

    public void setAppointmentEntities(List<AppointmentEntity> appointmentEntities);

    public Integer getTotalAppointments();

    public void setTotalAppointments(Integer totalAppointments);
}
