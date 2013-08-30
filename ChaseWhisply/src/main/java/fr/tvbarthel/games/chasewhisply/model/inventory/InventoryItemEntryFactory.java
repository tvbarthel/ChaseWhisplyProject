package fr.tvbarthel.games.chasewhisply.model.inventory;

import fr.tvbarthel.games.chasewhisply.R;

public class InventoryItemEntryFactory {

	public static InventoryItemEntry createInventoryEntry(int inventoryItemType, long quantityAvailable) {
		InventoryItemEntry inventoryItemEntry = new InventoryItemEntry();
		DroppedByList droppedByList = new DroppedByList();
		Recipe recipe = new Recipe();
		int titleResourceId = R.string.inventory_item_default_title;
		int descriptionResourceId = R.string.inventory_item_default_description;
		switch (inventoryItemType) {
			case InventoryItemEntry.TYPE_BABY_DROOL:
				titleResourceId = R.string.inventory_item_baby_drool_title;
				descriptionResourceId = R.string.inventory_item_baby_drool_description;
				droppedByList.addMonster(R.string.bestiary_baby_ghost_title, InventoryItemEntry.DROP_RATE_BABY_DROOL);
				break;

			case InventoryItemEntry.TYPE_BROKEN_HELMET_HORN:
				titleResourceId = R.string.inventory_item_broken_helmet_horn_title;
				descriptionResourceId = R.string.inventory_item_broken_helmet_horn_description;
				droppedByList.addMonster(R.string.bestiary_ghost_with_helmet_title, InventoryItemEntry.DROP_RATE_BROKEN_HELMET_HORN);
				break;

			case InventoryItemEntry.TYPE_COIN:
				titleResourceId = R.string.inventory_item_coin_title;
				descriptionResourceId = R.string.inventory_item_coin_description;
				droppedByList.addMonster(R.string.bestiary_easy_ghost_title, InventoryItemEntry.DROP_RATE_COIN);
				droppedByList.addMonster(R.string.bestiary_hidden_ghost_title, InventoryItemEntry.DROP_RATE_COIN);
				droppedByList.addMonster(R.string.bestiary_baby_ghost_title, InventoryItemEntry.DROP_RATE_COIN * 2);
				droppedByList.addMonster(R.string.bestiary_ghost_with_helmet_title, InventoryItemEntry.DROP_RATE_COIN * 4);
				droppedByList.addMonster(R.string.bestiary_king_ghost_title, InventoryItemEntry.DROP_RATE_COIN * 25);
				break;

			case InventoryItemEntry.TYPE_KING_CROWN:
				titleResourceId = R.string.inventory_item_king_crown_title;
				descriptionResourceId = R.string.inventory_item_king_crown_description;
				droppedByList.addMonster(R.string.bestiary_king_ghost_title, InventoryItemEntry.DROP_RATE_KING_CROWN);
				break;

			case InventoryItemEntry.TYPE_STEEL_BULLET:
				titleResourceId = R.string.inventory_item_steel_bullet_title;
				descriptionResourceId = R.string.inventory_item_steel_bullet_description;
				recipe.addIngredient(R.string.inventory_item_coin_title, 25);
				recipe.addIngredient(R.string.inventory_item_broken_helmet_horn_title, 10);
				break;

			case InventoryItemEntry.TYPE_GOLD_BULLET:
				titleResourceId = R.string.inventory_item_gold_bullet_title;
				descriptionResourceId = R.string.inventory_item_gold_bullet_description;
				recipe.addIngredient(R.string.inventory_item_coin_title, 100);
				recipe.addIngredient(R.string.inventory_item_king_crown_title, 1);
				break;
		}

		inventoryItemEntry.setTitleResourceId(titleResourceId);
		inventoryItemEntry.setDescriptionResourceId(descriptionResourceId);
		inventoryItemEntry.setQuantityAvailable(quantityAvailable);
		inventoryItemEntry.setDroppedBy(droppedByList);
		inventoryItemEntry.setRecipe(recipe);
		return inventoryItemEntry;
	}
}
