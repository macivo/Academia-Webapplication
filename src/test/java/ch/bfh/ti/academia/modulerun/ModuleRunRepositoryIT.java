
/*
* Academia (c) 2020, Bern University of Applied Sciences, Switzerland
*/

package ch.bfh.ti.academia.modulerun;

import ch.bfh.ti.academia.util.ConnectionManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModuleRunRepositoryIT {


	private static Connection connection;
	private static ModuleRunRepository repository;
	private static final ModuleRun modulerun = new ModuleRun(1, "autumn", 2021, true, "Programming with Java 1", "BTI1011");


	@BeforeAll
	public static void init() {
		connection = ConnectionManager.getConnection(false);
		repository = new ModuleRunRepository(connection);
	}

	@AfterAll
	public static void close() {
		ConnectionManager.close(connection);
	}

	@Test
	@Order(1)
	public void persistModulerun() throws SQLException {
		long id = repository.persist(modulerun);
		modulerun.setId(id);
		ModuleRun persistedModulerun = repository.findById(id);
		assertEquals(modulerun, persistedModulerun);
	}

	@Test
	@Order(2)
	public void findModulerun() throws SQLException {
		List<ModuleRun> moduleruns = repository.findAll();
		assertTrue(moduleruns.contains(modulerun));
	}

	@Test
	@Order(3)
	public void updateModulerun() throws SQLException {
		modulerun.setYear(2025);
		repository.update(modulerun);
		ModuleRun updatedModulerun = repository.findById(modulerun.getId());
		assertEquals(modulerun, updatedModulerun);
	}

	@Test
	@Order(4)
	public void deleteModulerun() throws SQLException {
		repository.delete(modulerun.getId());
		ModuleRun deletedModulerun = repository.findById(modulerun.getId());
		assertNull(deletedModulerun);
	}


}