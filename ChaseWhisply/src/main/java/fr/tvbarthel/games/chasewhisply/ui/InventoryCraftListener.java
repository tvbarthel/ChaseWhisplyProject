package fr.tvbarthel.games.chasewhisply.ui;

import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;

public interface InventoryCraftListener {
    public void onCraftRequested(InventoryItemEntry inventoryItemEntry);
}
