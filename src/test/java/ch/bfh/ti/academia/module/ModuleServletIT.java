package ch.bfh.ti.academia.module;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class
ModuleServletIT {

	private static final Module module = new Module("BTI3021", "Project and Training 3", "Description goes here...", (long)1, 4, false, "Eric", "Dubuis");

	@BeforeAll
	public static void init() {
		RestAssured.port = 8080;
	}

	@Test
	@Order(1)
	public void addModule() {
		int id = RestAssured
				.given()
                .auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
                .header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(module)
				.when().post("/api/modules")
				.then().statusCode(201).extract().path("id");
		module.setId((long)id);
		Module addedModule = RestAssured
				.given()
                .auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
                .header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().get("/api/modules/" + id)
				.then().statusCode(200).extract().as(Module.class);
		assertEquals(module, addedModule);
	}

	@Test
	@Order(2)
	public void getModules() {
		Module[] modules = RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().get("/api/modules")
				.then().statusCode(200).extract().as(Module[].class);
		assertTrue(Arrays.asList(modules).contains(module));
	}

	@Test
	@Order(3)
	public void updateModule() {
		module.setName("Project & Training 3");
		RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.body(module)
				.when().put("/api/modules/" + module.getId())
				.then().statusCode(204);
		Module updatedModule = RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().get("/api/modules/" + module.getId())
				.then().statusCode(200).extract().as(Module.class);
		assertEquals(module, updatedModule);
	}

	@Test
	@Order(4)
	public void removeModule() {
		RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().delete("/api/modules/" + module.getId())
				.then().statusCode(204);
		RestAssured
				.given()
				.auth().preemptive().basic("admin", "827ccb0eea8a706c4c34a16891f84e7b")
				.header("Accept", ContentType.JSON.getAcceptHeader()).contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().get("/api/modules/" + module.getId())
				.then().statusCode(404);
	}
}
