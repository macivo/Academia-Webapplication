/*
 * Academia (c) 2020, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.module;

import ch.bfh.ti.academia.util.ConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModuleRepositoryIT {

	//Test classes have to be written

	private static ModuleRepository repository;
    private static final Module module = new Module("BTI3021", "Project and Training 3", "Description goes here...", (long)1, 4, false, "Eric", "Dubuis");

	@BeforeAll
	public static void init() {
		Connection connection = ConnectionManager.getConnection(false);
		repository = new ModuleRepository(connection);
	}

	@Test
	@Order(1)
	public void persistModule() throws SQLException {
		long id = repository.persist(module);
		module.setId(id);
		Module persistedModule = repository.findById(id);
		assertEquals(module, persistedModule);
	}

	@Test
	@Order(2)
	public void findModule() throws SQLException {
		List<Module> modules = repository.findAllWithRunningAndCoord();
		assertTrue(modules.contains(module));
		Module foundModule = repository.findById(module.getId());
		assertEquals(module, foundModule);
	}

	@Test
	@Order(3)
	public void updateModule() throws SQLException {
		module.setName("Project & Training 3");
		repository.update(module);
		Module updatedModule = repository.findById(module.getId());
		assertEquals(module, updatedModule);
	}

	@Test
	@Order(4)
	public void deleteModule() throws SQLException {
		repository.delete(module.getId());
		Module deletedModule = repository.findById(module.getId());
		assertNull(deletedModule);
	}
}
