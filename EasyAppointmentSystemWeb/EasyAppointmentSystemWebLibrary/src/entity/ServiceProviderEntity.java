package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ServiceProviderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 32)
    private String name;
    
    @Column(nullable = false, length = 32)
    private String businessCategory;
    
    @Column(nullable = false, length = 32)
    private String businessRegistrationNumber;
    
    @Column(nullable = false, length = 32)
    private String city;
    
    @Column(nullable = false, length = 32)
    private String businessAddress;
    
    @Column(nullable = false, length = 32)
    private String emailAddress;
    
    @Column(nullable = false, length = 32)
    private String phoneNumber;
    
    @Column(nullable = false, length = 32)
    private String password;

    public ServiceProviderEntity() 
    {
    }

    public ServiceProviderEntity(String name, String businessCategory, String businessRegistrationNumber, String city, String businessAddress, String emailAddress, String phoneNumber, String password) 
    {
        this();
        this.name = name;
        this.businessCategory = businessCategory;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.city = city;
        this.businessAddress = businessAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the businessCategory
     */
    public String getBusinessCategory() {
        return businessCategory;
    }

    /**
     * @param businessCategory the businessCategory to set
     */
    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    /**
     * @return the businessRegistrationNumber
     */
    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    /**
     * @param businessRegistrationNumber the businessRegistrationNumber to set
     */
    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the businessAddress
     */
    public String getBusinessAddress() {
        return businessAddress;
    }

    /**
     * @param businessAddress the businessAddress to set
     */
    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
        @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceProviderEntity)) {
            return false;
        }
        ServiceProviderEntity other = (ServiceProviderEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ServiceProviderEntity[ id=" + id + " ]";
    }
    
}
