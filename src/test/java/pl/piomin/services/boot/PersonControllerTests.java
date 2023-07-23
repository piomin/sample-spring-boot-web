package pl.piomin.services.boot;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.piomin.services.boot.model.Person;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addAndGet() {
        Person person = restTemplate.postForObject("/persons", Instancio.create(Person.class), Person.class);
        assertNotNull(person);
        assertEquals(1, person.getId());

        Person[] persons = restTemplate.getForObject("/persons", Person[].class);
        assertTrue(persons.length > 0);
    }
}
