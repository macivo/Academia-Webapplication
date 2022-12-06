package ch.bfh.ti.academia.person;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonTest {
    @Test
    public void createPerson() {
        Person person = new Person(1L,
                                    "male",
                                    "firstname",
                                    "lastname",
                                    "1999-01-01",
                                    "firstname.lastname@bfh.ch",
                                    "username",
                                    "827ccb0eea8a706c4c34a16891f84e7b",
                                    "professor",
                                    true);
        assertEquals(1L, person.getPersonal_id());
        assertEquals("male", person.getGender());
        assertEquals("firstname", person.getFirstname());
        assertEquals("lastname", person.getLastname());
        assertEquals("1999-01-01", person.getDate_of_birth());
        assertEquals("firstname.lastname@bfh.ch", person.getEmail());
        assertEquals("username", person.getUsername());
        assertEquals("827ccb0eea8a706c4c34a16891f84e7b", person.getPassword());
        assertEquals("professor", person.getRole());
        assertTrue(person.getStatus());
    }
}
