package com.tushar.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar timerSeekBar;
    TextView timerText;
    Boolean isCounterActive = false;
    Button controllerButton ;
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        timerText = (TextView)findViewById(R.id.timerText);
        controllerButton = (Button)findViewById(R.id.controllerButton);
        Log.i("Info : ", "timerText initialized" + timerText.toString());
        timerSeekBar = (SeekBar) findViewById(R.id.timerSeekBar);
        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(30);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Info : ", "updating timer");
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateTimer(int progress) {
        int min = progress/60;
        int sec = progress%60;
        String secString=Integer.toString(sec);
        if(sec<10){
            secString = "0"+secString;
        }

        String minString=Integer.toString(min);
        if(min<10){
            minString = "0"+minString;
        }
        Log.i("Info : ", "timerText before updating label : " + timerText.toString());
        timerText.setText(minString + " : " + secString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void controlTimer(View view){
        if(!isCounterActive) {

            isCounterActive = true;
            timerSeekBar.setEnabled(false);
            controllerButton.setText("Stop");
            //add delay because when this method is called, the timerSeekBar.getProgress() is slightly lower
            // than the actual value we set as some time elapses between the onClick() and this method call
            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) millisUntilFinished / 1000);

                }

                @Override
                public void onFinish() {
                    timerText.setText("00:00");
                    MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                    mplayer.start();
                    try{
                    Thread.sleep(1000);
                    }catch(Exception e){

                    }
                    resetTimer();
                }
            }.start();
        }else{
            resetTimer();
        }
    }

    private void resetTimer() {
        isCounterActive = false;
        timerSeekBar.setEnabled(true);
        controllerButton.setText("GO");
        timerText.setText("00:30");
        timerSeekBar.setProgress(30);
        countDownTimer.cancel();
    }
}
