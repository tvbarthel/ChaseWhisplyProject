package fr.tvbarthel.games.chasewhisply.model.bonus;

import fr.tvbarthel.games.chasewhisply.model.PlayerProfile;

public interface BonusInventoryItemConsumer {
    public Bonus consume(PlayerProfile playerProfile);
}
