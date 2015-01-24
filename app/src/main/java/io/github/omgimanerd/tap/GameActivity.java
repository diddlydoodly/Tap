package io.github.omgimanerd.tap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


public class GameActivity extends Activity {

  public static final String TAG = GameActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View gameView = new GameView(this);
    setContentView(gameView);
  }
}
