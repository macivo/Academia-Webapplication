/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.enroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class EnrollRepository provides persistence methods for enrollment.
 */
public class EnrollRepository {

    private static final String FIND_BY_MODULE_RUN_ID = "SELECT * FROM enrollment WHERE enrollment.module_run_id=?";
    private static final String FIND_BY_ID = "SELECT * FROM enrollment WHERE enrollment.id=?";
    private static final String DELETE_QUERY = "DELETE FROM enrollment WHERE enrollment.id=?";
    private static final String FIND_BY_STUDENT_ID = "SELECT enrollment.id as enrollment_id, enrollment.module_run_id, enrollment.student_id, enrollment.grade, module_run.semester, module_run.year, module_run.running, module.module_number, module.name, module.description, module.module_coordinator, module.ects FROM enrollment INNER JOIN module_run ON enrollment.module_run_id = module_run.id INNER JOIN module ON module_run.module_id = module.id INNER JOIN student ON enrollment.student_id = student.id WHERE student.id=?";
    private static final String UPDATE_QUERY = "UPDATE enrollment SET grade=? WHERE enrollment.id=?";
    private static final String INSERT_ENROLLMENT_QUERY = "INSERT INTO enrollment (module_run_id, student_id, grade) VALUES (?, ?, '*')";
    private static final String IS_PERSON_TEACHER_OF_THIS_MODULE_RUN_QUERY = "SELECT mr.id FROM module_run mr INNER JOIN teaching t ON mr.id = t.module_run_id WHERE t.module_run_id=? AND t.personal_id=?";
    private static final String HAS_PERSON_THIS_STUDENT_ID_QUERY = "SELECT * FROM person p JOIN student s ON p.id=s.personal_id WHERE p.id=? AND s.id=?";
    private static final String IS_PERSON_TEACHER_FOR_THIS_ENROLLMENT_QUERY = "SELECT * FROM enrollment e JOIN teaching t ON e.module_run_id=t.module_run_id WHERE e.id=? AND t.personal_id=?";

    private static final Logger LOGGER = Logger.getLogger(EnrollRepository.class.getName());
    private final Connection connection;
    public EnrollRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Finding an enrollment from the id
     *
     * @param id - id of enrollment
     * @return - an enrollment
     * @throws SQLException - throws SQL Exception
     */
    public Enroll findById(long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
        statement.setLong(1, id);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        if (results.next()) {
            return getEnroll(results);
        } else return null;
    }

    /**
     * Finding all enrollments from an id of running module
     *
     * @param moduleRunId - id of running module
     * @return - list of enrollments
     * @throws SQLException - throws SQL Exception
     */
    public List<Enroll> findByModuleRunId(long moduleRunId) throws SQLException {
        List<Enroll> enrolls = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_MODULE_RUN_ID);
        statement.setLong(1, moduleRunId);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            enrolls.add(getEnroll(results));
        }
        return enrolls;
    }

    /**
     * Finding all enrollment from an id of a student
     *
     * @param studentId - id of student
     * @return - list of enrollments of this student
     * @throws SQLException - throws SQL Exception
     */
    public List<Enroll> findByStudentId(long studentId) throws SQLException {
        List<Enroll> enrolls = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_STUDENT_ID);
        statement.setLong(1, studentId);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            enrolls.add(getEnrollForStudent(results));
        }
        return enrolls;
    }

    /**
     * Update the grade of enrollment
     *
     * @param enroll - enrollment
     * @return - true if updated
     * @throws SQLException - throws SQL Exception
     */
    public boolean update(Enroll enroll) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        statement.setString(1, enroll.getGrade());
        statement.setLong(2, enroll.getId());
        LOGGER.info("Executing query: " + statement);
        return statement.executeUpdate() > 0;
    }

    /**
     * Makes an enroll object from the database result
     *
     * @param results - the result of query
     * @return - an enroll object
     * @throws SQLException - throws SQL Exception
     */
    private Enroll getEnroll(ResultSet results) throws SQLException {
        return new Enroll(
                results.getLong("id"),
                results.getLong("module_run_id"),
                results.getLong("student_id"),
                results.getString("grade")
        );
    }

    /**
     * delete an enroll from the database
     *
     * @param id - the id of the enroll to delete
     * @return boolean - true if deleting was successful otherwise false
     * @throws SQLException - throws SQL Exception
     */
    public boolean delete(long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
        statement.setLong(1, id);
        LOGGER.info("Executing query: " + statement);
        return statement.executeUpdate() > 0;
    }

    /**
     * Makes an enroll object from the database result with information about module
     *
     * @param results - the result of query
     * @return - an enroll object
     * @throws SQLException - throws SQL Exception
     */
    private Enroll getEnrollForStudent(ResultSet results) throws SQLException {
        return new Enroll(
                results.getLong("enrollment_id"),
                results.getLong("module_run_id"),
                results.getLong("student_id"),
                results.getString("grade"),
                results.getString("semester"),
                results.getInt("year"),
                results.getBoolean("running"),
                results.getString("module_number"),
                results.getString("description"),
                results.getString("name"),
                results.getLong("module_coordinator"),
                results.getInt("ects")
        );
    }

    /**
     * inserts a new record to the database in table module
     *
     * @param enroll - the enrollment
     * @return the unique id of this record
     * @throws SQLException - throws SQL Exception
     */
    public long persist(Enroll enroll) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_ENROLLMENT_QUERY,
                Statement.RETURN_GENERATED_KEYS);
        int parameterIndex = 0;
        statement.setLong(++parameterIndex, enroll.getModule_run_id());
        statement.setLong(++parameterIndex, enroll.getStudent_id());
        LOGGER.info("Executing query: " + statement);
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getLong(1);
    }

    /**
     * check if person is teacher of a module run
     *
     * @param personalId  - personal id
     * @param moduleRunId - module run id
     * @return - true if person is teacher of this module - false if person is not teacher of this module
     * @throws SQLException - throws SQL Exception
     */
    public boolean isPersonTeacherOfModuleRun(Long personalId, Long moduleRunId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_PERSON_TEACHER_OF_THIS_MODULE_RUN_QUERY);
        int parameterIndex = 0;
        statement.setLong(++parameterIndex, moduleRunId);
        statement.setLong(++parameterIndex, personalId);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        return results.next();
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
        int parameterIndex = 0;
        statement.setLong(++parameterIndex, personalId);
        statement.setLong(++parameterIndex, studentId);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        return results.next();
    }

    /**
     * check if person with person id is teacher for this enrollment id
     *
     * @param enrollmentId - enrollment id
     * @param personalId   - personal id
     * @return true if person is teacher for this enrollment id - false if person is not teacher for this enrollment id
     * @throws SQLException - throws SQL Exception
     */
    public boolean isPersonTeacherForThisEnrollment(Long enrollmentId, Long personalId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_PERSON_TEACHER_FOR_THIS_ENROLLMENT_QUERY);
        int parameterIndex = 0;
        statement.setLong(++parameterIndex, enrollmentId);
        statement.setLong(++parameterIndex, personalId);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        return results.next();
    }
}
