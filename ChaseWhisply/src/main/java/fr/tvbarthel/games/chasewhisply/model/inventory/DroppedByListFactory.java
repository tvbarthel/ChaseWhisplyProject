package fr.tvbarthel.games.chasewhisply.model.inventory;

import fr.tvbarthel.games.chasewhisply.R;

public class DroppedByListFactory {
    public static DroppedByList create(int inventoryItemType) {
        DroppedByList droppedByList = new DroppedByList();
        switch (inventoryItemType) {
            case InventoryItemInformation.TYPE_BABY_DROOL:
                droppedByList.addMonster(R.string.bestiary_baby_ghost_title, DroppedByList.DROP_RATE_BABY_DROOL);
                break;

            case InventoryItemInformation.TYPE_BROKEN_HELMET_HORN:
                droppedByList.addMonster(R.string.bestiary_ghost_with_helmet_title, DroppedByList.DROP_RATE_BROKEN_HELMET_HORN);
                break;

            case InventoryItemInformation.TYPE_COIN:
                droppedByList.addMonster(R.string.bestiary_easy_ghost_title, DroppedByList.DROP_RATE_COIN);
                droppedByList.addMonster(R.string.bestiary_hidden_ghost_title, DroppedByList.DROP_RATE_COIN);
                droppedByList.addMonster(R.string.bestiary_baby_ghost_title, DroppedByList.DROP_RATE_COIN * 2);
                droppedByList.addMonster(R.string.bestiary_ghost_with_helmet_title, DroppedByList.DROP_RATE_COIN * 4);
                droppedByList.addMonster(R.string.bestiary_king_ghost_title, DroppedByList.DROP_RATE_COIN * 10);
                break;

            case InventoryItemInformation.TYPE_KING_CROWN:
                droppedByList.addMonster(R.string.bestiary_king_ghost_title, DroppedByList.DROP_RATE_KING_CROWN);
                break;

            case InventoryItemInformation.TYPE_GHOST_TEAR:
                droppedByList.addMonster(R.string.bestiary_blond_ghost_title, DroppedByList.DROP_RATE_GHOST_TEAR);
                break;
        }
        return droppedByList;
    }
}
