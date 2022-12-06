package ch.bfh.ti.academia.person;

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
public class PersonServletIT {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "827ccb0eea8a706c4c34a16891f84e7b";
    private static final Person person = new Person(1L,"male","firstname","lastname", "1999-01-01","firstname.lastname@students.bh.ch", "username","827ccb0eea8a706c4c34a16891f84e7b","student", true);
    private static PersonRepository personRepository;

    @BeforeAll
    public static void init() throws SQLException {
        RestAssured.port = 8080;
    }

    @Test
    @Order(1)
    public void addPerson (){
        long id = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .body(person)
                .when().post("/api/persons/")
                .then().statusCode(201).extract().as(Person.class).getPersonal_id();
        person.setPersonal_id(id);
        Person addedPerson = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .when().get("/api/persons/"+ id)
                .then().statusCode(200).extract().as(Person.class);
        assertEquals(person, addedPerson);
    }

    @Test
    @Order(2)
    public void getPerson() {
        RestAssured
                .given().accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .when().get("/api/persons/")
                .then().statusCode(405);
    }

    @Test
    @Order(3)
    public void updatePerson() {
        person.setUsername("newUsername");
        RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .body(person)
                .when().put("/api/persons/")
                .then().statusCode(204);
        Person updatedPerson = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .when().get("/api/persons/"+ person.getPersonal_id())
                .then().statusCode(200).extract().as(Person.class);
        assertEquals(person, updatedPerson);
    }

    @Test
    @Order(4)
    public void removePerson() {
        RestAssured
                .given().auth().preemptive().basic(USERNAME, PASSWORD)
                .when().delete("/api/persons/" + person.getPersonal_id())
                .then().statusCode(204);
        RestAssured
                .given().accept(ContentType.JSON)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .when().get("/api/persons/" + person.getPersonal_id())
                .then().statusCode(404);
    }
}
