package io.github.omgimanerd.tap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import io.github.omgimanerd.tap.game.Game;

/**
 * Created by omgimanerd on 1/23/15.
 */
public class GameView extends View {

  private static final float FPS = 60;
  private static final int OVERLAY_BORDER_RADIUS = 10;
  private static final String OVERLAY_COLOR = "#99808080";
  private static final String OVERLAY_TEXT_COLOR = "#FFCCCCCC";
  private static final float OVERLAY_TEXT_SIZE = 30;

  private static final int STATE_MENU = 0;
  private static final int STATE_GAME = 1;
  private static final int STATE_LOST = 2;
  private int STATE = 0;

  private float screenHeight_;
  private float screenWidth_;
  private Game game_;

  private RectF overlay_;
  private Paint overlayPaint_;
  private Paint textPaint_;

  public GameView(Context context) {
    super(context);
    screenWidth_ = getResources().getDisplayMetrics().widthPixels;
    screenHeight_ = getResources().getDisplayMetrics().heightPixels;
    game_ = new Game(screenWidth_, screenHeight_);

    float padding = ((screenWidth_ / 10) + (screenHeight_ / 10)) / 2;
    overlay_ = new RectF(padding, padding, screenWidth_ - padding,
                         screenHeight_ - padding);
    overlayPaint_ = new Paint();
    overlayPaint_.setColor(Color.parseColor(OVERLAY_COLOR));
    textPaint_ = new Paint();
    textPaint_.setColor(Color.parseColor(OVERLAY_TEXT_COLOR));
    textPaint_.setTextSize(OVERLAY_TEXT_SIZE);
  }

  public void onDraw(Canvas canvas) {
    switch (STATE) {
      case STATE_MENU:
        canvas.drawRoundRect(overlay_, OVERLAY_BORDER_RADIUS,
                             OVERLAY_BORDER_RADIUS, overlayPaint_);
        canvas.drawText("Start", 0, 5, screenWidth_ / 2,
                        screenHeight_ / 2, textPaint_);
        break;
      case STATE_GAME:
        game_.update();
        game_.redraw(canvas);

        try {
          Thread.sleep((long) (1000 / FPS));
        } catch (Exception e) {}

        if (game_.lost()) {
          STATE = STATE_LOST;
        }

        invalidate();
        break;
      case STATE_LOST:
        canvas.drawRoundRect(overlay_, OVERLAY_BORDER_RADIUS,
                             OVERLAY_BORDER_RADIUS, overlayPaint_);
        break;
    }
  }

  public boolean onTouchEvent(MotionEvent event) {
    switch (STATE) {
      case STATE_GAME:
        game_.onTouchEvent(event);
        break;
      default:
        Log.d("touch", "touched");
        game_.resetGame();
        STATE = STATE_GAME;
        break;
    }
    return true;
  }
}
