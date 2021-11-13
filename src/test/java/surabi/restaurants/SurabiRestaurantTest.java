package surabi.restaurants;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import surabi.restaurants.controller.HomeController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SurabiRestaurantTest {

    @Autowired
    private HomeController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}