package surabi.restaurants.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import surabi.restaurants.dto.AuditDTO;
import surabi.restaurants.entity.Item;
import surabi.restaurants.service.SurabiRestaurantService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SurabiRestaurantsController.class)
@AutoConfigureTestDatabase
class SurabiRestaurantsControllerTest {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private SurabiRestaurantService surabiRestaurantService;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void viewMenu() throws Exception {
        Item idli = Item.builder().id(1).name("Idli").numberOfItemsAvailable(20).price(30).build();
        Item vada = Item.builder().id(2).name("Vada").numberOfItemsAvailable(10).price(40).build();
        List<Item> items = List.of(idli, vada);

        when(surabiRestaurantService.getAllItems()).thenReturn(items);
        mockMvc.perform(get("/surabi-restaurant/view-menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("Idli")));
    }

    @Test
    void selectMenuWithBill() throws Exception {
        Item idli = Item.builder().id(1).name("Idli").numberOfItemsAvailable(20).price(30).build();
        Item vada = Item.builder().id(2).name("Vada").numberOfItemsAvailable(10).price(40).build();
        List<Item> items = List.of(idli, vada);

        when(surabiRestaurantService.selectItemsWithBill(List.of(1, 2))).thenReturn(items);
        mockMvc.perform(get("/surabi-restaurant/select-menu-with-bill").param("ids", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "[Item(id=1, name=Idli, numberOfItemsAvailable=20, price=30), Item(id=2, name=Vada, numberOfItemsAvailable=10, price=40)] selected & Final bill is ₹70"
                )));
    }

    @Test
    void lastBill() throws Exception {
        when(surabiRestaurantService.getLastBill()).thenReturn("bill");
        mockMvc.perform(get("/surabi-restaurant/last-bill"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "bill"
                )));
    }

    @Test
    void addLogToAudit() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        AuditDTO paramObject = AuditDTO.builder().build();
        when(surabiRestaurantService.addLogToAudit(paramObject)).thenReturn("Success!");
        mockMvc.perform(post("/surabi-restaurant/add-log-to-audit")
                        .param("auditDTO", objectMapper.writeValueAsString(paramObject))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "Success!"
                )));
    }

    @Test
    void lastOneDayBill() throws Exception {
        when(surabiRestaurantService.getLastOneDayBill()).thenReturn("bill");
        mockMvc.perform(get("/surabi-restaurant/last-one-day-bill"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "bill"
                )));
    }

    @Test
    void lastOneMonthBill() throws Exception {
        when(surabiRestaurantService.getLastOneMonthBill()).thenReturn("bill");
        mockMvc.perform(get("/surabi-restaurant/last-one-month-bill"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "bill"
                )));
    }
}