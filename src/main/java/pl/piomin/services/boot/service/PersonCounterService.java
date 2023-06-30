package pl.piomin.services.boot.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonCounterService {

    private final Counter counterAdded;
    private final Counter counterDeleted;

    @Autowired
    public PersonCounterService(MeterRegistry registry) {
        this.counterAdded = registry.counter("services.person.add");
        this.counterDeleted = registry.counter("services.person.deleted");
    }

    public void countNewPersons() {
        this.counterAdded.increment();
    }

    public void countDeletedPersons() {
        this.counterDeleted.increment();
    }

}
