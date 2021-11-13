package surabi.restaurants.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import surabi.restaurants.dto.AuditDTO;
import surabi.restaurants.entity.Audit;
import surabi.restaurants.entity.Item;
import surabi.restaurants.repository.AuditRepository;
import surabi.restaurants.repository.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SurabiRestaurantServiceTest {

    private SurabiRestaurantService surabiRestaurantService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AuditRepository auditRepository;

    @BeforeEach
    void init() {
        surabiRestaurantService = new SurabiRestaurantService(itemRepository, auditRepository);
    }

    @Test
    void getAllItems() {
        List<Item> items = List.of(
                Item.builder().id(1).name("Idli").numberOfItemsAvailable(20).price(30).build(),
                Item.builder().id(2).name("Vada").numberOfItemsAvailable(10).price(40).build()
        );
        when(itemRepository.findAll()).thenReturn(items);
        List<Item> response = surabiRestaurantService.getAllItems();

        assertEquals(items, response);
    }

    @Test
    void selectItemsWithBill() {
        Item idli = Item.builder().id(1).name("Idli").numberOfItemsAvailable(20).price(30).build();
        Item vada = Item.builder().id(2).name("Vada").numberOfItemsAvailable(10).price(40).build();

        when(itemRepository.getOne(1)).thenReturn(idli);
        when(itemRepository.getOne(2)).thenReturn(vada);
        List<Item> response = surabiRestaurantService.selectItemsWithBill(List.of(1, 2));

        assertEquals(List.of(idli, vada), response);
    }

    @Test
    void addLogToAudit() {
        String response = surabiRestaurantService.addLogToAudit(AuditDTO.builder().build());
        assertEquals("Success!", response);
    }

    @Test
    void getLastBill() {
        List<Audit> audits = List.of(Audit.builder().totalPrice(100).items("items").user("dummyUser").build());

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        User user = mock(User.class);

        when(auditRepository.findAll()).thenReturn(audits);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(user.getUsername()).thenReturn("dummyUser");
        SecurityContextHolder.setContext(securityContext);

        String response = surabiRestaurantService.getLastBill();

        assertEquals("items selected & Final bill is ₹100", response);
    }

    @Test
    void getLastOneDayBill() {
        String response = surabiRestaurantService.getLastOneDayBill();
        assertEquals("[]", response);
    }

    @Test
    void getLastOneMonthBill() {
        String response = surabiRestaurantService.getLastOneMonthBill();
        assertEquals("[]", response);
    }
}