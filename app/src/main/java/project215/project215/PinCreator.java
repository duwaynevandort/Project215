package project215.project215;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Kenny on 11/15/2015.
 */

public class PinCreator extends Activity
{
    private Spinner categorySpinner;
    private EditText descriptionField;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_create_screen);

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

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String categorySelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }
}
