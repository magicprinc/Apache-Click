package org.springframework.samples.petclinic.model.auto;

import java.util.List;

/** Class _Vet was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _Vet extends org.springframework.samples.petclinic.model.Entity {

    public static final String FIRST_NAME_PROPERTY = "firstName";
    public static final String LAST_NAME_PROPERTY = "lastName";
    public static final String SPECIALTIES_PROPERTY = "specialties";

    public static final String ID_PK_COLUMN = "ID";

    public void setFirstName(String firstName) {
        writeProperty("firstName", firstName);
    }
    public String getFirstName() {
        return (String)readProperty("firstName");
    }
    
    
    public void setLastName(String lastName) {
        writeProperty("lastName", lastName);
    }
    public String getLastName() {
        return (String)readProperty("lastName");
    }
    
    
    public void addToSpecialties(org.springframework.samples.petclinic.model.Specialty obj) {
        addToManyTarget("specialties", obj, true);
    }
    public void removeFromSpecialties(org.springframework.samples.petclinic.model.Specialty obj) {
        removeToManyTarget("specialties", obj, true);
    }
    public List getSpecialties() {
        return (List)readProperty("specialties");
    }
    
    
}