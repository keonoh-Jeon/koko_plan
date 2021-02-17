package com.koko_plan.sub;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.koko_plan.R;

import java.util.HashMap;

public class MySoundPlayer {
    public static final int TEAR = R.raw.tearingpaper;
    public static final int POP = R.raw.pop;
    public static final int PAGE = R.raw.page;
    public static final int PLAY = R.raw.playbutton;
    public static final int CLICK = R.raw.sound_click;
    public static final int PAUSE = R.raw.pausebutton;
    public static final int POP2 = R.raw.pop2;

    private static SoundPool soundPool;
    private static HashMap<Integer, Integer> soundPoolMap;

    // sound media initialize
    public static void initSounds(Context context) {
        AudioAttributes attributes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();
        }
        soundPoolMap = new HashMap<Integer, Integer>(8);
        soundPoolMap.put(TEAR, soundPool.load(context, TEAR, 1));
        soundPoolMap.put(POP, soundPool.load(context, POP, 2));
        soundPoolMap.put(PAGE, soundPool.load(context, PAGE, 3));
        soundPoolMap.put(PLAY, soundPool.load(context, PLAY, 4));
        soundPoolMap.put(CLICK, soundPool.load(context, CLICK, 5));
        soundPoolMap.put(PAUSE, soundPool.load(context, PAUSE, 6));
        soundPoolMap.put(POP2, soundPool.load(context, POP2, 7));
    }

    public static void play(int raw_id){
        if( soundPoolMap.containsKey(raw_id) ) {
            soundPool.play(soundPoolMap.get(raw_id), 1, 1, 1, 0, 1f);
        }
    }
}

/*      각 액티비티 초기화

        MySoundPlayer.initSounds(getApplicationContext());
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        */
