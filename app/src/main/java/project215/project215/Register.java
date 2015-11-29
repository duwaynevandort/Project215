package project215.project215;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private EditText confirmedPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailField = (EditText) findViewById(R.id.register_screen_email_entry_field);
        passwordField = (EditText) findViewById(R.id.register_screen_password_entry_field);
        confirmedPasswordField = (EditText) findViewById(R.id.register_screen_password_reentry_field);
    }

    public void confirmRegistration(View view)
    {
        String emailEntered = emailField.getText().toString();
        String passwordEntered = passwordField.getText().toString();
        String confirmedPasswordEntered = confirmedPasswordField.getText().toString();

        //TODO check if user is already registered

        if (passwordEntered.equals(confirmedPasswordEntered))
        {
            if ( SuperController.createUser(emailEntered, passwordEntered) )
            {
                Toast.makeText(Register.this, "Registration successful! Please check" +
                        "your email to confirm your new account!", Toast.LENGTH_LONG).show();
                Intent login = new Intent(getApplication(), Login.class);
                startActivity(login);
                //this.finish();
            }
            else
            {
                Toast.makeText(Register.this, "Registration failed. If you already have a jUST iN!" +
                        "account, please sign in at the login screen.", Toast.LENGTH_LONG).show();
                emailField.setText("");
                passwordField.setText("");
                confirmedPasswordField.setText("");
            }
        }
        else
        {
            Toast.makeText(Register.this, "Your passwords need to match.", Toast.LENGTH_SHORT).show();
            passwordField.setText("");
            confirmedPasswordField.setText("");
        }
    }

    public void cancelRegistration(View view)
    {
        Intent login = new Intent(getApplication(), Login.class);
        startActivity(login);
        //this.finish();
    }
}
