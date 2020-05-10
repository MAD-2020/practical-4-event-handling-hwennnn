package sg.edu.np.WhackAMole;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private int count = 0;
    private int last_location = 0;
    private int last_const = 0;
    private static final String TAG = "Whack-A-Mole";
    private static final int[] BUTTON_IDS = {R.id.button_1, R.id.button_2, R.id.button_3};
    Random ran = new Random();
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msg = findViewById(R.id.message);

        setNewMole();
        msg.setText(String.valueOf(count));
        Log.v(TAG, "Finished Pre-Initialisation!");
    }

    @Override
    protected void onStart(){
        super.onStart();
        setNewMole();
        Log.v(TAG, "Starting GUI!");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.v(TAG, "Paused Whack-A-Mole!");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.v(TAG, "Stopped Whack-A-Mole!");
        finish();
    }


    private void nextLevelQuery(){
        Log.v(TAG, "Advance option given to user!");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning! Insane Whack-A-Mole Incoming!");
        builder.setMessage("Would you like to advance to the advance mode?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Log.v(TAG, "User accepts!");
                nextLevel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Log.v(TAG,"User decline!");
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void nextLevel(){
        Intent activityName = new Intent(MainActivity.this, Main2Activity.class);
        Bundle extras = new Bundle();
        extras.putInt("score", count);
        activityName.putExtras(extras);
        startActivity(activityName);
    }

    public void setNewMole()
    {
        int randomLocation = ran.nextInt(3);
        Button this_btn = findViewById(BUTTON_IDS[randomLocation]);
        Button last_btn = findViewById(BUTTON_IDS[last_location]);
        last_btn.setText("O");
        this_btn.setText("*");
        last_location = randomLocation;
    }

    public void onClickBtn(View v)
    {
        switch(v.getId()) {
            case R.id.button_1:
                Log.v(TAG, "Button Left Clicked!");
                break;
            case R.id.button_2:
                Log.v(TAG, "Button Middle Clicked!");
                break;
            case R.id.button_3:
                Log.v(TAG, "Button Right Clicked!");
        }

        Button b = (Button)v;
        String buttonText = b.getText().toString();
        if (buttonText.equals("*")){
            count++;
            Log.v(TAG, "Hit, score added!");
        }else{
            if (count > 0){
                count--;
                Log.v(TAG, "Missed, score deducted!");
            }else{
                Log.v(TAG, "Missed Hit!");
            }
        }

        setNewMole();
        msg.setText(String.valueOf(count));

        if (count % 10 == 0 && count > last_const){
            last_const = count; //to record peak value of the count
            nextLevelQuery();
        }
    }
}