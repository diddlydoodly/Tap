package io.github.omgimanerd.tap.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

/**
 * Created by omgimanerd on 1/23/15.
 */
public class Game {
  private static final int TOUCH_DISTANCE_THRESHOLD = 10;
  private static final int MIN_SPAWN_INTERVAL = 500;
  private static final int MAX_SPAWN_INTERVAL = 2000;
  private static final int DOUBLE_SPAWN_CHANCE = 15;

  private boolean lost_;
  private int score_;

  private Random rand_;
  private double lastBallSpawnTime_;
  private float spawnInterval_;
  private float screenWidth_;
  private float screenHeight_;

  // The Paint, Color, and RectF arrays are all parallel arrays.
  // Hence, the rectangular stripes are drawn in the order that the colors
  // array is declared.
  private int[] colors_ = new int[] {
      Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW
  };
  private Paint[] paints_;
  private RectF[] rects_;

  private ArrayList<TapBall> balls_;

  public Game(float screenWidth, float screenHeight) {
    lost_ = false;

    rand_ = new Random();
    lastBallSpawnTime_ = 0;
    spawnInterval_ = 0;
    screenWidth_ = screenWidth;
    screenHeight_ = screenHeight;;
    balls_ = new ArrayList<TapBall>();

    paints_ = new Paint[colors_.length];
    for (int i = 0; i < 4; ++i) {
      paints_[i] = new Paint();
      paints_[i].setColor(colors_[i]);
    }

    rects_ = new RectF[colors_.length];
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
        this.lost_ = true;
        balls_.remove(ball);
        ball = null;
      }
    }

    if (currentTimeMillis() - lastBallSpawnTime_ >= spawnInterval_ ||
        balls_.size() == 0) {
      balls_.add(TapBall.generateRandomlyMovingBall(screenWidth_,
                                                    screenHeight_));
      if (rand_.nextInt(100) < DOUBLE_SPAWN_CHANCE) {
        balls_.add(TapBall.generateRandomlyMovingBall(screenWidth_,
                                                      screenHeight_));
      }
      lastBallSpawnTime_ = currentTimeMillis();
      spawnInterval_ = rand_.nextInt(MAX_SPAWN_INTERVAL) + MIN_SPAWN_INTERVAL;
    }
  }

  public void redraw(Canvas canvas) {
    // Render the background.

    for (int i = 0; i < colors_.length; ++i) {
      canvas.drawRect(rects_[i], paints_[i]);
    }

    // Render the balls.
    for (int i = 0; i < balls_.size(); ++i) {
      balls_.get(i).redraw(canvas);
    }
  }

  private boolean touchedBall(float[] touchPoint, TapBall ball) {
    double distance = Math.sqrt(Math.pow(touchPoint[0] - ball.getX(), 2) +
                                    Math.pow(touchPoint[1] - ball.getY(), 2));
    return distance < ball.getRadius() + TOUCH_DISTANCE_THRESHOLD;
  }

  public void onTouchEvent(MotionEvent event) {
    // Register the touch event if it was on top of a ball.
    // We search backwards through the ArrayList in case a ball is on top of
    // another one since the balls are drawn from the ArrayList iterating
    // forward.
    float[] touchPoint = new float[] {
        event.getX(), event.getY()
    };

    for (int i = balls_.size() - 1; i >= 0; --i) {
      TapBall ball = balls_.get(i);
      if (touchedBall(touchPoint, ball)) {
        // Check if the ball is in the correct stripe.
        if (!rects_[ball.getColorIndex()].contains(ball.getX(), ball.getY())) {
          lost_ = true;
        } else {
          score_++;
        }

        balls_.remove(ball);
        ball = null;
        return;
      }
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
