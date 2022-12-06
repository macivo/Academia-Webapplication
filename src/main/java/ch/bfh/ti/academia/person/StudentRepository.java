/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class StudentRepository provides persistence methods for students.
 * We do not catch any exceptions here. All SQL-Exception will be caught in Servlet
 */
public class StudentRepository {
    private static final String FIND_ALL_QUERY = "SELECT person.id as personal_id, person.gender, person.firstname, person.lastname, person.date_of_birth, person.email, person.username, person.password, person.role, person.status, student.id as student_id, student.matriculation_number FROM person INNER JOIN student ON person.id = student.personal_id";
    private static final String DELETE_QUERY = "DELETE FROM student WHERE student.id=?";
    private static final String INSERT_QUERY = "INSERT INTO student ( matriculation_number, personal_id) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE student SET matriculation_number=? WHERE student.id = ?";
    private static final String FIND_BY_MAT_QUERY = "SELECT student.matriculation_number FROM student WHERE matriculation_number=?";
    private static final String HAS_PERSON_THIS_STUDENT_ID_QUERY = "SELECT * FROM person p JOIN student s ON p.id=s.personal_id WHERE p.id=? AND s.id=?";
    private static final String FIND_BY_ID_QUERY = "SELECT person.id as personal_id, person.gender, person.firstname, person.lastname, person.date_of_birth, person.email, person.username, person.password, person.role, person.status, student.id as student_id, student.matriculation_number FROM person INNER JOIN student ON person.id = student.personal_id WHERE student.id=?";

    private static final Logger LOGGER = Logger.getLogger(StudentRepository.class.getName());
    private final Connection connection;

    public StudentRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * List all students
     *
     * @return a list of all student objects
     * @throws SQLException - throws SQL Exception
     */
    public List<Student> findAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            students.add(getStudent(results));
        }
        return students;
    }

    /**
     * Find a student from id
     *
     * @param student_id - id of student
     * @return Student - a student object
     * @throws SQLException - throws SQL Exception
     */
    public Student findStudentById(long student_id) throws SQLException {
        Student student = new Student();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY);
        statement.setLong(1, student_id);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        if (results.next()) {
            return getStudent(results);
        } else return null;
    }

    /**
     * Checking if a matriculation exits
     *
     * @param matriculation - a matriculation
     * @return boolean - true if found
     * @throws SQLException - throws SQL Exception
     */
    public boolean isExistingMatNr(String matriculation) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(FIND_BY_MAT_QUERY);
        statement.setString(1, matriculation);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        return results.next();
    }

    /**
     * Parser to a student object
     *
     * @param result - the results of SQL Query
     * @return Student - return a crated student object
     * @throws SQLException - throws SQL Exception
     */
    private Student getStudent(ResultSet result) throws SQLException {
        Student student = new Student();
        student.setStudent_id(result.getLong("student_id"));
        student.setMatriculation_number(result.getString("matriculation_number"));
        student.setPersonal_id(result.getLong("personal_id"));
        student.setGender(result.getString("gender"));
        student.setFirstname(result.getString("firstname"));
        student.setLastname(result.getString("lastname"));
        student.setDate_of_birth(result.getString("date_of_birth"));
        student.setEmail(result.getString("email"));
        student.setUsername(result.getString("username"));
        student.setPassword(result.getString("password"));
        student.setRole(result.getString("role"));
        student.setStatus(result.getBoolean("status"));
        return student;
    }

    /**
     * Add a new student to database
     *
     * @param student     - a student as object
     * @param personal_id - id of the person
     * @return student_id - id of a student
     * @throws SQLException - throws SQL Exception
     */
    public long persist(Student student, long personal_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, student.getMatriculation_number());
        statement.setLong(2, personal_id);
        LOGGER.info("Executing query: " + statement);
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getLong(1);
    }

    /**
     * Updating a student
     *
     * @param student - a student object
     * @return true if succeeded
     * @throws SQLException - throws SQL Exception
     */
    public boolean update(Student student) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        statement.setString(1, student.getMatriculation_number());
        statement.setLong(2, student.getStudent_id());
        LOGGER.info("Executing query: " + statement);
        return statement.executeUpdate() > 0;
    }

    /**
     * Deletion a student from the database
     * a student should not be deleted if he/she already has an enrollment
     *
     * @param student_id - need a id of a person to delete
     * @return true if succeeded
     * @throws SQLException - throws SQL Exception
     */
    public boolean delete(long student_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
        statement.setLong(1, student_id);
        LOGGER.info("Executing query: " + statement);
        return statement.executeUpdate() > 0;
    }

    /**
     * check if person with person id has this student id
     *
     * @param personalId - personal id
     * @param studentId  - student id
     * @return true if person is has this student id - false if person has not this student id
     * @throws SQLException - throws SQL Exception
     */
    public boolean hasPersonThisStudentId(Long personalId, Long studentId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(HAS_PERSON_THIS_STUDENT_ID_QUERY);
        statement.setLong(1, personalId);
        statement.setLong(2, studentId);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        return results.next();
    }
}
