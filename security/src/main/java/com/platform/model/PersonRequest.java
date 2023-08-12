package com.platform.model;

import com.platform.aspect.annotation.Mask;
import java.time.LocalDate;


public class PersonRequest extends PlatformClientRequest {

    @Mask
    private String familyName;
    @Mask
    private String givenName;
    @Mask
    private String middleName;
    private LocalDate birthDate;

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
