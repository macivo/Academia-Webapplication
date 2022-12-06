/*
 * Academia (c) 2020, Bern University of Applied Sciences, Switzerland
 */
package ch.bfh.ti.academia.enroll;
import ch.bfh.ti.academia.util.ConnectionManager;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EnrollRepositoryIT {
    private static final Long ID = 1L;
    private static EnrollRepository repository;
    private static final Enroll enroll = new Enroll(ID, ID, ID, "*");

    @BeforeAll
    public static void init() {
        Connection connection = ConnectionManager.getConnection(false);
        repository = new EnrollRepository(connection);
    }

    @Test
    @Order(1)
    public void persistEnroll() throws SQLException {
        long id = repository.persist(enroll);
        enroll.setId(id);
        Enroll persistedEnroll = repository.findById(id);
        assertEquals(enroll, persistedEnroll);
    }

    @Test
    @Order(2)
    public void findEnroll() throws SQLException {
        List<Enroll> enrolls = repository.findByModuleRunId(enroll.getModule_run_id());
        assertTrue(enrolls.contains(enroll));
    }

    @Test
    @Order(3)
    public void updateEnroll() throws SQLException {
        enroll.setGrade("A");
        repository.update(enroll);
        Enroll updatedEnroll = repository.findById(enroll.getId());
        assertEquals(enroll, updatedEnroll);
    }

    @Test
    @Order(4)
    public void deleteEnroll() throws SQLException {
        repository.delete(enroll.getId());
        Enroll deletedEnroll = repository.findById(enroll.getId());
        assertNull(deletedEnroll);
    }
}
