package io.github.omgimanerd.tap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.ads.*;

import io.github.omgimanerd.tap.game.Game;

/**
 * Created by omgimanerd on 1/23/15.
 */
public class GameView extends View {

  private static final float FPS = 60;
  private static final int OVERLAY_BORDER_RADIUS = 10;
  private static final int SCREEN_PADDING = 40;
  private static final String OVERLAY_COLOR = "#99808080";
  private static final String OVERLAY_TEXT_COLOR = "#FFCCCCCC";
  private static final float OVERLAY_TEXT_SIZE = 50;
  private static final float OVERLAY_TEXT_SIZE_SMALL = 25;

  private static final int STATE_MENU = 0;
  private static final int STATE_GAME = 1;
  private static final int STATE_LOST = 2;
  private int STATE = 0;

  private SharedPreferences tapData_;
  private float screenHeight_;
  private float screenWidth_;
  private Game game_;

  private RectF overlay_;
  private Paint overlayPaint_;
  private Paint textPaint_;
  private Paint textPaintSmall_;

  private InterstitialAd ad_;

  public GameView(Context context) {
    super(context);
    tapData_ = context.getSharedPreferences("tapData", Context.MODE_PRIVATE);
    screenWidth_ = getResources().getDisplayMetrics().widthPixels;
    screenHeight_ = getResources().getDisplayMetrics().heightPixels -
        SCREEN_PADDING;
    game_ = new Game(screenWidth_, screenHeight_);

    float padding = ((screenWidth_ / 10) + (screenHeight_ / 10)) / 2;
    overlay_ = new RectF(padding, padding, screenWidth_ - padding,
                         screenHeight_ - padding);
    overlayPaint_ = new Paint();
    overlayPaint_.setColor(Color.parseColor(OVERLAY_COLOR));
    textPaint_ = new Paint();
    textPaint_.setColor(Color.parseColor(OVERLAY_TEXT_COLOR));
    textPaint_.setTextSize(OVERLAY_TEXT_SIZE);
    textPaint_.setTextAlign(Paint.Align.CENTER);
    textPaintSmall_ = new Paint();
    textPaintSmall_.setColor(Color.parseColor(OVERLAY_TEXT_COLOR));
    textPaintSmall_.setTextSize(OVERLAY_TEXT_SIZE_SMALL);
    textPaintSmall_.setTextAlign(Paint.Align.CENTER);

    ad_ = new InterstitialAd(context);
    ad_.setAdUnitId(getResources().getString(R.string.ad_unit_id));

    AdRequest adRequest = new AdRequest.Builder().addTestDevice
        (AdRequest.DEVICE_ID_EMULATOR).build();

    ad_.loadAd(adRequest);
  }

  public void onDraw(Canvas canvas) {
    switch (STATE) {
      case STATE_MENU:
        canvas.drawRoundRect(overlay_, OVERLAY_BORDER_RADIUS,
                             OVERLAY_BORDER_RADIUS, overlayPaint_);

        String startText = getResources().getString(R.string.start_button);
        canvas.drawText(startText, 0, startText.length(),
                        screenWidth_ / 2,
                        screenHeight_ / 2, textPaint_);


        String instructions = getResources().getString(R.string.instructions);
        canvas.drawText(instructions, 0, instructions.length() / 2,
                        screenWidth_ / 2,
                        3 * screenHeight_ / 4 - textPaint_.getTextSize(),
                        textPaintSmall_);
        canvas.drawText(instructions, instructions.length()/ 2,
                        instructions.length() - 1,
                        screenWidth_ / 2, 3 * screenHeight_ / 4,
                        textPaintSmall_);

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
        break;
      case STATE_LOST:
        canvas.drawRoundRect(overlay_, OVERLAY_BORDER_RADIUS,
                             OVERLAY_BORDER_RADIUS, overlayPaint_);
        String lostText = getResources().getString(R.string.lost_screen);
        canvas.drawText(lostText, 0, lostText.length(), screenWidth_ / 2,
                        screenHeight_ / 2, textPaint_);

        int score = game_.getScore();
        int highscore = tapData_.getInt("tapHighScore", 0);
        if (score > highscore) {
          SharedPreferences.Editor editor = tapData_.edit();
          editor.putInt("tapHighScore", score);
          editor.commit();
        }

        String scoreString = "Score: " + score;
        String highscoreString = "High score: " + highscore;

        canvas.drawText(highscoreString, 0, highscoreString.length(),
                        screenWidth_ / 2,
                        3 * screenHeight_ / 4 - textPaintSmall_.getTextSize(),
                        textPaintSmall_);
        canvas.drawText(scoreString, 0, scoreString.length(), screenWidth_ / 2,
                        3 * screenHeight_ / 4, textPaintSmall_);

        displayAd();

        break;
    }

    invalidate();
  }

  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getAction();

    if (action == MotionEvent.ACTION_DOWN) {
      switch (STATE) {
        case STATE_GAME:
          game_.onTouchEvent(event);
          break;
        default:
          game_.resetGame();

          try {
            Thread.sleep((long) 1000);
          } catch (Exception e) {}

          STATE = STATE_GAME;
          break;
      }
    }
    return true;
  }

  public void displayAd() {
    if (ad_.isLoaded()) {
      ad_.show();
    }
  }
}
