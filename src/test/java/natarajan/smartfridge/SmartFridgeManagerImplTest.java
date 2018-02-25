package natarajan.smartfridge;

import static org.junit.Assert.assertEquals;
import natarajan.smartfridge.Item.ItemType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SmartFridgeManagerImplTest {

	SmartFridgeManager fridgeManager = new SmartFridgeManagerImpl();
	
	@Before
	public void setup() {		
	}
	
	@After
	public void tearDown() {		
		fridgeManager.handleItemRemoved("1");
		fridgeManager.handleItemRemoved("2");
		fridgeManager.handleItemRemoved("3");
		fridgeManager.handleItemRemoved("4");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testHandleItemRemoved() {
	
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 1.00);
		
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "2",
				"Lucerne Milk", 1.00);
		
		fridgeManager.handleItemRemoved("1");
		
		//1 full item is left
		assertEquals(1.00, fridgeManager.getFillFactor(1L).doubleValue(), 0.005);
	}

	@Test
	public void testHandleItemAdded() {
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 1.00);
		
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "2",
				"Lucerne Milk", 1.00);
		
		//overall fill factor is 1
		assertEquals(1.00, fridgeManager.getFillFactor(1L).doubleValue(), 0.005);
	}

	@Test
	public void testGetItems() {
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 1.00);
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "2",
				"Giant Milk", 1.00);
		fridgeManager.handleItemAdded(ItemType.CHOCOLATE_MILK.getValue(), "3",
				"Marva Maid", 1.00);
		fridgeManager.handleItemAdded(ItemType.CHOCOLATE_MILK.getValue(), "4",
				"Generic Chocolate", 1.00);
		
		fridgeManager.handleItemRemoved("1");
		fridgeManager.handleItemRemoved("2");
		fridgeManager.handleItemRemoved("3");
		
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 0.00);
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "2",
				"Lucerne Milk", 0.50);
		fridgeManager.handleItemAdded(ItemType.CHOCOLATE_MILK.getValue(), "3",
				"Marva Maid Chocolate Milk", 0.5);
		
		Object[] newDepletingItems = fridgeManager.getItems(0.50);
		
		assertEquals(1, newDepletingItems.length);
	}

	@Test
	public void testGetFillFactor() {
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 1.00);
		
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "2",
				"Lucerne Milk", 1.00);
		
		//overall fill factor is 1
		assertEquals(1.00, fridgeManager.getFillFactor(1L).doubleValue(), 0.005);
	}

	@Test
	public void testForgetItem() {
		
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 0.50);
		
		fridgeManager.handleItemAdded(ItemType.CHOCOLATE_MILK.getValue(), "3",
				"Marva Maid Chocolate Milk", 0.50);
		
		fridgeManager.forgetItem(ItemType.MILK.getValue());
		
		Object[] depletingItems = fridgeManager.getItems(0.50);
		
		assertEquals(1, depletingItems.length);
		
	}

}
