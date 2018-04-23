package cz.vsimanek.closeit.test.rest;

import cz.vsimanek.closeit.test.service.FlightDelayService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Vlada Simanek
 */
@RestController
public class IndexController {
    
    @Autowired
    private FlightDelayService flightDelayService;

    @RequestMapping("/")
    public String index() throws IOException {
        return String.format("Average delay for Los Angeles flight arrival in 1989 is %s minutes.", flightDelayService.averageFlightsDelayIn1989());
    }
}
