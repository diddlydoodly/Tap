package io.github.omgimanerd.tap;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class GameActivity extends ActionBarActivity {

  public static final String TAG = GameActivity.class.getSimpleName();

  private Button startButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);

    startButton = (Button) findViewById(R.id.startButton);
    startButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startGame();
      }
    });
  }

  private void startGame() {
    Intent intent = new Intent(this, GameActivity.class);
    startActivity(intent);
  }
}
