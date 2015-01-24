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

  // The ball colors array is also parallel with respect to stripe color array.
  private static final int[] BALL_COLORS = new int[] {
      Color.parseColor("#dd0000"), Color.parseColor("#0000dd"),
      Color.parseColor("#00dd00"), Color.parseColor("#dddd00")
  };

  private float x_;
  private float y_;
  private float yOffset_;
  private float radius_;
  private float amplitude_;
  private float wavelength_;
  private Paint paint_;

  public TapBall(float x, float y, float yOffset, float radius, float amplitude,
                 float wavelength, int color) {
    x_ = x;
    y_ = y;
    yOffset_ = yOffset;
    radius_ = radius;
    amplitude_ = amplitude;
    wavelength_ = wavelength;
    paint_ = new Paint();
    paint_.setColor(color);
  }

  public static TapBall generateRandomlyMovingBall(float screenWidth,
                                                   float screenHeight) {
    Random rand = new Random();
    float x = 0;
    float y = screenHeight / 2;
    float yOffset = screenHeight / 2;
    float radius = screenHeight / 8;
    float amplitude = 3 * screenHeight / 8;
    if (rand.nextInt(2) == 0) {
      amplitude *= -1;
    }
    float wavelength = rand.nextInt((int) (screenWidth / 15)) + screenWidth /
        15;
    int color = BALL_COLORS[rand.nextInt(BALL_COLORS.length)];
    return new TapBall(x, y, yOffset, radius, amplitude, wavelength, color);
  }

  public void update() {
    x_ += 10;
    y_ = (float) (amplitude_ * Math.sin(x_ / wavelength_)) + yOffset_;
  }

  public void redraw(Canvas canvas) {
    canvas.drawCircle(x_, y_, radius_, paint_);
  }

  public boolean isOutOfBounds(float screenWidth) {
    return x_ > screenWidth + radius_;
  }

  public float getX() {
    return x_;
  }

  public float getY() {
    return y_;
  }

  public float[] getCenter() {
    return new float[] {x_, y_};
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
