/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.modulerun;

import java.util.Objects;

/**
 * The class modulerun represents the instance of a module
 */

public class ModuleRun {

    private Long id;
    private String semester; //can be autumn or spring
    private int year;
    private boolean running; //is either currently running or past
    private String moduleName; //Modulename of this modulerun is needed for displaying it
    private String moduleNumber; //Modulenumber of this modulerun is needed for displaying it
    private long moduleId; //id of corresponding module

    public ModuleRun() {
    }

    public ModuleRun(Long id, String semester, int year, boolean running,
                     String moduleName, String moduleNumber, long moduleId) {
        this.id = id;
        this.semester = semester;
        this.year = year;
        this.running = running;
        this.moduleName = moduleName;
        this.moduleNumber = moduleNumber;
        this.moduleId = moduleId;
    }

    public ModuleRun(long moduleId, String semester, int year, boolean running,
                     String moduleName, String moduleNumber) {
        this.moduleId = moduleId;
        this.semester = semester;
        this.year = year;
        this.running = running;
        this.moduleName = moduleName;
        this.moduleNumber = moduleNumber;
    }

    public ModuleRun(long moduleId, String semester, int year, boolean running) {
        this.moduleId = moduleId;
        this.semester = semester;
        this.year = year;
        this.running = running;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean getRunning() {
        return running;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleNumber() {
        return moduleNumber;
    }

    public void setModuleNumber(String moduleNumber) {
        this.moduleNumber = moduleNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleRun moduleRun = (ModuleRun) o;
        return id.equals(moduleRun.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ModuleRun{"
                + "id=" + id
                + ", semester='" + semester + '\''
                + ", year=" + year
                + ", running=" + running
                + ", moduleName='" + moduleName + '\''
                + ", moduleNumber='" + moduleNumber + '\''
                + ", moduleId=" + moduleId
                + '}';
    }
}
