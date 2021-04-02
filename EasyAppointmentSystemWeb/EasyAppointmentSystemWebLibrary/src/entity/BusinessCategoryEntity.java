/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author adele
 */
@Entity
public class BusinessCategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long businessCategoryId;

    @Column(nullable = false, length = 32)
    private String category;
    
    @OneToMany(mappedBy = "businessCategoryEntity")
    private List<ServiceProviderEntity> serviceProviderEntities;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ServiceProviderEntity> getServiceProviderEntities() {
        return serviceProviderEntities;
    }

    public void setServiceProviderEntities(List<ServiceProviderEntity> serviceProviderEntities) {
        this.serviceProviderEntities = serviceProviderEntities;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (businessCategoryId != null ? businessCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the businessCategoryId fields are not set
        if (!(object instanceof BusinessCategoryEntity)) {
            return false;
        }
        BusinessCategoryEntity other = (BusinessCategoryEntity) object;
        if ((this.businessCategoryId == null && other.businessCategoryId != null) || (this.businessCategoryId != null && !this.businessCategoryId.equals(other.businessCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BusinessCategoryEntity[ id=" + businessCategoryId + " ]";
    }
    
}
