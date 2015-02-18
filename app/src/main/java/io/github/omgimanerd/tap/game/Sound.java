package io.github.omgimanerd.tap.game;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

import io.github.omgimanerd.tap.R;

/**
 * Created by omgimanerd on 1/26/15.
 */
public class Sound {

  private static SoundPool soundPool_;
  private static HashMap sounds_;

  public static void loadSounds(Context context) {
    soundPool_ = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
    sounds_ = new HashMap();
    sounds_.put("blop", new Integer(soundPool_.load(context, R.raw.blop, 1)));
    sounds_.put("lost", new Integer(soundPool_.load(context, R.raw.lost, 1)));
    sounds_.put("new_highscore", new Integer(soundPool_.load(
        context, R.raw.new_highscore, 1)));
  }

  public static void play(String soundName) {
    if (sounds_.containsKey(soundName )) {
      soundPool_.play((Integer) sounds_.get(soundName), 0, 1, 1, 0, 1);
    } else {
      throw new Error("Something bad happened, sound does not exist :/");
    }
  }

  public static void release() {
    soundPool_.release();
    soundPool_ = null;
  }
}