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

    //get the email and password still

    android.widget.Button button1;
    android.widget.Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //Goto Map View
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //Goto Register View
            }
        });
    }
}
