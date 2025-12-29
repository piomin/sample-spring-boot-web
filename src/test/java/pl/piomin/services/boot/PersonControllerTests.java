package pl.piomin.services.boot;

import org.instancio.Instancio;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;
import pl.piomin.services.boot.model.Person;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureRestTestClient
public class PersonControllerTests {

    private static final String API_PATH = "/persons";

//    private RestTestClient restTemplate;
//
//    public PersonControllerTests(WebApplicationContext context) {
//        this.restTemplate = RestTestClient.bindToApplicationContext(context)
//                .baseUrl(API_PATH)
//                .build();
//    }

    @Test
    @Order(1)
    void add(@Autowired RestTestClient restTestClient) {
        restTestClient.post().uri(API_PATH).body(Instancio.create(Person.class))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Person.class)
                .value(person -> assertNotNull(person.getId()));
    }

    @Test
    @Order(2)
    void findAll(@Autowired RestTestClient restTestClient) {
        restTestClient.get().uri(API_PATH)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Person[].class)
                .value(persons -> assertTrue(persons.length > 0));
    }

    @Test
    @Order(2)
    void findById(@Autowired RestTestClient restTestClient) {
        restTestClient.get().uri(API_PATH + "/{id}", 1L)
                .exchange()
                .expectBody(Person.class)
                .value(person -> assertNotNull(person.getId()));
    }

    @Test
    @Order(3)
    void update(@Autowired RestTestClient restTestClient) {
        Person person = restTestClient.get().uri(API_PATH + "/{id}", 1L)
                .exchange()
                .returnResult(Person.class)
                .getResponseBody();
        person.setFirstName("Updated");
        restTestClient.put().uri(API_PATH + "/{id}", 1L)
                .body(person)
                .exchange()
                .expectStatus().is2xxSuccessful();
        restTestClient.get().uri(API_PATH + "/{id}", 1L)
                .exchange()
                .expectBody(Person.class)
                .value(personUpdated -> assertEquals("Updated", personUpdated.getFirstName()));
    }

    @Test
    @Order(4)
    void delete(@Autowired RestTestClient restTestClient) {
        restTestClient.delete().uri(API_PATH + "/{id}", 1L)
                .exchange()
                .expectStatus().is2xxSuccessful();
        restTestClient.get().uri(API_PATH + "/{id}", 1L)
                .exchange()
                .expectStatus().is5xxServerError();
    }

}
