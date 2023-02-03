package pl.piomin.services.boot;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.piomin.services.boot.model.Person;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addAndGet() {
        Person person = restTemplate.postForObject("/person", Instancio.create(Person.class), Person.class);
        assertNotNull(person);
        assertEquals(1, person.getId());

        person = restTemplate.getForObject("/person/{}", Person.class, 1);
        assertNotNull(person);
        assertEquals(1, person.getId());
    }
}
