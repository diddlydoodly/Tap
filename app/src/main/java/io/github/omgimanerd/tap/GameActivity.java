package io.github.omgimanerd.tap;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.*;

import io.github.omgimanerd.tap.game.Sound;

public class GameActivity extends Activity {

  private View gameView_;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    Sound.loadSounds(this);
    gameView_ = new GameView(this);
    setContentView(gameView_);
  }

  public void onDestroy() {
    super.onDestroy();
    Sound.release();
    gameView_ = null;
  }
}
