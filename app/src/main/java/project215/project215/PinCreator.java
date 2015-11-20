package project215.project215;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * Author: Kenny
 * Manipulates the pin creation layout
 *      and takes in the category and description
 */

public class PinCreator extends Activity
{
    SuperController pinController;

    private Spinner categorySpinner;
    private EditText descriptionField;
    //private Button submitButton;
    //private Button cancelButton;

    private String categorySelected;
    private String descriptionText;
    //private double userLatitude
    //private double userLongitude

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_create_screen);

        descriptionField = (EditText) findViewById(R.id.pin_description_text);
        //submitButton = (Button) findViewById(R.id.submit_pin_create_button);
        //cancelButton = (Button) findViewById(R.id.cancel_pin_create_button);

        fillCategorySpinner();
        addListenerToCategorySpinner();
    }

    //Populate drop down box with categories
    public void fillCategorySpinner()
    {
        categorySpinner = (Spinner)
                findViewById(R.id.category_spinner);

        ArrayAdapter<CharSequence> categorySpinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.categories,
                        android.R.layout.simple_spinner_item);

        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        categorySpinner.setAdapter(categorySpinnerAdapter);
    }

    //Listens for a category selection from the user
    public void addListenerToCategorySpinner()
    {
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void submitPinCreation(View view)
    {
        descriptionText = descriptionField.getText().toString();
        pinController.createPin(0, 0, categorySelected, descriptionText);
        Toast.makeText(PinCreator.this, "Pin Submitted!", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    public void cancelPinCreation(View view)
    {
        Toast.makeText(PinCreator.this, "Pin Creation Cancelled!", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
