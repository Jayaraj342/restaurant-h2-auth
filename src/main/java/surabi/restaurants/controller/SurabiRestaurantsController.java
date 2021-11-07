package surabi.restaurants.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import surabi.restaurants.dto.AuditDTO;
import surabi.restaurants.entity.Item;
import surabi.restaurants.service.SurabiRestaurantService;

import java.util.List;

@RestController
@RequestMapping("/surabi-restaurant")
@AllArgsConstructor
public class SurabiRestaurantsController {

    private final SurabiRestaurantService surabiRestaurantService;

    @GetMapping("/view-menu")
    public List<Item> viewMenu() {
        return surabiRestaurantService.getAllItems();
    }

    @GetMapping("/select-menu-with-bill")
    public String selectMenuWithBill(@RequestParam List<Integer> ids) {
        List<Item> items = surabiRestaurantService.selectItemsWithBill(ids);
        int totalPrice = 0;
        for (Item item : items) {
            totalPrice += item.getPrice();
        }
        return items + " selected & Final bill is ₹" + totalPrice;
    }

    @GetMapping("/last-bill")
    public String lastBill() {
        return surabiRestaurantService.getLastBill();
    }

    @PostMapping("/add-log-to-audit")
    @ApiOperation(value = "", hidden = true)
    public String addLogToAudit(AuditDTO auditDTO) {
        return surabiRestaurantService.addLogToAudit(auditDTO);
    }

    @GetMapping("/last-one-day-bill")
    public String lastOneDayBill() {
        return surabiRestaurantService.getLastOneDayBill();
    }

    @GetMapping("/last-one-month-bill")
    public String lastOneMonthBill() {
        return surabiRestaurantService.getLastOneDayBill();
    }
}
