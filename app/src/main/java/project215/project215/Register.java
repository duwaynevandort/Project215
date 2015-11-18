package project215.project215;

/*
Created by Matt
11/15/2015
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Register extends AppCompatActivity {

    android.widget.Button Submit;
    android.widget.Button Cancel;

    android.widget.EditText Email;
    android.widget.EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Submit is pushed if the user has entered his/her credentials
        Submit = (Button) findViewById(R.id.button3);
        Submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //register and go back to the login screen to login
                //This part is not finished!!
                //Needs to send email and password to plaice
                UserModel.createUser(Email.toString(), Password.toString());
                Intent map = new Intent(getApplicationContext(), MapTest.class);
                startActivityForResult(map, 0);
            }
        });

        //Cacel is pushed if the user wants to cancel registration
        Cancel = (Button) findViewById(R.id.button4);
        Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel registration goto login view
                //pushing the cancel button sends the user back to the login screen
                Intent login = new Intent(getApplicationContext(), Login.class);
                startActivityForResult(login, 0);
            }
        });

    }
}
