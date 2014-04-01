package fr.tvbarthel.games.chasewhisply.model.inventory;

import java.util.ArrayList;

public class InventoryItemEntryFactory {

    private static ArrayList<Integer> frenchFeminineGenderItemTypes = new ArrayList<Integer>();

    static {
        frenchFeminineGenderItemTypes.add(InventoryItemInformation.TYPE_ONE_SHOT_BULLET);
        frenchFeminineGenderItemTypes.add(InventoryItemInformation.TYPE_STEEL_BULLET);
        frenchFeminineGenderItemTypes.add(InventoryItemInformation.TYPE_GOLD_BULLET);
        frenchFeminineGenderItemTypes.add(InventoryItemInformation.TYPE_COIN);
        frenchFeminineGenderItemTypes.add(InventoryItemInformation.TYPE_BABY_DROOL);
        frenchFeminineGenderItemTypes.add(InventoryItemInformation.TYPE_KING_CROWN);
        frenchFeminineGenderItemTypes.add(InventoryItemInformation.TYPE_BROKEN_HELMET_HORN);
        frenchFeminineGenderItemTypes.add(InventoryItemInformation.TYPE_GHOST_TEAR);
    }

    public static InventoryItemEntry create(int inventoryItemType, long quantityAvailable) {
        InventoryItemEntry inventoryItemEntry = new InventoryItemEntry();
        inventoryItemEntry.setInventoryItemInformation(InventoryItemInformationFactory.create(inventoryItemType));
        inventoryItemEntry.setQuantityAvailable(quantityAvailable);
        inventoryItemEntry.setDroppedBy(DroppedByListFactory.create(inventoryItemType));
        inventoryItemEntry.setRecipe(RecipeFactory.create(inventoryItemType));
        if (frenchFeminineGenderItemTypes.contains(inventoryItemType)) {
            inventoryItemEntry.setFrenchFeminineGender(true);
        }
        return inventoryItemEntry;
    }
}
