package com.test.controller;

import com.test.model.Person;
import org.eclipse.microprofile.faulttolerance.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
public class PersonController {

    List<Person> peopleList = new ArrayList<>();
    Logger LOGGER = Logger.getLogger("Demologger");

    @GET
    //@Retry(maxRetries = 4)
    //@Timeout(value = 5000L)
    //@CircuitBreaker(failureRatio = 0.1, delay = 15000L)
    @Bulkhead(value = 1)
    @Fallback(fallbackMethod = "getPeopleFallbackList")
    public List<Person> getPeopleList()
    {
        LOGGER.info("Executing popleList");
        doFail();
        //doWait();
        return this.peopleList;
    }

    public List<Person> getPeopleFallbackList() {
        var person = new Person(1L, "Angel", "test@test.com");
        return List.of(person);
    }

    //Simulate a timeout

    public void doWait()
    {
        var random = new Random();

        try {
            Thread.sleep(random.nextInt(10) + 6 * 1000L);
        } catch (Exception e) {

        }
    }

    //Simulate a fault

    public void doFail()
    {
        var random = new Random();

        if(random.nextBoolean())
        {
            LOGGER.warning("Ups! It does not work");
            throw new RuntimeException("Doing the application fails");
        }
    }

}
