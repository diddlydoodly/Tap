package io.github.omgimanerd.tap;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import io.github.omgimanerd.tap.game.Game;

/**
 * Created by omgimanerd on 1/23/15.
 */
public class GameView extends View {

  private float screenHeight_;
  private float screenWidth_;
  private Game game_;

  public GameView(Context context) {
    super(context);
    screenWidth_ = getResources().getDisplayMetrics().widthPixels;
    screenHeight_ = getResources().getDisplayMetrics().heightPixels;
    game_ = new Game(screenWidth_, screenHeight_);
  }

  public void onDraw(Canvas canvas) {
    game_.update();
    game_.redraw(canvas);

    try {
      Thread.sleep((long) (1000 / Game.FPS));
    } catch (Exception e) {}

    invalidate();
  }

  public boolean onTouchEvent(MotionEvent event) {
    game_.onTouchEvent(event);
    return true;
  }
}
