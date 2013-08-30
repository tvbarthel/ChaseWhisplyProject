package fr.tvbarthel.games.chasewhisply.model.inventory;

import java.util.HashMap;

public class Recipe {
	private final HashMap<Integer, Integer> mIngredientsAndQuantities;

	public Recipe() {
		mIngredientsAndQuantities = new HashMap<Integer, Integer>();
	}

	public void addIngredient(Integer ingredientNameResourceId, Integer quantity) {
		if (mIngredientsAndQuantities.containsKey(ingredientNameResourceId)) {
			quantity += mIngredientsAndQuantities.get(ingredientNameResourceId);
		}
		mIngredientsAndQuantities.put(ingredientNameResourceId, quantity);
	}

	public HashMap<Integer, Integer> getIngredientsAndQuantities() {
		return mIngredientsAndQuantities;
	}
}
