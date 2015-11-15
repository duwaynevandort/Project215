package project215.project215;

/*
Created by Matt
11/15/2015
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Register extends AppCompatActivity {

    android.widget.Button button1;
    android.widget.Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button1 = (Button) findViewById(R.id.button3);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //register and go back to the login screen to login

            }
        });

        button2 = (Button) findViewById(R.id.button4);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel registration
            }
        });

    }
}
