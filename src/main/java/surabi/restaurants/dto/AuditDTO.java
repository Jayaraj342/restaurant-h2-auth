package surabi.restaurants.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AuditDTO {
    private String user;
    private String items;
    private int totalPrice;
    private Date time;
}
