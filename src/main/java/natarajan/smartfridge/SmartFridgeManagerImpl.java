package natarajan.smartfridge;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import natarajan.smartfridge.Item.ItemType;

public class SmartFridgeManagerImpl implements SmartFridgeManager {

	private final Map<ItemType, List<Item>> fridgeItems = new HashMap<ItemType, List<Item>>();
	private final List<ItemType> forgetItems = new ArrayList<ItemType>();

	public void handleItemRemoved(String itemUUID) {
		removeItem(itemUUID);
	}

	public void handleItemAdded(long type, String itemUUID, String name,
			Double fillFactor) {

		// check if the item type is valid
		ItemType itemType = checkItemType(type);
		if (itemType == null)
			return;

		Item item = new Item(itemUUID, itemType, name, fillFactor);

		// check if the item type (e.g., "Milk", "Ketchup", etc.) is present in
		// the fridge
		List<Item> items = fridgeItems.get(itemType);

		if (items != null && !items.isEmpty()) {
			// check if this item is being re-inserted
			int index = items.indexOf(item);
			if (index > 0) {
				// remove
				items.remove(index);
			}
			// add the item
			items.add(item);
		} else { // create a new list for the item type
			List<Item> newItems = new ArrayList<Item>();
			newItems.add(item);
			fridgeItems.put(itemType, newItems);
		}

	}

	private void removeItem(String itemUUID) {
		Item item = new Item(itemUUID);
		fridgeItems.entrySet().stream()
				.forEach(entry -> entry.getValue().remove(item));
	}

	public Object[] getItems(Double fillFactor) {
		// get the overall fill factors for each item type
		Map<Long, Double> fillFactorByItemTypeMap = getOverallFillFactorByItemType();
		return fillFactorByItemTypeMap.entrySet().stream()
				.filter(entry -> entry.getValue() <= fillFactor)
				.collect(Collectors.toList()).toArray();
	}

	private Map<Long, Double> getOverallFillFactorByItemType() {

		Map<Long, Double> fillFactorByItemTypeMap = new HashMap<Long, Double>();
		for (Map.Entry<ItemType, List<Item>> entry : fridgeItems.entrySet()) {
			// skip item type if the owner does not want to track it
			if (forgetItems.contains(entry.getKey()))
				continue;

			Double overallFillFactor = 0.00;
			int countItems = 0;

			for (Item item : entry.getValue()) {

				if (item.getFillFactor() > 0.00) {
					overallFillFactor += item.getFillFactor();
					countItems++;
				}
			}

			if (countItems > 0) {
				fillFactorByItemTypeMap.put(entry.getKey().getValue(),
						overallFillFactor / countItems);
			} else {
				fillFactorByItemTypeMap.put(entry.getKey().getValue(), 0.00);
			}

		}
		return fillFactorByItemTypeMap;
	}

	public Double getFillFactor(long itemType) {
		return getOverallFillFactorByItemType().get(itemType);
	}

	public void forgetItem(long type) {
		// check if the item type is valid
		ItemType itemType = checkItemType(type);
		if (itemType != null)
			forgetItems.add(itemType);
	}

	private ItemType checkItemType(long type) {
		ItemType itemType = ItemType.get(type);
		return itemType;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		// test case 1: add 4 different item types, remove 3 & re-insert all 3
		// with 1) one empty 2) one half full 3) one 70% full
		SmartFridgeManager fridgeManager = new SmartFridgeManagerImpl();

		// step 1: add 4 items
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 1.00);
		fridgeManager.handleItemAdded(ItemType.CHOCOLATE_MILK.getValue(), "2",
				"Marva Maid Chocolate Milk", 1.00);
		fridgeManager.handleItemAdded(ItemType.CARROT.getValue(), "3",
				"Odwalla Carrot", 1.00);
		fridgeManager.handleItemAdded(ItemType.ORANGE_JUICE.getValue(), "4",
				"", 1.00);

		fridgeManager.handleItemRemoved("1");
		fridgeManager.handleItemRemoved("2");
		fridgeManager.handleItemRemoved("3");

		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 0.00);
		fridgeManager.handleItemAdded(ItemType.CHOCOLATE_MILK.getValue(), "2",
				"Marva Maid Chocolate Milk", 1.00);
		fridgeManager.handleItemAdded(ItemType.CARROT.getValue(), "3",
				"Odwalla Carrot", 0.50);
		fridgeManager.handleItemAdded(ItemType.ORANGE_JUICE.getValue(), "4",
				"", 0.70);

		Object[] depletingItems = fridgeManager.getItems(0.50);

		assertEquals(2, depletingItems.length);

		for (int i = 0; i < depletingItems.length; i++) {
			Map.Entry<Long, Double> depletingItem = (Map.Entry<Long, Double>) depletingItems[i];
			if (depletingItem.getValue() == 0.00)
				assertEquals(1L, depletingItem.getKey().longValue());
			else if (depletingItem.getValue() == 0.50)
				assertEquals(10L, depletingItem.getKey().longValue());
		}

		// teardown
		fridgeManager.handleItemRemoved("1");
		fridgeManager.handleItemRemoved("2");
		fridgeManager.handleItemRemoved("3");
		fridgeManager.handleItemRemoved("4");

		// test case 2: add 2 different item types, 2 items for each item type
		// (e.g, 2 items of milk and chocolate milk
		// replace one item half full, one item empty
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 1.00);
		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "2",
				"Giant Milk", 1.00);
		fridgeManager.handleItemAdded(ItemType.CHOCOLATE_MILK.getValue(), "3",
				"Marva Maid", 1.00);
		fridgeManager.handleItemAdded(ItemType.CHOCOLATE_MILK.getValue(), "4",
				"Hersheys Chocolate", 1.00);

		fridgeManager.handleItemRemoved("1");
		fridgeManager.handleItemRemoved("3");

		fridgeManager.handleItemAdded(ItemType.MILK.getValue(), "1",
				"Lucerne Milk", 0.00);
		fridgeManager.handleItemAdded(ItemType.CHOCOLATE_MILK.getValue(), "3",
				"Marva Maid Chocolate Milk", 0.5);

		Object[] newDepletingItems = fridgeManager.getItems(0.50);

		assertEquals(1, newDepletingItems.length);

	}

}
