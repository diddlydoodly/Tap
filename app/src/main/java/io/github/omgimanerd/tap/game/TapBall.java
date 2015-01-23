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

  private static final int[] BALL_COLORS = new int[] {
      Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW
  };

  private float x_;
  private float y_;
  private float yOffset_;
  private float radius_;
  private float amplitude_;
  private float wavelength_;
  private RectF bounds_;
  private Paint paint_;

  public TapBall(float x, float y, float yOffset, float radius, float amplitude,
                 float wavelength, int color) {
    x_ = x;
    y_ = y;
    yOffset_ = yOffset;
    radius_ = radius;
    amplitude_ = amplitude;
    wavelength_ = wavelength;
    bounds_ = new RectF();
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
    float amplitude = rand.nextInt((int) (3 * screenHeight / 8));
    float wavelength = rand.nextInt((int) (screenWidth / 8)) + screenWidth / 8;
    int color = TapBall.BALL_COLORS[rand.nextInt(TapBall.BALL_COLORS.length)];
    return new TapBall(x, y, yOffset, radius, amplitude, wavelength, color);
  }

  public void update() {
    x_ += 5;
    y_ = (float) (amplitude_ * Math.sin(x_ / wavelength_)) + yOffset_;
    bounds_.top = y_ - radius_;
    bounds_.right = x_ + radius_;
    bounds_.bottom = y_ + radius_;
    bounds_.left = x_ - radius_;
  }

  public void redraw(Canvas canvas) {
    canvas.drawOval(bounds_, paint_);
  }

  public boolean isOutOfBounds(float screenWidth) {
    return x_ > screenWidth + radius_;
  }

  public int getColor() {
    return paint_.getColor();
  }
}
