package io.github.omgimanerd.tap.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

/**
 * Created by omgimanerd on 1/23/15.
 */
public class Game {

  public static final float FPS = 60;

  private double lastBallSpawnTime_;
  private float screenWidth_;
  private float screenHeight_;
  private float ballStartX_;
  private float ballStartY_;
  private float ballRadius_;

  private Paint redPaint_;
  private Paint bluePaint_;
  private Paint greenPaint_;
  private Paint yellowPaint_;

  private RectF redBG_;
  private RectF blueBG_;
  private RectF greenBG_;
  private RectF yellowBG_;

  private ArrayList<TapBall> balls_;

  public Game(float screenWidth, float screenHeight) {
    screenWidth_ = screenWidth;
    screenHeight_ = screenHeight;
    ballStartX_ = 0;
    ballStartY_ = screenHeight_ / 2;
    ballRadius_ = screenHeight_  / 4;
    balls_ = new ArrayList<TapBall>();

    redPaint_ = new Paint();
    bluePaint_ = new Paint();
    greenPaint_ = new Paint();
    yellowPaint_ = new Paint();
    redPaint_.setColor(Color.RED);
    bluePaint_.setColor(Color.BLUE);
    greenPaint_.setColor(Color.GREEN);
    yellowPaint_.setColor(Color.YELLOW);

    redBG_ = new RectF(0, 0, screenWidth, screenHeight / 4);
    blueBG_ = new RectF(0, screenHeight / 4, screenWidth, screenHeight / 2);
    greenBG_ = new RectF(0, screenHeight / 2, screenWidth,
                         3 * screenHeight / 4);
    yellowBG_ = new RectF(0, 3 * screenHeight / 4, screenWidth, screenHeight);
  }

  public void update() {
    for (int i = 0; i < balls_.size(); ++i) {
      TapBall ball = balls_.get(i);
      ball.update();
      if (ball.isOutOfBounds(screenWidth_)) {
        balls_.remove(ball);
        ball = null;
      }
    }
    if (currentTimeMillis() - lastBallSpawnTime_ > 2000 || balls_.size() == 0) {
      balls_.add(TapBall.generateRandomlyMovingBall(screenWidth_, screenHeight_));
      lastBallSpawnTime_ = currentTimeMillis();
    }
  }

  public void redraw(Canvas canvas) {
    // Render the background.

    canvas.drawRect(redBG_, redPaint_);
    canvas.drawRect(blueBG_, bluePaint_);
    canvas.drawRect(greenBG_, greenPaint_);
    canvas.drawRect(yellowBG_, yellowPaint_);

    // Render the balls.
    for (int i = 0; i < balls_.size(); ++i) {
      balls_.get(i).redraw(canvas);
    }
  }

  public void onTouchEvent(MotionEvent event) {
    Log.d("touchevent", "touched screen");
  }
}
