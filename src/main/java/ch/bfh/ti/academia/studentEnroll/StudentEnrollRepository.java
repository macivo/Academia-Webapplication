/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.studentEnroll;

import ch.bfh.ti.academia.enroll.Enroll;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class StudentEnrollRepository provides persistence methods for enroll students.
 */
public class StudentEnrollRepository {

    private static final String FIND_ALL_RUNNING_MODULES_FOR_PERSONAL_ID_QUERY = "SELECT enrollment.id as enrollment_id, enrollment.module_run_id, enrollment.student_id, enrollment.grade, module_run.semester, module_run.year, module_run.running, module.module_number, module.name, module.description, module.module_coordinator, module.ects FROM enrollment INNER JOIN module_run ON enrollment.module_run_id = module_run.id INNER JOIN module ON module_run.module_id = module.id INNER JOIN student ON enrollment.student_id = student.id INNER JOIN person ON student.personal_id = person.id WHERE person.id=?";
    private static final String FIND_ALL_RUNNING_MODULES_QUERY = "SELECT module_run.id AS mr_id, module_run.module_id AS m_id, semester, year, running, module_number, name, ects FROM module_run JOIN module ON module_run.module_id = module.id WHERE module_run.running=true";
    private static final Logger LOGGER = Logger.getLogger(StudentEnrollRepository.class.getName());
    private final Connection connection;

    public StudentEnrollRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * find all running modules from database
     *
     * @return List of StudentEnroll
     * @throws SQLException - throws SQL Exception
     */
    public List<StudentEnroll> findAllRunningModulesWithoutTeacher() throws SQLException {
        List<StudentEnroll> studentEnrolls = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_RUNNING_MODULES_QUERY)) {
            LOGGER.info("Executing query: " + statement);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                studentEnrolls.add(getAllRunningModules(results));
            }
            return studentEnrolls;
        }
    }

    /**
     * find all enrolled modules for a student by personal id
     *
     * @param id - personal id
     * @return List of Enroll
     * @throws SQLException - throws SQL Exception
     */
    public List<Enroll> findEnrolledModulesByPersonaId(long id) throws SQLException {
        List<Enroll> enrolls = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(FIND_ALL_RUNNING_MODULES_FOR_PERSONAL_ID_QUERY);
        statement.setLong(1, id);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            enrolls.add(getEnrollForStudent(results));
        }
        return enrolls;
    }

    /**
     * Makes a enroll Object from database result
     *
     * @param results - the database result
     * @return Enroll - a enroll object
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
     * Makes a studentEnroll object from database result
     *
     * @param results - the database result
     * @return - a studentEnroll object
     * @throws SQLException - throws SQL Exception
     */
    private StudentEnroll getAllRunningModules(ResultSet results) throws SQLException {
        StudentEnroll studentEnroll = new StudentEnroll();
        studentEnroll.setModuleId(results.getLong("m_id"));
        studentEnroll.setModuleNumber(results.getString("module_number"));
        studentEnroll.setName(results.getString("name"));
        studentEnroll.setEcts(results.getInt("ects"));
        studentEnroll.setModulerunId(results.getLong("mr_id"));
        studentEnroll.setSemester(results.getString("semester"));
        studentEnroll.setYear(results.getInt("year"));
        studentEnroll.setRunning(results.getBoolean("running"));
        return studentEnroll;
    }
}
