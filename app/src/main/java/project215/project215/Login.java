package project215.project215;

/*
Created by Matt
11/15/2015
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;

public class Login extends AppCompatActivity {

    Button Submit;
    Button Register;

    EditText Email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Submit is pushed if the user is trying to login
        Submit = (Button) findViewById(R.id.button);
        Submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //compare email and hashed password to current database
                //Goto Map View

                if (UserModel.checkUser(Email.toString(), password.toString()) == true) {
                    Intent map = new Intent(getApplicationContext(), MapTest.class);
                    startActivityForResult(map, 0);
                }
                else {
                    // there's been an error
                }
            }
        });

        //Cancel is pushed if the user is trying to register
        Register = (Button) findViewById(R.id.button2);
        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Goto Register View
                Intent reg = new Intent(getApplicationContext(), Register.class);
                startActivityForResult(reg, 0);
            }
        });
    }
}
