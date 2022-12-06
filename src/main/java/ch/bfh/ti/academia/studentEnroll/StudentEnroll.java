/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.studentEnroll;

/**
 * The class StudentEnroll represents a running module with the teacher.
 */

public class StudentEnroll {

    private Long moduleId;
    private String moduleNumber;
    private String name;
    private int ects;
    private Long modulerunId;
    private String semester;
    private int year;
    private boolean running;
    private String teacherFirstname;
    private String teacherLastname;
    private Long studentId;

    public StudentEnroll() {
    }

    public StudentEnroll(Long moduleId, String moduleNumber, String name, int ects, Long modulerunId, String semester,
                         int year, boolean running, String teacherFirstname, String teacherLastname, Long studentId) {
        this.moduleId = moduleId;
        this.moduleNumber = moduleNumber;
        this.name = name;
        this.ects = ects;
        this.modulerunId = modulerunId;
        this.semester = semester;
        this.year = year;
        this.running = running;
        this.teacherFirstname = teacherFirstname;
        this.teacherLastname = teacherLastname;
        this.studentId = studentId;
    }

    public StudentEnroll(Long moduleId, String moduleNumber, String name, int ects, Long modulerunId, String semester,
                         int year, boolean running, String teacherFirstname, String teacherLastname) {
        this.moduleId = moduleId;
        this.moduleNumber = moduleNumber;
        this.name = name;
        this.ects = ects;
        this.modulerunId = modulerunId;
        this.semester = semester;
        this.year = year;
        this.running = running;
        this.teacherFirstname = teacherFirstname;
        this.teacherLastname = teacherLastname;
    }

    public StudentEnroll(Long moduleId, String moduleNumber, String name, int ects, Long modulerunId, String semester,
                         int year, boolean running) {
        this.moduleId = moduleId;
        this.moduleNumber = moduleNumber;
        this.name = name;
        this.ects = ects;
        this.modulerunId = modulerunId;
        this.semester = semester;
        this.year = year;
        this.running = running;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleNumber() {
        return moduleNumber;
    }

    public void setModuleNumber(String moduleNumber) {
        this.moduleNumber = moduleNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public Long getModulerunId() {
        return modulerunId;
    }

    public void setModulerunId(Long modulerunId) {
        this.modulerunId = modulerunId;
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

    public String getTeacherFirstname() {
        return teacherFirstname;
    }

    public void setTeacherFirstname(String teacherFirstname) {
        this.teacherFirstname = teacherFirstname;
    }

    public String getTeacherLastname() {
        return teacherLastname;
    }

    public void setTeacherLastname(String teacherLastname) {
        this.teacherLastname = teacherLastname;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
