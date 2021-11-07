package surabi.restaurants.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import surabi.restaurants.dto.AuditDTO;
import surabi.restaurants.entity.Audit;
import surabi.restaurants.entity.Item;
import surabi.restaurants.repository.AuditRepository;
import surabi.restaurants.repository.ItemRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SurabiRestaurantService {

    private final ItemRepository itemRepository;
    private final AuditRepository auditRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> selectItemsWithBill(List<Integer> ids) {
        return ids.stream().map(itemRepository::getOne).collect(Collectors.toList());
    }

    public String addLogToAudit(AuditDTO auditDTO) {
        auditRepository.save(
                Audit.builder()
                        .user(auditDTO.getUser())
                        .items(auditDTO.getItems())
                        .totalPrice(auditDTO.getTotalPrice())
                        .time(auditDTO.getTime())
                        .build()
        );
        return "Success!";
    }

    public String getLastBill() {
        List<Audit> audits = auditRepository.findAll();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = user.getUsername();

        Audit usersLastAudit = audits.stream().filter(audit -> audit.getUser().equals(userName))
                .findFirst()
                .orElse(Audit.builder().items("No items").totalPrice(0).build());
        return usersLastAudit.getItems() + " selected & Final bill is ₹" + usersLastAudit.getTotalPrice();
    }

    public String getLastOneDayBill() {

        Calendar today = getStartOfToday();

        List<Audit> audits = auditRepository.findAll();
        Date startOfToday = today.getTime();
        return audits.stream().filter(audit -> audit.getTime().after(startOfToday))
                .map(audit -> audit.getItems() + " Total: " + audit.getTotalPrice())
                .collect(Collectors.toList()).toString();
    }

    public String getLastOneMonthBill() {

        Calendar today = getStartOfToday();
        today.add(Calendar.DATE, -30);

        List<Audit> audits = auditRepository.findAll();
        Date startOfToday = today.getTime();
        return audits.stream().filter(audit -> audit.getTime().after(startOfToday))
                .map(audit -> audit.getItems() + " Total: " + audit.getTotalPrice())
                .collect(Collectors.toList()).toString();
    }

    private Calendar getStartOfToday() {
        Calendar today = new GregorianCalendar();
        today.setTime(new Date());
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }
}
