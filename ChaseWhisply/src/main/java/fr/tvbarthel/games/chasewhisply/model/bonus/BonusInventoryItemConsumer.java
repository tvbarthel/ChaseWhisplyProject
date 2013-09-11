package fr.tvbarthel.games.chasewhisply.model.bonus;

import android.os.Parcel;

import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public interface BonusInventoryItemConsumer {
	public void consume(PlayerProfile playerProfile);
}
