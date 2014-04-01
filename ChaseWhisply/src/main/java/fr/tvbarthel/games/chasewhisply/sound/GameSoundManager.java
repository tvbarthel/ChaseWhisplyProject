package fr.tvbarthel.games.chasewhisply.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.MathUtils;

public class GameSoundManager {
    public static final int SOUND_TYPE_GUN_SHOT = 0x00000001;
    public static final int SOUND_TYPE_DRY_SHOT = 0x00000002;
    public static final int SOUND_TYPE_GHOST_DEATH = 0x00000003;
    public static final int SOUND_TYPE_LAUGH = 0x00000004;
    public static final int SOUND_TYPE_LAUGH_RANDOM = 0x0000005;

    private final SparseIntArray mStreamIds;
    private final SparseBooleanArray mIsSoundLoaded = new SparseBooleanArray();

    private SoundPool mSoundPool;
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private int mGhostLaughRate;


    public GameSoundManager(Context context) {
        mGhostLaughRate = 0;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mStreamIds = new SparseIntArray();

        mMediaPlayer = MediaPlayer.create(context, R.raw.background);
        final float volume = getVolume(0.5f);
        mMediaPlayer.setVolume(volume, volume);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    switch (sampleId) {
                        case 1:
                            mIsSoundLoaded.put(R.raw.dry_gun_shot, true);
                            break;
                        case 2:
                            mIsSoundLoaded.put(R.raw.gun_shot_2, true);
                            break;
                        case 3:
                            mIsSoundLoaded.put(R.raw.ghost_death, true);
                            break;
                        case 4:
                            mIsSoundLoaded.put(R.raw.laugh_1, true);
                            break;
                    }
                }
            }
        });
        mStreamIds.put(R.raw.dry_gun_shot, mSoundPool.load(context, R.raw.dry_gun_shot, 1));
        mStreamIds.put(R.raw.gun_shot_2, mSoundPool.load(context, R.raw.gun_shot_2, 1));
        mStreamIds.put(R.raw.ghost_death, mSoundPool.load(context, R.raw.ghost_death, 1));
        mStreamIds.put(R.raw.laugh_1, mSoundPool.load(context, R.raw.laugh_1, 1));
    }

    public void requestSound(int soundType) {
        switch (soundType) {
            case SOUND_TYPE_DRY_SHOT:
                playDryGunShot();
                break;
            case SOUND_TYPE_GHOST_DEATH:
                playGhostDeath();
                break;
            case SOUND_TYPE_LAUGH:
                playGhostLaugh();
                break;
            case SOUND_TYPE_LAUGH_RANDOM:
                playGhostLaughRandom();
                break;
            case SOUND_TYPE_GUN_SHOT:
                playGunShot();
                break;
        }
    }

    public void playGunShot() {
        playSoundFromPool(R.raw.gun_shot_2);
    }

    public void playDryGunShot() {
        playSoundFromPool(R.raw.dry_gun_shot);
    }

    public void playGhostDeath() {
        playSoundFromPool(R.raw.ghost_death, 0.1f);
    }

    public void playGhostLaugh() {
        playSoundFromPool(R.raw.laugh_1, 0.2f);
    }

    public void playGhostLaughRandom() {
        mGhostLaughRate += 1;
        final int draft = MathUtils.randomize(0, 200);
        if (draft < mGhostLaughRate) {
            mGhostLaughRate = 0;
            playSoundFromPool(R.raw.laugh_1, 0.2f);
        }
    }

    private float getVolume(float volumeRatio) {
        final float actualVolume = (float) mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        final float maxVolume = (float) mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return volumeRatio * actualVolume / maxVolume;
    }

    private void playSoundFromPool(int soundResourceId, float volumeRatio) {
        if (mSoundPool != null && mIsSoundLoaded.get(soundResourceId, false)) {
            final float volume = getVolume(volumeRatio);
            mSoundPool.play(mStreamIds.get(soundResourceId), volume, volume, 1, 0, 1f);
        }
    }

    private void playSoundFromPool(int soundResourceId) {
        playSoundFromPool(soundResourceId, 1f);
    }

    public void stopAllSounds() {
        if (mSoundPool != null) {
            for (int i = 0; i < mStreamIds.size(); i++) {
                mSoundPool.stop(mStreamIds.valueAt(i));
            }
            mSoundPool.release();
            mSoundPool = null;
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.getCurrentPosition();
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
