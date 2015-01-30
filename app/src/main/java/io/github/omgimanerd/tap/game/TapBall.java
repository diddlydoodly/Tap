package io.github.omgimanerd.tap.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by omgimanerd on 12/1/14.
 */
public class TapBall {

  private static final int TOUCH_DISTANCE_THRESHOLD = 20;
  private static final int SIN_WAVE = 0;
  private static final int COS_WAVE = 1;
  private static final int NUM_WAVES = 2;

  // The ball colors array is also parallel with respect to stripe color array.
  private static final int[] BALL_COLORS = new int[] {
      Color.parseColor("#dd0000"), // Red
      Color.parseColor("#0000dd"), // Blue
      Color.parseColor("#00dd00"), // Green
      Color.parseColor("#dddd00")  // Yellow
  };

  private float x_;
  private float y_;
  private float yOffset_;
  private float radius_;
  private float amplitude_;
  private float wavelength_;
  private int wavetype_;
  private float updateSpeed_;
  private Paint paint_;

  public TapBall(float x, float y, float yOffset, float radius, float amplitude,
                 float wavelength, int wavetype, float updateSpeed, int color) {
    x_ = x;
    y_ = y;
    yOffset_ = yOffset;
    radius_ = radius;
    amplitude_ = amplitude;
    wavelength_ = wavelength;
    wavetype_ = wavetype;
    updateSpeed_ = updateSpeed;
    paint_ = new Paint();
    paint_.setColor(color);
  }

  public static TapBall generateRandomlyMovingBall(float screenWidth,
                                                   float screenHeight) {
    Random rand = new Random();
    float x = 0;
    float y = 0;
    float yOffset = screenHeight / 2;
    float radius = screenHeight / 8;
    float amplitude = 3 * screenHeight / 8;
    if (rand.nextInt(2) == 0) {
      amplitude *= -1;
    }
    float wavelength = rand.nextInt((int) (screenWidth / 15)) + screenWidth /
        15;
    int wavetype = rand.nextInt(NUM_WAVES);
    float updateSpeed = screenWidth / 150;
    int color = BALL_COLORS[rand.nextInt(BALL_COLORS.length)];

    TapBall tapBall = new TapBall(x, y, yOffset, radius, amplitude,
                                  wavelength, wavetype, updateSpeed, color);
    tapBall.update();
    return tapBall;
  }

  public void update() {
    x_ += updateSpeed_;
    switch (wavetype_) {
      case SIN_WAVE:
        y_ = (float) (amplitude_ * Math.sin(x_ / wavelength_)) + yOffset_;
        break;

      case COS_WAVE:
        y_ = (float) (amplitude_ * Math.cos(x_ / wavelength_)) + yOffset_;
        break;
    }
  }

  public void redraw(Canvas canvas) {
    canvas.drawCircle(x_, y_, radius_, paint_);
  }

  public boolean isOutOfBounds(float screenWidth) {
    return x_ > screenWidth + radius_;
  }

  public boolean touched(float[] touchPoint) {
    double distance = Math.sqrt(Math.pow(touchPoint[0] - getX(), 2) +
                                    Math.pow(touchPoint[1] - getY(), 2));
    return distance < getRadius() + TOUCH_DISTANCE_THRESHOLD;
  }

  public float getX() {
    return x_;
  }

  public float getY() {
    return y_;
  }

  public float getRadius() {
    return radius_;
  }

  public int getColorIndex() {
    for (int i = 0; i < BALL_COLORS.length; ++i) {
      if (paint_.getColor() == BALL_COLORS[i]) {
        return i;
      }
    }
    throw new Error("WTF, something really bad happened.");
  }
}
