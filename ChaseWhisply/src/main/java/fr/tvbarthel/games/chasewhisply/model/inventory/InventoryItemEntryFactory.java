package fr.tvbarthel.games.chasewhisply.model.inventory;

public class InventoryItemEntryFactory {

	public static InventoryItemEntry create(int inventoryItemType, long quantityAvailable) {
		InventoryItemEntry inventoryItemEntry = new InventoryItemEntry();
		inventoryItemEntry.setInventoryItemInformation(InventoryItemInformationFactory.create(inventoryItemType));
		inventoryItemEntry.setQuantityAvailable(quantityAvailable);
		inventoryItemEntry.setDroppedBy(DroppedByListFactory.create(inventoryItemType));
		inventoryItemEntry.setRecipe(RecipeFactory.create(inventoryItemType));
		return inventoryItemEntry;
	}
}
