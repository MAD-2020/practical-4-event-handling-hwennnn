package sg.edu.np.WhackAMole;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


import static java.lang.String.*;

public class Main2Activity extends AppCompatActivity {

    private int advancedScore = 0;
    private int last_location = 0;
    private final static String TAG = "Whack-A-Mole 2.0";
    TextView result;
    Random ran = new Random();
    CountDownTimer myCountDown;
    private static final int[] BUTTON_IDS = {R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4,
            R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9};

    private final Handler mhandler = new Handler();
    private final Runnable mrunnable = new Runnable() { //it is to make it run on thread as Only the original thread that created a view hierarchy can touch its views.
        @Override
        public void run() {
            Log.v(TAG, "New Mole Location!");
            setNewMole();
            mhandler.postDelayed(this, 1000);
        }
    };


    private void readyTimer(){

        myCountDown = new CountDownTimer(10000, 1000){
            public void onTick(long millisUntilFinished){
                final Toast toast = Toast.makeText(getApplicationContext(), format("Get Ready In %d seconds",millisUntilFinished/1000), Toast.LENGTH_SHORT);
                toast.show();
                Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() { //this is to make sure every toast only run for 1 second
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);
            }

            public void onFinish(){
                Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Ready CountDown Complete!");
                myCountDown.cancel();
                placeMoleTimer();
                populateBtns();
            }
        };
        myCountDown.start();
    }

    private void placeMoleTimer(){
        mhandler.postDelayed(mrunnable, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle b = getIntent().getExtras();
        advancedScore= b.getInt("score");
        result = findViewById(R.id.message);

        readyTimer();
        result.setText(String.valueOf(advancedScore));
        Log.v(TAG, "Current User Score: " + advancedScore);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.v(TAG,"Pause");
        mhandler.removeCallbacks(mrunnable);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v(TAG,"Stop!");
        mhandler.removeCallbacks(mrunnable);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG,"Destroy");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.v(TAG,"Resume!");
        placeMoleTimer();
        super.onResume();
    }

    private void populateBtns(){
        for(final int id : BUTTON_IDS){
            Button btn = findViewById(id);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int _id = view.getId();
                    Button click_btn = (Button) findViewById(_id);
                    doCheck(click_btn);
                    //methods below is to avoid calling of setNewMole() concurrently
                    mhandler.removeCallbacks(mrunnable); // this is to interrupt the runnable
                    setNewMole(); // this will be triggered after user has clicked the button
                    placeMoleTimer(); // the runnable will run again

                }
            });
        }
    }

    private void doCheck(Button checkButton)
    {
        if (checkButton.getText().toString().equals("*")){
            advancedScore++;
            Log.v(TAG, "Hit, score added!");
        }else{
            if (advancedScore > 0){
                advancedScore--;
                Log.v(TAG, "Missed, point deducted!");
            }else{
                Log.v(TAG, "Missed Hit!");
            }
        }
        result.setText(valueOf(advancedScore));

    }

    public void setNewMole()
    {
        int randomLocation = ran.nextInt(9);
        Button this_btn = findViewById(BUTTON_IDS[randomLocation]);
        Button last_btn = findViewById(BUTTON_IDS[last_location]);
        last_btn.setText("O");
        this_btn.setText("*");
        last_location = randomLocation;
    }
}

