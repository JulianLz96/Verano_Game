package com.iteso.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.iteso.game.beans.Grid;
import com.iteso.game.beans.OnSwipeTouchListener;
import com.iteso.game.beans.Square;

public class GameActivity extends AppCompatActivity {
    public static final String PREF_NAME = "Preferences";
    public static final int SWIPE_TRESHOLD = 100;
    public static final int VELOCITY_TRESHOLD = 100;

    Grid grid;
    SharedPreferences settings;
    Button scoreBtn;
    Button bestBtn;
    Button restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final View[] viewsId = new View[16];
        settings = getApplicationContext().getSharedPreferences(PREF_NAME,0);
        int best = settings.getInt("best",0);
        grid = new Grid(4,this,best);
        scoreBtn = findViewById(R.id.game_activity_score);
        bestBtn = findViewById(R.id.game_activity_best);
        restart = findViewById(R.id.game_activity_restart);
        //obtener todos los views
        viewsId[0] = findViewById(R.id.game_0_0);
        viewsId[1] = findViewById(R.id.game_0_1);
        viewsId[2] = findViewById(R.id.game_0_2);
        viewsId[3] = findViewById(R.id.game_0_3);
        viewsId[4] = findViewById(R.id.game_1_0);
        viewsId[5] = findViewById(R.id.game_1_1);
        viewsId[6] = findViewById(R.id.game_1_2);
        viewsId[7] = findViewById(R.id.game_1_3);
        viewsId[8] = findViewById(R.id.game_2_0);
        viewsId[9] = findViewById(R.id.game_2_1);
        viewsId[10] = findViewById(R.id.game_2_2);
        viewsId[11] = findViewById(R.id.game_2_3);
        viewsId[12] = findViewById(R.id.game_3_0);
        viewsId[13] = findViewById(R.id.game_3_1);
        viewsId[14] = findViewById(R.id.game_3_2);
        viewsId[15] = findViewById(R.id.game_3_3);
        //obtener el resto de botones
        //TODO
        //Crear movimientos del dedo
        for(View v:viewsId){
            v.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this){
                public void onSwipeTop(){
                    grid.swipeOnGrid(Grid.UP);
                    setScoreAndBest();
                }
                public void onSwipeRight(){
                    grid.swipeOnGrid(Grid.RIGHT);
                    setScoreAndBest();
                }
                public void onSwipeLeft(){
                    grid.swipeOnGrid(Grid.LEFT);
                    setScoreAndBest();
                }
                public void onSwipeBottom(){
                    grid.swipeOnGrid(Grid.DOWN);
                    setScoreAndBest();
                }
            });
        }
        grid.setGridSquares(viewsId);
        grid.restartGrid();
        grid.addRandomNumber();

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public boolean onFling(MotionEvent downEvent,
                           MotionEvent moveEvent,
                           float vx, float vy) {
        boolean result = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();

        if(Math.abs(diffX) > Math.abs(diffY)) {
            if(Math.abs(diffX) > SWIPE_TRESHOLD
                    && Math.abs(vx) > VELOCITY_TRESHOLD){
                if(diffX > 0) {
                    grid.swipeOnGrid(Grid.RIGHT);
                } else {
                    grid.swipeOnGrid(Grid.LEFT);
                }
                result = true;
                setScoreAndBest();
            }
        }else {
          if (Math.abs(diffY) > SWIPE_TRESHOLD
                  && Math.abs(vy) > VELOCITY_TRESHOLD){
              if(diffY > 0) {
                  grid.swipeOnGrid(Grid.DOWN);
              }else {
                  grid.swipeOnGrid(Grid.UP);
              }
              result = true;
              setScoreAndBest();
          }
        }
        return result;
    }


    private void setScoreAndBest() {
        int score = grid.getScore();
        int best = grid.getBest();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("best",best);
        editor.apply();
        scoreBtn.setText("SCORE\n"+score);
        bestBtn.setText("BEST\n"+best);
    }

}
