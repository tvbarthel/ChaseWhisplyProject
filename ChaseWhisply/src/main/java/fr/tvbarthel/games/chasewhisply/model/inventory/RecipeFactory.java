package fr.tvbarthel.games.chasewhisply.model.inventory;


public class RecipeFactory {
    public static Recipe create(int inventoryItemType) {
        Recipe recipe = new Recipe();
        switch (inventoryItemType) {

            case InventoryItemInformation.TYPE_BABY_DROOL:
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_COIN), 12);
                break;

            case InventoryItemInformation.TYPE_GHOST_TEAR:
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_COIN), 12);
                break;

            case InventoryItemInformation.TYPE_SPEED_POTION:
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_COIN), 100);
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_GHOST_TEAR), 15);
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_BABY_DROOL), 15);
                break;

            case InventoryItemInformation.TYPE_BROKEN_HELMET_HORN:
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_COIN), 10);
                break;

            case InventoryItemInformation.TYPE_KING_CROWN:
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_COIN), 95);
                break;

            case InventoryItemInformation.TYPE_STEEL_BULLET:
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_COIN), 50);
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_BROKEN_HELMET_HORN), 10);
                break;

            case InventoryItemInformation.TYPE_GOLD_BULLET:
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_COIN), 100);
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_KING_CROWN), 1);
                break;

            case InventoryItemInformation.TYPE_ONE_SHOT_BULLET:
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_COIN), 150);
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_KING_CROWN), 2);
                recipe.addIngredient(InventoryItemInformationFactory.create(InventoryItemInformation.TYPE_BROKEN_HELMET_HORN), 20);
                break;

        }

        return recipe;
    }
}
