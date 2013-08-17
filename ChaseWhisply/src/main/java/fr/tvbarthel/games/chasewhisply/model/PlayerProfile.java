package fr.tvbarthel.games.chasewhisply.model;


import android.content.SharedPreferences;

public class PlayerProfile {
	public static final String SHARED_PREFERENCES_NAME = "PlayerProfile";
	public static final String KEY_BULLETS_FIRED = "keyBulletsFired";
	public static final String KEY_GAMES_PLAYED = "keyGamesPlayed";
	public static final String KEY_TARGETS_KILLED = "keyTargetsKilled";
	public static final String KEY_BULLETS_MISSED = "keyBulletsMissed";
	public static final String KEY_EXPERIENCE_EARNED = "keyExperienceEarned";
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;

	public PlayerProfile(SharedPreferences sharedPreferences) {
		mSharedPreferences = sharedPreferences;
		mEditor = mSharedPreferences.edit();
	}

	private void increaseSharedLongInteger(String key, long amount) {
		long value = mSharedPreferences.getLong(key, 0);
		value += amount;
		mEditor.putLong(key, value);
	}

	private long getSharedLongInteger(String key) {
		return mSharedPreferences.getLong(key, 0);
	}

	public void increaseTargetsKilled(long amount) {
		increaseSharedLongInteger(KEY_TARGETS_KILLED, amount);
	}

	public long getTargetsKilled() {
		return getSharedLongInteger(KEY_TARGETS_KILLED);
	}

	public void increaseGamesPlayed(long amount) {
		increaseSharedLongInteger(KEY_GAMES_PLAYED, amount);
	}

	public long getGamesPlayed() {
		return getSharedLongInteger(KEY_GAMES_PLAYED);
	}

	public void increaseBulletsFired(long amount) {
		increaseSharedLongInteger(KEY_BULLETS_FIRED, amount);
	}

	public long getBulletsFired() {
		return getSharedLongInteger(KEY_BULLETS_FIRED);
	}

	public boolean saveChanges() {
		return mEditor.commit();
	}

	public void increaseBulletsMissed(long amount) {
		increaseSharedLongInteger(KEY_BULLETS_MISSED, amount);
	}

	public int getAccuracy() {
		final long bulletsFired = getSharedLongInteger(KEY_BULLETS_FIRED);
		if (bulletsFired == 0) return 0;
		final long bulletsMissed = getSharedLongInteger(KEY_BULLETS_MISSED);
		return (int) ((bulletsFired - bulletsMissed) * 100f / bulletsFired);
	}

	public void increaseExperienceEarned(long amount) {
		increaseSharedLongInteger(KEY_EXPERIENCE_EARNED, amount);
	}

	public int getLevelStep(int level) {
		return (int) (100 * level + Math.pow(level, 2.75));
	}

	public LevelInformation getLevelInformation() {
		long experienceEarned = getSharedLongInteger(KEY_EXPERIENCE_EARNED);
		int level = -1;
		long currentStep = 0;
		long nextStep = 0;

		do {
			level++;
			currentStep = getLevelStep(level);
			nextStep = getLevelStep(level + 1);
		} while (experienceEarned > nextStep);

		return new LevelInformation(level, experienceEarned, currentStep, nextStep);
	}

}
