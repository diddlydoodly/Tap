package io.github.omgimanerd.tap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.*;

public class GameActivity extends Activity {

  private View gameView_;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    gameView_ = new GameView(this);
    setContentView(gameView_);
  }
}
