package com.ota.countdowntimer;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CountdownActivity extends AppCompatActivity {
    private TextView mTextField;
    private Button setTimeButton;
    private EditText NumberField;
    private Button pauseButton;
    private String ALARMS = "alarms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        createNotificationChannel();

        mTextField = findViewById(R.id.mainTextField);
        setTimeButton = findViewById(R.id.setTimerBtn);
        NumberField = findViewById(R.id.Number);
        pauseButton = findViewById(R.id.pauseButton);


        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(NumberField.getText())){
                    int setTime = Integer.parseInt(NumberField.getText().toString());
                    if(setTime > 0){
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

//                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                                InputMethodManager.HIDE_NOT_ALWAYS);
                        timerFunction(setTime * 1000);
                        setTimeButton.setVisibility(View.INVISIBLE);
                        NumberField.setVisibility(View.INVISIBLE);
                        ProgressBar progressBar = findViewById(R.id.progressBar);
                        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 500);
                        animation.setDuration(setTime * 1000); // in milliseconds
                        animation.setInterpolator(new LinearInterpolator() {});
                        animation.start();


                    }else{
                        Toast.makeText(CountdownActivity.this, "You need to pass a duration higher than 0", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CountdownActivity.this, "You need to pass a duration", Toast.LENGTH_SHORT).show();
                }
            }
        });

    pauseButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });

    }



    void timerFunction(int x){

        new CountDownTimer(x, 1) {

            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished < 9000){
                    mTextField.setText("0" + (millisUntilFinished / 1000 + 1));
                }else{
                    mTextField.setText("" + (millisUntilFinished / 1000 + 1));
                }

                long pauser = millisUntilFinished / 1000;

            }




            public void onFinish() {
                mTextField.setText("00");
                NumberField.setVisibility(View.VISIBLE);
                setTimeButton.setVisibility(View.VISIBLE);
                AlertDialog.Builder dialog = new AlertDialog.Builder(CountdownActivity.this);
                dialog.setMessage("Your timer ended!").setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), ALARMS)
                        .setSmallIcon(R.drawable.circular)
                        .setContentTitle("Contdown Timer")
                        .setContentText("Your timer finished!");
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(1, builder.build());
            }
        }.start();


    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.alarm);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ALARMS, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
