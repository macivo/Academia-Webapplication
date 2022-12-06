/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class ModuleRepository provides persistence methods for modules.
 */
public class ModuleRepository {

	private static final String FIND_ALL_QUERY = "SELECT * FROM module";
	private static final String FIND_ALL_QUERY_WITH_COORD_NAME_QUERY = "SELECT DISTINCT a.id, a.module_number, a.name, a.description, a.module_coordinator, a.ects, a.running, p.firstname, p.lastname FROM (SELECT module.id, module.module_number, module.name, module.description, module.module_coordinator, module.ects, mr.running FROM module LEFT JOIN module_run mr ON module.id = mr.module_id AND mr.running=true) a LEFT JOIN person p ON a.module_coordinator = p.id";
	private static final String FIND_BY_ID_WITH_COORD_NAME_QUERY = "SELECT DISTINCT a.id, a.module_number, a.name, a.description, a.module_coordinator, a.ects, a.running, p.firstname, p.lastname FROM (SELECT module.id, module.module_number, module.name, module.description, module.module_coordinator, module.ects, mr.running FROM module LEFT JOIN module_run mr ON module.id = mr.module_id AND mr.running=true) a LEFT JOIN person p ON a.module_coordinator = p.id WHERE a.id=?";
	private static final String FIND_BY_NUMBER_QUERY = "SELECT * FROM module WHERE module_number=?";
	private static final String INSERT_QUERY = "INSERT INTO module (module_number, name, description, module_coordinator, ects) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_QUERY = "UPDATE module SET module_number=?, name=?, description=?, module_coordinator=?, ects=? WHERE id=?";
	private static final String DELETE_QUERY = "DELETE FROM module WHERE id=?";
	private static final String IS_PERSON_PROF_QUERY = "SELECT id FROM person WHERE id=? AND role='professor'";
	private static final String IS_PERSON_MODULE_COORDINATOR_FROM_MODULE_QUERY = "SELECT id FROM module WHERE id=? AND module_coordinator=?";


	private static final Logger LOGGER = Logger.getLogger(ModuleRepository.class.getName());
	private final Connection connection;
	public ModuleRepository(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Get all modules from database
	 *
	 * @return List - ArrayList of modules
	 * @throws SQLException - throws SQL Exception
	 */
	public List<Module> findAll() throws SQLException {
		List<Module> modules = new ArrayList<>();
		PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
		LOGGER.info("Executing query: " + statement);
		ResultSet results = statement.executeQuery();
		while (results.next()) {
			modules.add(getModule(results));
		}
		return modules;
	}

	/**
	 * Get all modules with module_run state running and information
	 * about first and last name of the module coordinator
	 *
	 * @return List - ArrayList of modules
	 * @throws SQLException - throws SQL Exception
	 */
	public List<Module> findAllWithRunningAndCoord() throws SQLException {
		List<Module> modules = new ArrayList<>();
		PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY_WITH_COORD_NAME_QUERY);
		LOGGER.info("Executing query: " + statement);
		ResultSet results = statement.executeQuery();
		while (results.next()) {
			modules.add(getModuleWithRunningAndCoord(results));
		}
		return modules;
	}

	/**
	 * find an existing module by it's unique id
	 *
	 * @param id - the id of the module
	 * @return Module
	 * @throws SQLException - throws SQL Exception
	 */
	public Module findById(long id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_WITH_COORD_NAME_QUERY);
		statement.setLong(1, id);
		LOGGER.info("Executing query: " + statement);
		ResultSet results = statement.executeQuery();
		if (results.next()) {
			return getModuleWithRunningAndCoord(results);
			//return getModule(results);
		} else return null;
	}

	/**
	 * find a module by its module number
	 *
	 * @param number - the module number - a string
	 * @return Module
	 * @throws SQLException - throws SQL Exception
	 */
	public Module findByNumber(String number) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(FIND_BY_NUMBER_QUERY);
		statement.setString(1, number);
		LOGGER.info("Executing query: " + statement);
		ResultSet results = statement.executeQuery();
		if (results.next()) {
			return getModule(results);
		} else return null;
	}

	/**
	 * inserts a new record to the database in table module
	 *
	 * @param module - the module to insert
	 * @return the unique id of this record
	 * @throws SQLException - throws SQL Exception
	 */
	public long persist(Module module) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(INSERT_QUERY,
				Statement.RETURN_GENERATED_KEYS);
		int parameterIndex = 0;
		statement.setString(++parameterIndex, module.getNumber());
		statement.setString(++parameterIndex, module.getName());
		statement.setString(++parameterIndex, module.getDescription());
		statement.setLong(++parameterIndex, module.getCoordinator());
		statement.setInt(++parameterIndex, module.getEcts());
		LOGGER.info("Executing query: " + statement);
		statement.executeUpdate();
		ResultSet keys = statement.getGeneratedKeys();
		keys.next();
		return keys.getLong(1);
	}

	/**
	 * updates a module
	 *
	 * @param module - the changes module
	 * @return boolean - true if update was successful otherwise false
	 * @throws SQLException - throws SQL Exception
	 */
	public boolean update(Module module) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
		int parameterIndex = 0;
		statement.setString(++parameterIndex, module.getNumber());
		statement.setString(++parameterIndex, module.getName());
		statement.setString(++parameterIndex, module.getDescription());
		statement.setLong(++parameterIndex, module.getCoordinator());
		statement.setInt(++parameterIndex, module.getEcts());
		statement.setLong(++parameterIndex, module.getId());
		LOGGER.info("Executing query: " + statement);
		return statement.executeUpdate() > 0;
	}

	/**
	 * delete a module from the database
	 *
	 * @param id - the id of the module to delete
	 * @return boolean - true if deleting was sucessful otherwise false
	 * @throws SQLException - throws SQL Exception
	 */
	public boolean delete(long id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
			statement.setLong(1, id);
			LOGGER.info("Executing query: " + statement);
			return statement.executeUpdate() > 0;
	}

	/**
	 * Makes a module Object from the database result
	 *
	 * @param results - the database result
	 * @return Module - a module object
	 * @throws SQLException - throws SQL Exception
	 */
	private Module getModule(ResultSet results) throws SQLException {
		Module module = new Module();
		module.setId(results.getLong("id"));
		module.setNumber(results.getString("module_number"));
		module.setName(results.getString("name"));
		module.setDescription(results.getString("description"));
		module.setCoordinator(results.getLong("module_coordinator"));
		module.setEcts(results.getInt("ects"));
		return module;
	}

	/**
	 * Makes an module Object from the database result with the information
	 * if module is running and first and last name of the module coordinator
	 *
	 * @param results - the database result
	 * @return Module - a module object
	 * @throws SQLException - throws SQL Exception
	 */
	private Module getModuleWithRunningAndCoord(ResultSet results) throws SQLException {
		Module module = new Module();
		module.setId(results.getLong("id"));
		module.setNumber(results.getString("module_number"));
		module.setName(results.getString("name"));
		module.setDescription(results.getString("description"));
		module.setCoordinator(results.getLong("module_coordinator"));
		module.setEcts(results.getInt("ects"));
		module.setRunning(results.getBoolean("running"));
		module.setFirstname(results.getString("firstname"));
		module.setLastname(results.getString("lastname"));
		return module;
	}


	/**
	 * Just to check if personal id from input module coordinator is role professor
	 * @param personal_id - the id of a person
	 * @return boolean - true if it exists otherwise false
	 * @throws SQLException - throws SQL Exception
	 */
	public boolean isThisPersonProf(Long personal_id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(IS_PERSON_PROF_QUERY);
		statement.setLong(1, personal_id);
		LOGGER.info("Executing query: " + statement);
		ResultSet results = statement.executeQuery();
		return results.next();
	}

	/**
	 * Just to check if personal is module coordinator for this module id
	 * @param personal_id - the id of a person
	 * @param module_id - the id of a module
	 * @return boolean - true if it exists otherwise false
	 * @throws SQLException - throws SQL Exception
	 */
	public boolean isPersonModuleCoordinator(Long personal_id, Long module_id) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(IS_PERSON_MODULE_COORDINATOR_FROM_MODULE_QUERY);
		statement.setLong(1, module_id);
		statement.setLong(2, personal_id);
		LOGGER.info("Executing query: " + statement);
		ResultSet results = statement.executeQuery();
		return results.next();
	}
}
