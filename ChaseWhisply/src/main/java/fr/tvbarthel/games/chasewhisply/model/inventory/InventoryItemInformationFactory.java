package fr.tvbarthel.games.chasewhisply.model.inventory;

import fr.tvbarthel.games.chasewhisply.R;

public class InventoryItemInformationFactory {

	public static InventoryItemInformation create(int inventoryItemType) {
		InventoryItemInformation inventoryItemInformation = new InventoryItemInformation();
		int titleResourceId = 0;
		int descriptionResourceId = 0;
		switch (inventoryItemType) {
			case InventoryItemInformation.TYPE_BABY_DROOL:
				titleResourceId = R.plurals.inventory_item_baby_drool_title;
				descriptionResourceId = R.string.inventory_item_baby_drool_description;
				break;

			case InventoryItemInformation.TYPE_BROKEN_HELMET_HORN:
				titleResourceId = R.plurals.inventory_item_broken_helmet_horn_title;
				descriptionResourceId = R.string.inventory_item_broken_helmet_horn_description;
				break;

			case InventoryItemInformation.TYPE_COIN:
				titleResourceId = R.plurals.inventory_item_coin_title;
				descriptionResourceId = R.string.inventory_item_coin_description;
				break;

			case InventoryItemInformation.TYPE_KING_CROWN:
				titleResourceId = R.plurals.inventory_item_king_crown_title;
				descriptionResourceId = R.string.inventory_item_king_crown_description;
				break;

			case InventoryItemInformation.TYPE_STEEL_BULLET:
				titleResourceId = R.plurals.inventory_item_steel_bullet_title;
				descriptionResourceId = R.string.inventory_item_steel_bullet_description;
				break;

			case InventoryItemInformation.TYPE_GOLD_BULLET:
				titleResourceId = R.plurals.inventory_item_gold_bullet_title;
				descriptionResourceId = R.string.inventory_item_gold_bullet_description;
				break;

			case InventoryItemInformation.TYPE_ONE_SHOT_BULLET:
				titleResourceId = R.plurals.inventory_item_one_shot_bullet_title;
				descriptionResourceId = R.string.inventory_item_one_shot_bullet_description;
				break;

			case InventoryItemInformation.TYPE_GHOST_TEAR:
				titleResourceId = R.plurals.inventory_item_ghost_tear_title;
				descriptionResourceId = R.string.inventory_item_ghost_tear_description;
				break;
		}
		inventoryItemInformation.setType(inventoryItemType);
		inventoryItemInformation.setTitleResourceId(titleResourceId);
		inventoryItemInformation.setDescriptionResourceId(descriptionResourceId);
		return inventoryItemInformation;
	}
}
