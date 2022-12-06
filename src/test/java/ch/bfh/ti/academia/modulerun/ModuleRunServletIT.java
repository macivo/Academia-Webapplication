package ch.bfh.ti.academia.modulerun;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModuleRunServletIT {


	private static final ModuleRun modulerun = new ModuleRun(1, "autumn", 2021, true, "Programming with Java 1", "BTI1011");

	@BeforeAll
	public static void init() {
		RestAssured.port = 8080;
	}

	@Test
	@Order(1)
	public void addModulerun() {
		int id = RestAssured
				.given()
                .auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
                .header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(modulerun)
				.when().post("/api/moduleruns")
				.then().statusCode(201).extract().path("id");
		modulerun.setId((long)id);
		ModuleRun addedModulerun = RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().get("/api/moduleruns/" + id)
				.then().statusCode(200).extract().as(ModuleRun.class);
		assertEquals(modulerun, addedModulerun);

	}

	@Test
	@Order(2)
	public void getModuleruns() {
		ModuleRun[] moduleruns = RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().get("/api/moduleruns")
				.then().statusCode(200).extract().as(ModuleRun[].class);
		assertTrue(Arrays.asList(moduleruns).contains(modulerun));
	}

	@Test
	@Order(3)
	public void updateModulerun() {
		modulerun.setSemester("spring");
		RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.body(modulerun)
				.when().put("/api/moduleruns/" + modulerun.getId())
				.then().statusCode(204);
		ModuleRun updatedModulerun = RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().get("/api/moduleruns/" + modulerun.getId())
				.then().statusCode(200).extract().as(ModuleRun.class);
		assertEquals(modulerun, updatedModulerun);
	}

	@Test
	@Order(4)
	public void removeModulerun() {
		RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().delete("/api/moduleruns/" + modulerun.getId())
				.then().statusCode(204);
		RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().get("/api/moduleruns/" + modulerun.getId())
				.then().statusCode(404);
	}

}
