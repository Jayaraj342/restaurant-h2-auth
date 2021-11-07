package surabi.restaurants.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	@Id
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "NUMBER_OF_ITEMS_AVAILABLE")
	private int numberOfItemsAvailable;
	
	@Column(name = "PRICE")
	private int price;

}
