package io.github.omgimanerd.tap;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import io.github.omgimanerd.tap.game.Sound;

public class GameActivity extends Activity {

  private GameView gameView_;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setVolumeControlStream(AudioManager.STREAM_MUSIC);
    Sound.loadSounds(this);

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                         WindowManager.LayoutParams.FLAG_FULLSCREEN);

    gameView_ = new GameView(this);
    setContentView(gameView_);
  }

  public void onPause() {
    super.onPause();
    gameView_.onPause();
  }

  public void onDestroy() {
    super.onDestroy();
    Sound.release();
    gameView_ = null;
  }
}
