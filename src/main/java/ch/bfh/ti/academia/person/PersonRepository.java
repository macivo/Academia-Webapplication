/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The class PersonRepository provides persistence method for person.
 */
public class PersonRepository {
    private static final String DELETE_QUERY = "DELETE FROM person WHERE person.id=?";
    private static final String FIND_BY_ID = "SELECT * FROM person WHERE person.id=?";
    private static final String UPDATE_QUERY = "UPDATE person SET gender=?, firstname=?, lastname=?, date_of_birth=?, email=?, username=?, password=?, role=?, status=? WHERE id =?";
    private static final String UPDATE_QUERY_BY_STUDENT_ID = "UPDATE person SET gender=?, firstname=?, lastname=?, date_of_birth=?, email=?, username=?, password=?, role=?, status=? WHERE id = (Select personal_id from student where id = ? )";
    private static final String INSERT_QUERY = "INSERT INTO person ( gender, firstname, lastname, date_of_birth, email, username, password, role, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_USERNAME_QUERY = "SELECT * FROM person WHERE person.username=?";
    private static final String FIND_ALL_PROFS_QUERY = "SELECT * FROM person WHERE role='professor'";

    private static final Logger LOGGER = Logger.getLogger(PersonRepository.class.getName());
    private final Connection connection;

    public PersonRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Finding a person from the id
     *
     * @param id - id of person
     * @return - an person
     * @throws SQLException - throws SQL Exception
     */
    public Person findById(long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
        statement.setLong(1, id);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        if (results.next()) {
            return getPerson(results);
        } else return null;
    }

    /**
     * Deletion a student from the database
     *
     * @param personal_id - need a id of a person to delete
     * @return true if succeeded
     * @throws SQLException - throws SQL Exception
     */
    public boolean delete(long personal_id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setLong(1, personal_id);
            LOGGER.info("Executing query: " + statement);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Add a new person to database
     *
     * @param person - a person as object
     * @return id of a created person
     * @throws SQLException - throws SQL Exception
     */
    public long persist(Person person) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        int parameterIndex = 0;
        statement.setString(++parameterIndex, person.getGender());
        statement.setString(++parameterIndex, person.getFirstname());
        statement.setString(++parameterIndex, person.getLastname());
        statement.setDate(++parameterIndex, Date.valueOf(person.getDate_of_birth()));
        statement.setString(++parameterIndex, person.getEmail());
        statement.setString(++parameterIndex, person.getUsername());
        statement.setString(++parameterIndex, person.getPassword());
        statement.setString(++parameterIndex, person.getRole());
        statement.setBoolean(++parameterIndex, person.getStatus());
        LOGGER.info("Executing query: " + statement);
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getLong(1);
    }

    /**
     * Updates a person (not for student)
     *
     * @param person a person object
     * @return true if successful
     * @throws SQLException in case of SQLException
     */
    public boolean update(Person person) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            int parameterIndex = 0;
            statement.setString(++parameterIndex, person.getGender());
            statement.setString(++parameterIndex, person.getFirstname());
            statement.setString(++parameterIndex, person.getLastname());
            statement.setDate(++parameterIndex, Date.valueOf(person.getDate_of_birth()));
            statement.setString(++parameterIndex, person.getEmail());
            statement.setString(++parameterIndex, person.getUsername());
            statement.setString(++parameterIndex, person.getPassword());
            statement.setString(++parameterIndex, person.getRole());
            statement.setBoolean(++parameterIndex, person.getStatus());
            statement.setLong(++parameterIndex, person.getPersonal_id());
            LOGGER.info("Executing query: " + statement);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Updates a student
     *
     * @param person     a person object
     * @param student_id the student-id
     * @return true if successful
     * @throws SQLException in case of SQLException
     */
    public boolean update(Person person, Long student_id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY_BY_STUDENT_ID)) {
            int parameterIndex = 0;
            statement.setString(++parameterIndex, person.getGender());
            statement.setString(++parameterIndex, person.getFirstname());
            statement.setString(++parameterIndex, person.getLastname());
            statement.setDate(++parameterIndex, Date.valueOf(person.getDate_of_birth()));
            statement.setString(++parameterIndex, person.getEmail());
            statement.setString(++parameterIndex, person.getUsername());
            statement.setString(++parameterIndex, person.getPassword());
            statement.setString(++parameterIndex, person.getRole());
            statement.setBoolean(++parameterIndex, person.getStatus());
            statement.setLong(++parameterIndex, student_id);
            LOGGER.info("Executing query: " + statement);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Finding a person from username
     *
     * @param username username of person
     * @return person if found by username, null if not found
     * @throws SQLException in case of SQLException
     */
    public Person findPersonByUsername(String username) throws SQLException {
        Person person = new Person();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME_QUERY);
        statement.setString(1, username);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        if (results.next()) {
            return getPerson(results);
        } else return null;
    }

    /**
     * Finding all Profs for selection of module coordinator in creating module
     *
     * @return a list of all profs
     * @throws SQLException in case of SQLException
     */
    protected List<Person> getAllProfs() throws SQLException {
        List<Person> profs = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(FIND_ALL_PROFS_QUERY);
        LOGGER.info("Executing query: " + statement);
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            profs.add(getProf(results));
        }
        return profs;
    }

    /**
     * Helper Function to create a person from SQL response
     *
     * @param result SQL response
     * @return a new person as object
     * @throws SQLException in case of SQLException
     */
    private Person getPerson(ResultSet result) throws SQLException {
        Person person = new Person();
        person.setPersonal_id(result.getLong("id"));
        person.setGender(result.getString("gender"));
        person.setFirstname(result.getString("firstname"));
        person.setLastname(result.getString("lastname"));
        person.setDate_of_birth(result.getString("date_of_birth"));
        person.setEmail(result.getString("email"));
        person.setUsername(result.getString("username"));
        person.setPassword(result.getString("password"));
        person.setRole(result.getString("role"));
        person.setStatus(result.getBoolean("status"));
        return person;
    }

    /**
     * Helper function to create profs from SQL response
     * This is used for selecting the module coordinator when editing or creating modules
     *
     * @param result SQL response
     * @return a new person - only with teh attributes is, firstname and lastname
     * @throws SQLException
     */
    private Person getProf(ResultSet result) throws SQLException {
        Person person = new Person();
        person.setPersonal_id(result.getLong("id"));
        person.setFirstname(result.getString("firstname"));
        person.setLastname(result.getString("lastname"));
        return person;
    }
}
