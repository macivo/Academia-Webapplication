/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.teach;

/**
 * The class Teaching combines a teacher with the module run
 * he is teaching, personal id, last and first name and the teaching id
 */

public class Teaching {
    private Long teachingId;
    private Long modulerunId;
    private Long personalId;
    private String profFirstname;
    private String profLastname;

    public Teaching() {
    }

    public Teaching(Long modulerunId, Long personalId) {
        this.modulerunId = modulerunId;
        this.personalId = personalId;
    }

    public Long getTeachingId() {
        return teachingId;
    }

    public void setTeachingId(Long teachingId) {
        this.teachingId = teachingId;
    }

    public Long getModulerunId() {
        return modulerunId;
    }

    public void setModulerunId(Long modulerunId) {
        this.modulerunId = modulerunId;
    }

    public Long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Long personalId) {
        this.personalId = personalId;
    }

    public String getProfFirstname() {
        return profFirstname;
    }

    public void setProfFirstname(String profFirstname) {
        this.profFirstname = profFirstname;
    }

    public String getProfLastname() {
        return profLastname;
    }

    public void setProfLastname(String profLastname) {
        this.profLastname = profLastname;
    }
}
