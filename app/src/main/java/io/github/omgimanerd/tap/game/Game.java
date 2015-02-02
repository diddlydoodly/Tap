package io.github.omgimanerd.tap.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

/**
 * Created by omgimanerd on 1/23/15.
 */
public class Game {
  private static final int INITIAL_DELAY = 2000;
  private static final int MIN_SPAWN_INTERVAL = 250;
  private static final int MAX_SPAWN_INTERVAL = 1750;
  protected static final int[] COLORS = new int[] {
      Color.parseColor("#D32F2F"),
      Color.parseColor("#303F9F"),
      Color.parseColor("#4CAF50"),
      Color.parseColor("#FFF689")
  };
  protected static final int STROKE_COLOR = Color.parseColor("#727272");
  protected static final float STROKE_WIDTH = 5;

  private boolean lost_;
  private int score_;

  private Random rand_;
  private double lastBallSpawnTime_;
  private float spawnInterval_;
  private float screenWidth_;
  private float screenHeight_;

  // The Paint, Color, and RectF arrays are all parallel arrays.
  // Hence, the rectangular stripes are drawn in the order that the colors
  // array is declared. The Balls will be drawn with this colors array.
  private Paint[] paints_;
  private RectF[] rects_;

  private ArrayList<TapBall> balls_;

  public Game(float screenWidth, float screenHeight) {
    lost_ = false;

    rand_ = new Random();
    lastBallSpawnTime_ = 0;
    spawnInterval_ = INITIAL_DELAY;
    screenWidth_ = screenWidth;
    screenHeight_ = screenHeight;;
    balls_ = new ArrayList<TapBall>();

    paints_ = new Paint[COLORS.length];
    for (int i = 0; i < 4; ++i) {
      paints_[i] = new Paint();
      paints_[i].setColor(COLORS[i]);
    }

    rects_ = new RectF[COLORS.length];
    rects_[0] = new RectF(0, 0, screenWidth, screenHeight / 4);
    rects_[1] = new RectF(0, screenHeight / 4, screenWidth, screenHeight / 2);
    rects_[2] = new RectF(0, screenHeight / 2, screenWidth,
                          3 * screenHeight / 4);
    rects_[3] = new RectF(0, 3 * screenHeight / 4, screenWidth, screenHeight);
  }

  public void update() {
    // Game loop, update the balls and spawn them if needed.
    for (int i = 0; i < balls_.size(); ++i) {
      TapBall ball = balls_.get(i);
      ball.update();
      if (ball.isOutOfBounds(screenWidth_)) {
        Sound.play("lost");
        this.lost_ = true;
        balls_.remove(ball);
        ball = null;
      }
    }

    if (currentTimeMillis() - lastBallSpawnTime_ >= spawnInterval_ ||
        balls_.size() == 0) {
      balls_.add(TapBall.generateRandomlyMovingBall(screenWidth_,
                                                    screenHeight_));
      lastBallSpawnTime_ = currentTimeMillis();
      spawnInterval_ = rand_.nextInt(MAX_SPAWN_INTERVAL) + MIN_SPAWN_INTERVAL;
    }
  }

  public void redrawBackground(Canvas canvas) {
    // Render the background.
    for (int i = 0; i < COLORS.length; ++ i) {
      canvas.drawRect(rects_[i], paints_[i]);
    }
  }

  public void redrawBalls(Canvas canvas) {
    // Render the balls.
    for (int i = 0; i < balls_.size(); ++i) {
      balls_.get(i).redraw(canvas);
    }
  }

  public void onTouchEvent(MotionEvent event) {
    // Exit if the balls_ ArrayList is empty.
    if (balls_.size() == 0) {
      return;
    }

    // Register the touch event if it was on top of a ball.
    float[] touchPoint = new float[] {
        event.getX(), event.getY()
    };

    // Find the ball closest to the touch point.
    TapBall closestBall = balls_.get(0);
    for (int i = 1; i < balls_.size(); ++i) {
      TapBall ball = balls_.get(i);
      if (ball.distanceFrom(touchPoint) < closestBall.distanceFrom(touchPoint)) {
        closestBall = ball;
      }
    }

    // Only register if it was close enough to count as a touch point.
    if (closestBall.touched(touchPoint)) {
      // Check if the ball is in the correct stripe.
      if (rects_[closestBall.getColorIndex()].contains(closestBall.getX(),
                                                       closestBall.getY())) {
        Sound.play("blop");
        score_++;
      } else {
        Sound.play("lost");
        lost_ = true;
      }

      balls_.remove(closestBall);
      return;
    }
  }

  public void resetGame() {
    lost_ = false;
    score_ = 0;
    balls_.clear();
  }

  public boolean lost() {
    return lost_;
  }

  public int getScore() {
    return score_;
  }
}
