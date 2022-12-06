/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.person;

public class Student extends Person {
    private Long student_id;
    private String matriculation_number;

    public Student(long personal_id, String gender, String firstname, String lastname, String date_of_birth,
                   String email, String username, String password, String role, Boolean status, Long student_id,
                   String matriculation_number) {
        super(personal_id, gender, firstname, lastname, date_of_birth, email, username, password, role, status);
        this.student_id = student_id;
        this.matriculation_number = matriculation_number;
    }

    public Student() {
        super();
    }

    public Long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }

    public String getMatriculation_number() {
        return matriculation_number;
    }

    public void setMatriculation_number(String matriculation_number) {
        this.matriculation_number = matriculation_number;
    }

}
