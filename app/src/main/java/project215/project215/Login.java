package project215.project215;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = (EditText) findViewById(R.id.login_screen_email_entry);
        passwordField = (EditText)findViewById(R.id.login_screen_password_entry);
    }

    public void submitLogin(View view)
    {
        String emailEntered = emailField.getText().toString();
        String passwordEntered = passwordField.getText().toString();

        if( SuperController.checkUser(emailEntered, passwordEntered) )
        {
            Intent loginSuccess = new Intent(this, Test.class);
            startActivity(loginSuccess);
            this.finish();
        }
        else
        {
            Toast.makeText(Login.this, "Login failed. If you just registered, please check your" +
                    "email for an activation link.", Toast.LENGTH_LONG).show();
            passwordField.setText("");
        }
    }

    public void switchToRegisterScreen(View view)
    {
        Intent register = new Intent(this, Register.class);
        startActivity(register);
        this.finish();
    }
}
