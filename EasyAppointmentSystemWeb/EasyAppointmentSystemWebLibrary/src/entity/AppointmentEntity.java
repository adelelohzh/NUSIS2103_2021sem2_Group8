package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AppointmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long appointmentId;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledTime;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customerEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ServiceProviderEntity serviceProviderEntity;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private BusinessCategoryEntity businessCategoryEntity;

    public AppointmentEntity() 
    {
    }

    public AppointmentEntity(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    
    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
         if(this.customerEntity != null)
        {
            this.customerEntity.getAppointmentEntities().remove(this);
        }
        
        this.customerEntity = customerEntity;
        
        if(this.customerEntity != null)
        {
            if(!this.customerEntity.getAppointmentEntities().contains(this))
            {
                this.customerEntity.getAppointmentEntities().add(this);
            }
        }
    }

    public ServiceProviderEntity getServiceProviderEntity() {
        return serviceProviderEntity;
    }

    public void setServiceProviderEntity(ServiceProviderEntity serviceProviderEntity) {
        if(this.serviceProviderEntity != null)
        {
            this.serviceProviderEntity.getAppointmentEntities().remove(this);
        }
        
        this.serviceProviderEntity = serviceProviderEntity;
        
        if(this.serviceProviderEntity != null)
        {
            if(!this.serviceProviderEntity.getAppointmentEntities().contains(this))
            {
                this.serviceProviderEntity.getAppointmentEntities().add(this);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appointmentId != null ? appointmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the appointmentId fields are not set
        if (!(object instanceof AppointmentEntity)) {
            return false;
        }
        AppointmentEntity other = (AppointmentEntity) object;
        if ((this.appointmentId == null && other.appointmentId != null) || (this.appointmentId != null && !this.appointmentId.equals(other.appointmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AppointmentEntity[ id=" + appointmentId + " ]";
    }
    
}
