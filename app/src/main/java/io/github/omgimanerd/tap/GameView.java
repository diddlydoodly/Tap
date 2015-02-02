package io.github.omgimanerd.tap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
//import com.google.android.gms.ads.*;

import io.github.omgimanerd.tap.game.Game;
import io.github.omgimanerd.tap.game.Sound;

import static java.lang.System.currentTimeMillis;

/**
 * Created by omgimanerd on 1/23/15.
 */

public class GameView extends View {

  private static final float FPS = 60;
  private static final double SCREEN_CLIP_RATIO = 19.0/20.0;
  private static final int OVERLAY_BORDER_RADIUS = 10;
  private static final String OVERLAY_COLOR = "#DD888888";
  private static final String OVERLAY_TEXT_COLOR = "#FFCCCCCC";
  private static final String OVERLAY_TEXT_SHADOW_COLOR = "#AA000000";

  private static final int STATE_MENU = 0;
  private static final int STATE_GAME = 1;
  private static final int STATE_LOST = 2;
  private int STATE = 0;

  private SharedPreferences tapData_;
  private float screenHeight_;
  private float screenWidth_;
  private Game game_;
  private double lastUpdateTime_;

  private RectF overlay_;
  private Paint overlayPaint_;
  private Paint textPaintLarge_;
  private Paint textPaintSmall_;

  //private InterstitialAd interstitialAd_;

  public GameView(Context context) {
    super(context);
    tapData_ = context.getSharedPreferences("tapData", Context.MODE_PRIVATE);
    screenWidth_ = getResources().getDisplayMetrics().widthPixels;
    screenHeight_ = (float) (getResources().getDisplayMetrics().heightPixels *
        SCREEN_CLIP_RATIO);
    game_ = new Game(screenWidth_, screenHeight_);
    lastUpdateTime_ = currentTimeMillis();

    float padding = ((screenWidth_ / 10) + (screenHeight_ / 10)) / 2;
    overlay_ = new RectF(padding, padding, screenWidth_ - padding,
                         screenHeight_ - padding);
    overlayPaint_ = new Paint();
    overlayPaint_.setColor(Color.parseColor(OVERLAY_COLOR));
    textPaintLarge_ = new Paint();
    textPaintLarge_.setColor(Color.parseColor(OVERLAY_TEXT_COLOR));
    textPaintLarge_.setShadowLayer(5, 5, 5,
                                   Color.parseColor(OVERLAY_TEXT_SHADOW_COLOR));
    textPaintLarge_.setTextSize(
        getResources().getDimensionPixelSize(R.dimen.text_size_large));
    textPaintLarge_.setTextAlign(Paint.Align.CENTER);
    textPaintSmall_ = new Paint();
    textPaintSmall_.setColor(Color.parseColor(OVERLAY_TEXT_COLOR));
    textPaintSmall_.setShadowLayer(5, 5, 5,
                                   Color.parseColor(OVERLAY_TEXT_SHADOW_COLOR));
    textPaintSmall_.setTextSize(
        getResources().getDimensionPixelSize(R.dimen.text_size_small));
    textPaintSmall_.setTextAlign(Paint.Align.CENTER);

    /*
    interstitialAd_ = new InterstitialAd(context);
    interstitialAd_.setAdUnitId(getResources().getString(R.string.ad_unit_id));
    AdRequest adRequest = new AdRequest.Builder().addTestDevice
        (AdRequest.DEVICE_ID_EMULATOR).build();
    interstitialAd_.loadAd(adRequest);*/
  }

  public void onDraw(Canvas canvas) {

    game_.redrawBackground(canvas);

    switch (STATE) {
      case STATE_MENU:
        canvas.drawRoundRect(overlay_, OVERLAY_BORDER_RADIUS,
                             OVERLAY_BORDER_RADIUS, overlayPaint_);

        String startText = getResources().getString(R.string.start_button);
        canvas.drawText(startText, screenWidth_ / 2, screenHeight_ / 2,
                        textPaintLarge_);


        String highscoreString = "Highscore: " +
            tapData_.getInt("tapHighScore", 0);
        canvas.drawText(highscoreString, screenWidth_ / 2,
                        3 * screenHeight_ / 4 - 2 * textPaintSmall_
                .getTextSize(),
                        textPaintSmall_);
        String instructions = getResources().getString(R.string.instructions);
        canvas.drawText(instructions, 0, instructions.length() / 2,
                        screenWidth_ / 2,
                        3 * screenHeight_ / 4,
                        textPaintSmall_);
        canvas.drawText(instructions, instructions.length() / 2,
                        instructions.length(),
                        screenWidth_ / 2, 3 * screenHeight_ / 4 +
                textPaintSmall_.getTextSize(),
                        textPaintSmall_);
        break;

      case STATE_GAME:
        if (currentTimeMillis() - lastUpdateTime_ >= 1000 / FPS) {
          game_.update();
          game_.redrawBalls(canvas);

          String scoreText = "" + game_.getScore();
          canvas.drawText(scoreText, textPaintLarge_.getTextSize(),
                          textPaintLarge_.getTextSize(),
                          textPaintLarge_);

          if (game_.lost()) {
            STATE = STATE_LOST;
          }
        }
        break;

      case STATE_LOST:
        canvas.drawRoundRect(overlay_, OVERLAY_BORDER_RADIUS,
                             OVERLAY_BORDER_RADIUS, overlayPaint_);
        String lostText = getResources().getString(R.string.lost_screen);
        canvas.drawText(lostText, screenWidth_ / 2, screenHeight_ / 2,
                        textPaintLarge_);

        int score = game_.getScore();
        int highscore = tapData_.getInt("tapHighScore", 0);
        if (score > highscore) {
          SharedPreferences.Editor editor = tapData_.edit();
          editor.putInt("tapHighScore", score);
          editor.commit();
        }

        String scoreString = "Score: " + score;
        highscoreString = "High score: " + highscore;

        canvas.drawText(highscoreString, screenWidth_ / 2,
                        3 * screenHeight_ / 4 - textPaintSmall_.getTextSize(),
                        textPaintSmall_);
        canvas.drawText(scoreString, screenWidth_ / 2,
                        3 * screenHeight_ / 4, textPaintSmall_);

        //displayAd();
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
          Sound.play("blop");
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
  /*
  public void displayAd() {
    if (interstitialAd_.isLoaded()) {
      interstitialAd_.show();
    }
  }*/
}
