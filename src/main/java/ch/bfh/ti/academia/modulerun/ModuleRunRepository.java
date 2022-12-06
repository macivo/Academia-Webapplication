/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.modulerun;

import ch.bfh.ti.academia.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Savepoint;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * The class ModuleRunRepository provides persistence methods for module_run.
 */
public class ModuleRunRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM module_run";
    private static final String FIND_RUNS_AND_MODULES = "SELECT mr.*, m.id as m_id, m.module_number, m.name FROM module m LEFT OUTER JOIN module_run mr ON mr.module_id = m.id";
    private static final String FIND_BY_ID_QUERY = "SELECT mr.*, m.id as m_id, m.module_number, m.name FROM module m LEFT OUTER JOIN module_run mr ON mr.module_id = m.id WHERE mr.id=?";
    private static final String INSERT_QUERY_MODRUN = "INSERT INTO module_run (module_id, semester, year, running) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
    private static final String UPDATE_QUERY = "UPDATE module_run SET semester=?, year=?, running=? WHERE id=?";
    private static final String DELETE_QUERY_ENROLMENT = "DELETE FROM enrollment WHERE module_run_id=?";
    private static final String DELETE_QUERY_TEACHING = "DELETE FROM teaching WHERE module_run_id=?";
    private static final String DELETE_QUERY_MODULERUN = "DELETE FROM module_run WHERE id=?";

    private static final Logger LOGGER = Logger.getLogger(ModuleRunRepository.class.getName());
    private final Connection connection;
    public ModuleRunRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get all module_run from database
     *
     * @return List - ArrayList of module_run's
     * @throws SQLException - throws SQL Exception
     */
    public List<ModuleRun> findAll() throws SQLException {
        List<ModuleRun> moduleRuns = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(FIND_RUNS_AND_MODULES)) {
            LOGGER.info("Executing query: " + statement);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                moduleRuns.add(getModuleRun(results));
            }
            return moduleRuns;
        }
    }

    /**
     * Makes a ModuleRun Object from the database result
     *
     * @param results - the database result
     * @return ModuleRun - a ModuleRun object
     * @throws SQLException - throws SQL Exception
     */
    private ModuleRun getModuleRun(ResultSet results) throws SQLException {
        try {
            ModuleRun moduleRun = new ModuleRun();
            moduleRun.setId(results.getLong("id"));
            moduleRun.setSemester(results.getString("semester"));
            moduleRun.setYear(results.getInt("year"));
            moduleRun.setRunning(results.getBoolean("running"));
            moduleRun.setModuleName(results.getString("name"));
            moduleRun.setModuleNumber(results.getString("module_number"));
            moduleRun.setModuleId(results.getLong("m_id"));
            return moduleRun;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * find an existing module_run by it's unique id
     *
     * @param id - the id of the module_run
     * @return ModuleRun
     * @throws SQLException - throws SQL Exception
     */
    public ModuleRun findById(long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);
            LOGGER.info("Executing query: " + statement);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return getModuleRun(results);
            }
        } catch (SQLException e) {
            ConnectionManager.rollback(connection);
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    /**
     * inserts a new record to the database in table module_run
     *
     * @param moduleRun - the module_run to insert
     * @return the unique id of this record
     * @throws SQLException - throws SQL Exception
     */
    public long persist(ModuleRun moduleRun) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY_MODRUN,
                Statement.RETURN_GENERATED_KEYS)) {
            int parameterIndex = 0;
            statement.setLong(++parameterIndex, moduleRun.getModuleId());
            statement.setString(++parameterIndex, moduleRun.getSemester());
            statement.setInt(++parameterIndex, moduleRun.getYear());
            statement.setBoolean(++parameterIndex, moduleRun.isRunning());
            LOGGER.info("Executing query: " + statement);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            return keys.getLong(1);
        }
    }

    /**
     * updates a module_run
     *
     * @param moduleRun - to changes module_run
     * @return boolean - true if update was successful otherwise false
     * @throws SQLException - throws SQL Exception
     */
    public boolean update(ModuleRun moduleRun) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            int parameterIndex = 0;
            statement.setString(++parameterIndex, moduleRun.getSemester());
            statement.setInt(++parameterIndex, moduleRun.getYear());
            statement.setBoolean(++parameterIndex, moduleRun.isRunning());
            statement.setLong(++parameterIndex, moduleRun.getId());
            LOGGER.info("Executing query: " + statement);
            return statement.executeUpdate() > 0;
        }
    }
    /**
     * delete a module_run from the database
     *
     * @param id - the id of the module to delete
     * @return boolean - true if deleting was successful otherwise false
     * @throws SQLException - throws SQL Exception
     */
    public boolean delete(long id) throws SQLException {
        Savepoint save1 = connection.setSavepoint();
        try {
            PreparedStatement statementEnrolment = connection.prepareStatement(DELETE_QUERY_ENROLMENT);
            statementEnrolment.setLong(1, id);
            LOGGER.info("Executing query: " + statementEnrolment);
            statementEnrolment.executeUpdate();

            PreparedStatement statementTeaching = connection.prepareStatement(DELETE_QUERY_TEACHING);
            statementTeaching.setLong(1, id);
            LOGGER.info("Executing query: " + statementTeaching);
            statementTeaching.executeUpdate();

            PreparedStatement statementModuleRun = connection.prepareStatement(DELETE_QUERY_MODULERUN);
            statementModuleRun.setLong(1, id);
            LOGGER.info("Executing query: " + statementModuleRun);
            statementModuleRun.executeUpdate();
            connection.commit();
            return true;
        } catch (Exception e) {
            connection.rollback(save1);
            return false;
        }
    }
}
