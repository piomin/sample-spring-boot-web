package pl.piomin.services.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.piomin.services.boot.model.Person;
import pl.piomin.services.boot.service.PersonCounterService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final List<Person> persons = new ArrayList<>();

    @Autowired
    PersonCounterService counterService;

    @GetMapping
    public List<Person> findAll() {
        return persons;
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable("id") Long id) {
        return persons.stream().filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    @GetMapping("/name/{firstName}/{lastName}")
    public List<Person> findById(@PathVariable("firstName") String firstName,
                                 @PathVariable("lastName") String lastName) {
        return persons.stream().filter(it -> it.getFirstName().equals(firstName)
                        && it.getLastName().equals(lastName))
                .toList();
    }

    @PostMapping
    public Person add(@RequestBody Person p) {
        p.setId((long) (persons.size() + 1));
        persons.add(p);
        counterService.countNewPersons();
        return p;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        Person p = persons.stream().filter(it -> it.getId().equals(id)).findFirst().orElseThrow();
        persons.remove(p);
        counterService.countDeletedPersons();
    }

    @PutMapping
    public void update(@RequestBody Person p) {
        Person person = persons.stream().filter(it -> it.getId().equals(p.getId())).findFirst().orElseThrow();
        persons.set(persons.indexOf(person), p);
    }

}
