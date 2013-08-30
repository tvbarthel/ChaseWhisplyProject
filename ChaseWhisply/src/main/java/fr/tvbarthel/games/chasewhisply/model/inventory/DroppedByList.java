package fr.tvbarthel.games.chasewhisply.model.inventory;

import java.util.HashMap;

public class DroppedByList {
	private final HashMap<Integer, Integer> mMonstersAndPercents;

	public DroppedByList() {
		mMonstersAndPercents = new HashMap<Integer, Integer>();
	}

	public void addMonster(Integer monsterNameResourceId, Integer percent) {
		if (mMonstersAndPercents.containsKey(monsterNameResourceId)) {
			percent += mMonstersAndPercents.get(monsterNameResourceId);
		}
		mMonstersAndPercents.put(monsterNameResourceId, percent);
	}

	public HashMap<Integer, Integer> getMonstersAndPercents() {
		return mMonstersAndPercents;
	}
}
