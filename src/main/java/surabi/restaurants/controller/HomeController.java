package surabi.restaurants.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String homePage() {
        return "<h1> Welcome home! </h1><a href=\"http://localhost:8080/swagger-ui/\">Swagger</a>";
    }
}
