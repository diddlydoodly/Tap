package io.github.omgimanerd.tap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


public class GameActivity extends Activity {

  public static final int BACKGROUND_COLOR = Color.BLACK;

  public static final String TAG = GameActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View gameView = new GameView(this);
    gameView.setBackgroundColor(BACKGROUND_COLOR);
    setContentView(gameView);
  }
}
