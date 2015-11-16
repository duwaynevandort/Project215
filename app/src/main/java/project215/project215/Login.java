package project215.project215;

/*
Created by Matt
11/15/2015

Still needs implementation.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    Button button1;
    Button button2;

    EditText Email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Goto Map View
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Goto Register View
                Intent reg = new Intent(getApplicationContext(), Register.class);
                startActivityForResult(reg, 0);
            }
        });
    }

}
