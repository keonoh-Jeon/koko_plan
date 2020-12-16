package com.koko_plan.sub;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.koko_plan.R;

import java.util.HashMap;

public class MySoundPlayer {
    public static final int CAMERA = R.raw.sound_cameraopen;
    public static final int PUTTING = R.raw.sound_putting;
    public static final int SWING = R.raw.sound_swing;
    public static final int TARGET = R.raw.sound_target;
    public static final int CLICK = R.raw.sound_click;
    public static final int PARSELECT = R.raw.sound_parselect;
    public static final int HOLEPOINT = R.raw.sound_holepoint;
    public static final int START = R.raw.start;

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
        soundPoolMap.put(CAMERA, soundPool.load(context, CAMERA, 1));
        soundPoolMap.put(PUTTING, soundPool.load(context, PUTTING, 2));
        soundPoolMap.put(SWING, soundPool.load(context, SWING, 3));
        soundPoolMap.put(TARGET, soundPool.load(context, TARGET, 4));
        soundPoolMap.put(CLICK, soundPool.load(context, CLICK, 5));
        soundPoolMap.put(PARSELECT, soundPool.load(context, PARSELECT, 6));
        soundPoolMap.put(HOLEPOINT, soundPool.load(context, HOLEPOINT, 7));
        soundPoolMap.put(START, soundPool.load(context, START, 8));
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
