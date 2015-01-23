package io.github.omgimanerd.tap.game;

/**
 * Created by omgimanerd on 12/1/14.
 */
public class Ball {
  private double amplitude_;
  private double wavelength_;
  private double x_;
  private double y_;

  public Ball(double x, double y, double amplitude, double wavelength) {
    this.x_ = x;
    this.y_ = y;
    this.amplitude_ = amplitude;
    this.wavelength_ = wavelength;
  }

  public void update() {
    this.x_++;
    this.y_ = this.amplitude_ * Math.sin(this.x_ / this.wavelength_);
  }

  public double getAmplitude() {
    return amplitude_;
  }

  public void setAmplitude(double amplitude_) {
    this.amplitude_ = amplitude_;
  }

  public double getWavelength() {
    return wavelength_;
  }

  public void setWavelength(double wavelength_) {
    this.wavelength_ = wavelength_;
  }

  public double getX() {
    return x_;
  }

  public void setX(double x_) {
    this.x_ = x_;
  }

  public double getY() {
    return y_;
  }

  public void setY(double y_) {
    this.y_ = y_;
  }
}
