package fr.tvbarthel.games.chasewhisply.model.inventory;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public class Recipe implements Parcelable {
    private final HashMap<InventoryItemInformation, Integer> mIngredientsAndQuantities;

    public Recipe() {
        mIngredientsAndQuantities = new HashMap<InventoryItemInformation, Integer>();
    }

    public Recipe(Parcel in) {
        mIngredientsAndQuantities = new HashMap<InventoryItemInformation, Integer>();
        readFromParcel(in);
    }

    public void addIngredient(InventoryItemInformation inventoryItemInformation, Integer quantity) {
        if (mIngredientsAndQuantities.containsKey(inventoryItemInformation)) {
            quantity += mIngredientsAndQuantities.get(inventoryItemInformation);
        }
        mIngredientsAndQuantities.put(inventoryItemInformation, quantity);
    }

    public HashMap<InventoryItemInformation, Integer> getIngredientsAndQuantities() {
        return mIngredientsAndQuantities;
    }

    public HashMap<Integer, Integer> getMissingResources(PlayerProfile playerProfile) {
        final HashMap<Integer, Integer> missingResources = new HashMap<Integer, Integer>();

        for (Map.Entry<InventoryItemInformation, Integer> entry : mIngredientsAndQuantities.entrySet()) {
            int quantityRequested = entry.getValue();
            long quantityAvailable = playerProfile.getInventoryItemQuantity(entry.getKey().getType());
            if (quantityAvailable < quantityRequested) {
                missingResources.put(entry.getKey().getTitleResourceId(), (int) (quantityRequested - quantityAvailable));
            }
        }

        return missingResources;
    }

    public String toString(Context context) {
        String string = context.getString(R.string.inventory_item_can_t_be_crafted);
        if (mIngredientsAndQuantities.size() != 0) {
            string = "";
            for (Map.Entry<InventoryItemInformation, Integer> entry : mIngredientsAndQuantities.entrySet()) {
                final int quantity = entry.getValue();
                final int titleResourceId = entry.getKey().getTitleResourceId();
                string += context.getString(R.string.recipe_item_entry, quantity, context.getResources().getQuantityString(titleResourceId, quantity));
                string += " ";
            }
            string = string.substring(0, string.length() - 2);
        }
        return string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final int size = mIngredientsAndQuantities.size();
        dest.writeInt(size);
        for (Map.Entry<InventoryItemInformation, Integer> entry : mIngredientsAndQuantities.entrySet()) {
            dest.writeParcelable(entry.getKey(), flags);
            dest.writeInt(entry.getValue());
        }
    }

    public void readFromParcel(Parcel in) {
        final int size = in.readInt();
        for (int i = 0; i < size; i++) {
            final InventoryItemInformation key = in.readParcelable(InventoryItemInformation.class.getClassLoader());
            final int value = in.readInt();
            mIngredientsAndQuantities.put(key, value);
        }
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
