package surabi.restaurants.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import surabi.restaurants.dto.AuditDTO;
import surabi.restaurants.service.SurabiRestaurantService;

import java.util.Date;

@Aspect
@Configuration
@Slf4j
@AllArgsConstructor
public class AuditLogConfiguration {

    private final SurabiRestaurantService surabiRestaurantService;

    @AfterReturning(value = "execution(* surabi.restaurants.controller.SurabiRestaurantsController.selectMenuWithBill(..))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, String result) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String[] itemsWithTotal = result.split("selected &");
        String items = itemsWithTotal[0].trim();
        String total = itemsWithTotal[1].replace("Final bill is ₹", "").trim();
        surabiRestaurantService.addLogToAudit(
                AuditDTO.builder()
                        .user(user.getUsername())
                        .items(items)
                        .totalPrice(Integer.parseInt(total))
                        .time(new Date())
                        .build()
        );
    }
}
