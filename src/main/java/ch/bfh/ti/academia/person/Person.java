/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.person;

import java.util.Objects;

public class Person {
    private long personal_id;
    private String gender;
    private String firstname;
    private String lastname;
    private String date_of_birth;
    private String email;
    private String username;
    private String password;
    private String role;
    private Boolean status;

    public Person(long personal_id, String gender, String firstname, String lastname, String date_of_birth,
                  String email, String username, String password, String role, Boolean status) {
        this.personal_id = personal_id;
        this.gender = gender;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date_of_birth = date_of_birth;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public Person(long personal_id, String firstname, String lastname) {
        this.personal_id = personal_id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Person() {
    }

    public long getPersonal_id() {
        return personal_id;
    }

    public void setPersonal_id(long person_id) {
        this.personal_id = person_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personal_id == person.personal_id && Objects.equals(username, person.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personal_id, username);
    }

    @Override
    public String toString() {
        return "Person{"
                + "personal_id=" + personal_id
                + ", gender='" + gender + '\''
                + ", firstname='" + firstname + '\''
                + ", lastname='" + lastname + '\''
                + ", date_of_birth='" + date_of_birth + '\''
                + ", email='" + email + '\''
                + ", username='" + username + '\''
                + ", password='" + password + '\''
                + ", role='" + role + '\''
                + ", status=" + status
                + '}';
    }
}
