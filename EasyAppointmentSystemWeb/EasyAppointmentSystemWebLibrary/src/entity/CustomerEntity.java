package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 32)
    private String identityNumber;
    
    @Column(nullable = false, length = 32)
    private String password;
    
    @Column(nullable = false, length = 32)
    private String firstName;
    
    @Column(nullable = false, length = 32)
    private String lastName;
    
    @Column(nullable = false, length = 32)
    private Character gender;
    
    @Column(nullable = false, length = 32)
    private Integer age;
    
    @Column(nullable = false, length = 32)
    private String phoneNumber;
    
    @Column(nullable = false, length = 32)
    private String address;
    
    @Column(nullable = false, length = 32)
    private String city;
    
    @Column(nullable = false, length = 32)
    private String emailAddress;

    public CustomerEntity() 
    {
    }

    public CustomerEntity(String identityNumber, String password, String firstName, String lastName, Character gender, Integer age, String phoneNumber, String address, String city, String emailAddress) 
    {
        this();
        this.identityNumber = identityNumber;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.emailAddress = emailAddress;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the identityNumber
     */
    public String getIdentityNumber() {
        return identityNumber;
    }

    /**
     * @param identityNumber the identityNumber to set
     */
    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
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

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the gender
     */
    public Character getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Character gender) {
        this.gender = gender;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
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
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + id + " ]";
    }
    
}
