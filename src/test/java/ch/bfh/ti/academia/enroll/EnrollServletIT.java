package ch.bfh.ti.academia.enroll;

import ch.bfh.ti.academia.person.PersonRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EnrollServletIT {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "827ccb0eea8a706c4c34a16891f84e7b";
    private static final Long ID = 1L;
    private static final String GRADE = "*";
    private static EnrollRepository enrollRepository;
    private static PersonRepository personRepository;
    private static final Enroll enroll = new Enroll(ID, ID, ID, GRADE);

    @BeforeAll
    public static void init() throws SQLException {
        RestAssured.port = 8080;
    }

    @Test
    @Order(1)
    public void addEnroll() {
        long id = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .body(enroll)
                .when().post("/api/enrollments")
                .then().statusCode(201).extract().as(Enroll.class).getId();
        enroll.setId(id);
        Enroll addedEnroll = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .when().get("/api/enrollments/"+ id)
                .then().statusCode(302).extract().as(Enroll.class);
        assertEquals(enroll, addedEnroll);
    }

    @Test
    @Order(2)
    public void getEnroll() {
        RestAssured
                .given().accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .when().get("/api/enrollments/")
                .then().statusCode(405);
    }

    @Test
    @Order(3)
    public void updateEnroll() {
        enroll.setGrade("A");
        RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .body(enroll)
                .when().put("/api/enrollments/"+ enroll.getId())
                .then().statusCode(204);
        Enroll updatedEnroll = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .when().get("/api/enrollments/"+ enroll.getId())
                .then().statusCode(302).extract().as(Enroll.class);
        assertEquals(enroll, updatedEnroll);
    }

    @Test
    @Order(4)
    public void removeEnroll() {
        RestAssured
                .given().auth().preemptive().basic(USERNAME, PASSWORD)
                .when().delete("/api/enrollments/" + enroll.getId())
                .then().statusCode(204);
        RestAssured
                .given().accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .when().get("/api/enrollments/" + enroll.getId())
                .then().statusCode(404);
    }

}
