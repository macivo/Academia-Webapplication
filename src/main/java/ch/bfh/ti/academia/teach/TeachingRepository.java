/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.teach;

import ch.bfh.ti.academia.modulerun.ModuleRun;
import ch.bfh.ti.academia.person.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class TeachingRepository provides persistence methods for teaching objects.
 */
public class TeachingRepository {

    private static final String FIND_RUNS_FOR_PROF_QUERY = "SELECT module.id as m_id, module.module_number, module.name, module_run.id as id, module_run.semester,module_run.year, module_run.running FROM teaching LEFT JOIN module_run ON teaching.module_run_id = module_run.id LEFT JOIN module ON module_run.module_id = module.id WHERE teaching.personal_id = ?";
    private static final String FIND_STUDENTS_FOR_THIS_MODULE_QUERY = "SELECT s.id as student_id, s.matriculation_number, p.id as personal_id, p.gender, p.firstname, p.lastname, p.date_of_birth, p.email, p.username, p.password, p.role, p.status FROM module_run mr JOIN enrollment e on mr.id = e.module_run_id JOIN student s on e.student_id = s.id JOIN person p on s.personal_id = p.id WHERE mr.id = ?";
    private static final String FIND_PROFS_FOR_MODULERUN_QUERY = "SELECT teaching.*, prof.id as prof_id, prof.firstname, prof.lastname FROM person prof LEFT OUTER JOIN teaching ON teaching.personal_id = prof.id WHERE teaching.module_run_id=?";
    private static final String INSERT_QUERY = "INSERT INTO teaching (module_run_id, personal_id) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE teaching SET personal_id=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM teaching WHERE id=?";
    private static final String IS_PERSON_TEACHER_OF_THIS_MODULE_RUN_QUERY = "SELECT mr.id FROM module_run mr INNER JOIN teaching t ON mr.id = t.module_run_id WHERE t.module_run_id=? AND t.personal_id=?";

    private static final Logger LOGGER = Logger.getLogger(TeachingRepository.class.getName());
    private final Connection connection;

    public TeachingRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Helper methode to creat a object from DB result
     *
     * @param results - the database result
     * @return ModelRun - A ModelRun object
     * @throws SQLException - throws SQL Exception
     */
    private ModuleRun getModuleRun(ResultSet results) throws SQLException {
        ModuleRun moduleRun = new ModuleRun();
        moduleRun.setId(results.getLong("id"));
        moduleRun.setSemester(results.getString("semester"));
        moduleRun.setYear(results.getInt("year"));
        moduleRun.setRunning(results.getBoolean("running"));
        moduleRun.setModuleName(results.getString("name"));
        moduleRun.setModuleNumber(results.getString("module_number"));
        moduleRun.setModuleId(results.getLong("m_id"));
        return moduleRun;
    }

    /**
     * Get all running modules which teaches logged in prof from database
     *
     * @param id - the personal id of the teacher
     * @return a list of modules he/she teaches
     * @throws SQLException - throws SQL Exception
     */
    public List<ModuleRun> findModuleRunsForProf(long id) throws SQLException {
        List<ModuleRun> modules = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_RUNS_FOR_PROF_QUERY)) {
            statement.setLong(1, id);
            LOGGER.info("Executing query: " + statement);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                modules.add(getModuleRun(results));
            }
            return modules;
        }
    }

    /**
     * inserts a new record to the database in table teaching
     *
     * @param teaching - the Teaching to insert
     * @return the unique id of this record
     * @throws SQLException - throws SQL Exception
     */
    public long persist(Teaching teaching) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        statement.setLong(1, teaching.getModulerunId());
        statement.setLong(2, teaching.getPersonalId());
        LOGGER.info("Executing query: " + statement);
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getLong(1);
    }

    /**
     * Find all enrolled students from a running module
     *
     * @param id - the moduleRun id for the module
     * @return - a list of all enrolled students
     * @throws SQLException - throws SQL Exception
     */
    public List<Student> findStudentsForModule(long id) throws SQLException {
        List<Student> students = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_STUDENTS_FOR_THIS_MODULE_QUERY)) {
            statement.setLong(1, id);
            LOGGER.info("Executing query: " + statement);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                students.add(getStudent(results));
            }
        }
        return students;
    }

    /**
     * Find all Profs per module run
     *
     * @param id - the id of the module run
     * @return a list of teachings
     * @throws SQLException - throws SQL Exception
     */
    public List<Teaching> findProfsForModuleRuns(long id) throws SQLException {
        List<Teaching> teachings = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_PROFS_FOR_MODULERUN_QUERY)) {
            statement.setLong(1, id);
            LOGGER.info("Executing query: " + statement);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                teachings.add(getTeaching(results));
            }
        }
        return teachings;
    }

    /**
     * updates a Teaching
     *
     * @param teaching - the changes Teaching
     * @return boolean - true if update was successful otherwise false
     * @throws SQLException - throws SQL Exception
     */
    public boolean update(Teaching teaching) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setLong(1, teaching.getModulerunId());
            statement.setLong(2, teaching.getPersonalId());
            LOGGER.info("Executing query: " + statement);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Helper methode to creat a object from DB result
     *
     * @param result - the database result
     * @return Student - A Student object
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
     * Get a dataset from table teaching
     *
     * @param result a Database ResultSet
     * @return a Teaching Object
     * @throws SQLException throws an SQL-Exception
     */
    private Teaching getTeaching(ResultSet result) throws SQLException {
        Teaching teaching = new Teaching();
        teaching.setTeachingId(result.getLong("id"));
        teaching.setModulerunId(result.getLong("module_run_id"));
        teaching.setPersonalId(result.getLong("personal_id"));
        teaching.setProfFirstname(result.getString("firstname"));
        teaching.setProfLastname(result.getString("lastname"));
        return teaching;
    }

    /**
     * Delete a dataset from table teaching
     * @param id - the id of the teaching to delete
     * @return boolean - true if deleted otherwise false
     * @throws SQLException - throws SQL Exception
     */
    public boolean delete(long id) throws SQLException {
        PreparedStatement statementTeaching = connection.prepareStatement(DELETE_QUERY);
        statementTeaching.setLong(1, id);
        LOGGER.info("Executing query: " + statementTeaching);
        return statementTeaching.executeUpdate() > 0;
    }

    /**
     * Just to check if professor is teacher of this module run
     *
     * @param personal_id   - the id of a person
     * @param module_run_id - the id of a module run
     * @return boolean - true if it exists otherwise false
     * @throws SQLException - throws SQL Exception
     */
    public boolean isPersonTeacherOfModuleRun(Long personal_id, Long module_run_id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(IS_PERSON_TEACHER_OF_THIS_MODULE_RUN_QUERY);
        statement.setLong(1, module_run_id);
        statement.setLong(2, personal_id);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        return results.next();
    }
}
