package ch.bfh.ti.academia.person;
import ch.bfh.ti.academia.util.ConnectionManager;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryIT {
    private static final Person person = new Person(1L,"male","firstname","lastname", "1999-01-01","firstname.lastname@bhf.ch", "username","827ccb0eea8a706c4c34a16891f84e7b","professor", true);
    private static PersonRepository repository;

    @BeforeAll
    public static void init() {
        Connection connection = ConnectionManager.getConnection(false);
        repository = new PersonRepository(connection);
    }

    @Test
    @Order(1)
    public void persisPerson() throws SQLException {
        long id = repository.persist(person);
        person.setPersonal_id(id);
        Person persistedPerson = repository.findById(id);
        assertEquals(person, persistedPerson);
    }

    @Test
    @Order(2)
    public void findPerson() throws SQLException {
        Person foundPerson = repository.findById(person.getPersonal_id());
        assertEquals(person, foundPerson);
    }

    @Test
    @Order(3)
    public void updatePerson() throws SQLException {
        person.setUsername("updatedUsername");
        repository.update(person);
        Person updatedPerson = repository.findById(person.getPersonal_id());
        assertEquals(person, updatedPerson);
    }

    @Test
    @Order(4)
    public void deletePerson() throws SQLException {
        repository.delete(person.getPersonal_id());
        Person deletedPerson = repository.findById(person.getPersonal_id());
        assertNull(deletedPerson);
    }


}
