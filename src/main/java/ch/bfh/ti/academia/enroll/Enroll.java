/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.enroll;

import java.util.Objects;

/**
 * The class Enroll represents an enrollment.
 */
public class Enroll {
    private long id;
    private long module_run_id;
    private long student_id;
    private String grade;
    private String semester;
    private int year;
    private boolean running;
    private String module_number;
    private String module_description;
    private String name;
    private long module_coordinator;
    private int ects;

    public Enroll() {
    }

    public Enroll(Long id, long module_run_id, long student_id, String grade) {
        this.id = id;
        this.module_run_id = module_run_id;
        this.student_id = student_id;
        this.grade = grade;
    }

    public Enroll(long id, long module_run_id, long student_id, String grade, String semester, int year,
                  boolean running, String module_number, String module_description, String name,
                  long module_coordinator, int ects) {
        this.id = id;
        this.module_run_id = module_run_id;
        this.student_id = student_id;
        this.grade = grade;
        this.semester = semester;
        this.year = year;
        this.running = running;
        this.module_number = module_number;
        this.module_description = module_description;
        this.name = name;
        this.module_coordinator = module_coordinator;
        this.ects = ects;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getModule_run_id() {
        return module_run_id;
    }

    public void setModule_run_id(long module_run_id) {
        this.module_run_id = module_run_id;
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public String getModule_number() {
        return module_number;
    }

    public void setModule_number(String module_number) {
        this.module_number = module_number;
    }

    public String getModule_description() {
        return module_description;
    }

    public void setModule_description(String module_description) {
        this.module_description = module_description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getModule_coordinator() {
        return module_coordinator;
    }

    public void setModule_coordinator(long module_coordinator) {
        this.module_coordinator = module_coordinator;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enroll enroll = (Enroll) o;
        return id == enroll.id
                && module_run_id == enroll.module_run_id
                && student_id == enroll.student_id
                && Objects.equals(grade, enroll.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, module_run_id, student_id, grade);
    }

    @Override
    public String toString() {
        return "Enroll{"
                + "id=" + id
                + ", module_run_id=" + module_run_id
                + ", student_id=" + student_id
                + ", grade='" + grade + '\''
                + ", semester='" + semester + '\''
                + ", year=" + year
                + ", running=" + running
                + ", module_number='" + module_number + '\''
                + ", module_description='" + module_description + '\''
                + ", name='" + name + '\''
                + ", module_coordinator=" + module_coordinator
                + ", ects=" + ects
                + '}';
    }
}
