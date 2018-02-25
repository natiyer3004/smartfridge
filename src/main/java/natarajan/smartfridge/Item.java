package natarajan.smartfridge;

import java.util.HashMap;
import java.util.Map;

public class Item {

	private String UUID;
	private ItemType type;
	private String name;
	private Double fillFactor;
	
	public enum ItemType {	
	    MILK (1L),
	    CHOCOLATE_MILK   (2L),
	    YOGURT (3L),
	    KETCHUP (4L),
	    MAYONNAISE (5L),
	    REGULAR_HAM (6L),
	    CHICKEN_HAM (7L),
	    TOMATO (8L),
	    JALAPENO (9L),
	    CARROT (10L),
	    ORANGE_JUICE (11L),
	    APPLE_JUICE (12L),
	    DEFAULT_ITEM (0L);

	    private final long type; 
	    
	    public long getValue() {
			return type;  	
	    }
	    
	    private static final Map<Long, ItemType> itemTypeMap = new HashMap<Long, ItemType>();
	    
	    static {
	         for(ItemType itemType : ItemType.values())
	        	 itemTypeMap.put(itemType.type, itemType);
	     }
	  
	    ItemType(long type) {
	        this.type = type;
	    }
	    
	    public static ItemType get(long value) { 
	          return itemTypeMap.get(value); 
	    }
	}
	
	public Item(String uUID, ItemType type, String name, Double fillFactor) {
		super();
		UUID = uUID;
		this.type = type;
		this.name = name;
		this.fillFactor = fillFactor;
	}
	
	/* Default Constructor *
	 * 
	 */
	public Item(String uUID) {
		super();
		this.UUID = uUID;
		this.type = ItemType.DEFAULT_ITEM;
		this.name = "Default Item";
		this.fillFactor = 1.00;
	}
	
	public String getUUID() {
		return UUID;
	}
	
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	
	public ItemType getType() {
		return type;
	}
	
	public void setType(ItemType type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Double getFillFactor() {
		return fillFactor;
	}
	
	public void setFillFactor(Double fillFactor) {
		this.fillFactor = fillFactor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((UUID == null) ? 0 : UUID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (UUID == null) {
			if (other.UUID != null)
				return false;
		} else if (!UUID.equals(other.UUID))
			return false;
		return true;
	}

}
