package uk.co.phpsoft.andriytest.Activities;
/**
 * Created by Andriy on 21/05/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import uk.co.phpsoft.andriytest.DatePickerFragment;
import uk.co.phpsoft.andriytest.DialogsSet;
import uk.co.phpsoft.andriytest.R;


public class MainForm extends AppCompatActivity implements View.OnClickListener {

    /**
     * number format editing
     */
    private static EditText mQuantityText;

    /**
     * Not sure if it's filed type money or string
     * Could be added money format on input.
     */
    private static EditText mValueText;

    /**
     * single select value
     */
    private static EditText mPriorityText;

    /**
     * multi select value, comma separated
     */
    private static EditText mAttributeText;

    /**
     * date picker value
     */
    private static EditText mDateText;

    /**
     * email value (email keyboard)
     */
    private static EditText mEmailText;


    /**
     * Edit EditText values buttons
     * Optionally can be edited in the field
     * Only Date use DatePicker and selections (Priority and Attributes)
     */
    private Button mButtonQuantity;

    private Button mButtonValue;

    private Button mButtonPriority;

    private Button mButtonAttribute;

    private Button mButtonEmail;

    private Button mButtonSendEmail;

    /**
     * Attributes list - optionally can be taken from SQLite DB
     */
    private Collection<String> cAttributesCollection =
            Arrays.asList("Power Steering",
                          "Stereo",
                           "Air Conditioning",
                           "Sat Nav");

    /**
     * Priority list - optionally can be taken from SQLite DB
     */
    private Collection<String> cPriorityCollection =
            Arrays.asList("High",
                          "Medium",
                           "Low");

    private String emailSubject = "Request from MobileApp (Andriy Test)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);

        // Keyboard no needed on EditText - different way to enter data.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Edit text field definition
        /**
         * .setEnabled(false) for keeping EditText non editable but only by clicking on Edit button
         * alternatively it can be removed and field edited directly
         */
        mQuantityText  = (EditText) findViewById(R.id.valueQuantity);
        mQuantityText.setKeyListener(null);
        mValueText     = (EditText) findViewById(R.id.valueValue);
        mValueText.setKeyListener(null);
        mPriorityText  = (EditText) findViewById(R.id.valuePriority);
        mPriorityText.setKeyListener(null);
        mAttributeText = (EditText) findViewById(R.id.valueAttributes);
        mAttributeText.setKeyListener(null);
        mDateText      = (EditText) findViewById(R.id.valueDate);
        mDateText.setKeyListener(null);
        mEmailText     = (EditText) findViewById(R.id.valueEmail);
        mEmailText.setKeyListener(null);

        // addint click listener for used buttons
        mButtonQuantity = (Button)findViewById(R.id.buttonQuantity);
        mButtonQuantity.setOnClickListener(this);

        mButtonValue = (Button)findViewById(R.id.buttonValue);
        mButtonValue.setOnClickListener(this);

        mButtonPriority = (Button)findViewById(R.id.buttonPriority);
        mButtonPriority.setOnClickListener(this);

        mButtonAttribute = (Button)findViewById(R.id.buttonAttributes);
        mButtonAttribute.setOnClickListener(this);

        mButtonEmail = (Button)findViewById(R.id.buttonEmail);
        mButtonEmail.setOnClickListener(this);

        mButtonSendEmail = (Button)findViewById(R.id.buttonSendEmail);
        mButtonSendEmail.setOnClickListener(this);
    }

    /**
     * Click listener and action triggers
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonQuantity:
                DialogsSet.showInputDialog(this, mQuantityText, InputType.TYPE_CLASS_NUMBER);
                break;

            case R.id.buttonValue:
                DialogsSet.showInputDialog(this, mValueText, InputType.TYPE_CLASS_TEXT);
                break;

            case R.id.buttonPriority:
                DialogsSet.showRadioButtonDialog(this, cPriorityCollection, mPriorityText);
                break;

            case R.id.buttonAttributes:
                DialogsSet.showCheckboxDialog(this, cAttributesCollection, mAttributeText);
                break;

            case R.id.buttonEmail:
                DialogsSet.showInputDialog(this, mEmailText, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;

            case R.id.buttonSendEmail:
                emailPrepare();
                break;

            default:
                break;
        }
    }

    public void showDatePicker(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    /**
     * void: prepare json from all fields and string it
     * for email body accepted format.
     * Possible enhancement:
     * 1. Add number, string, money, email, date validation
     * 2. Check for empty strings for required field
     * 3. highlight troubled field on the views
     */
    public void emailPrepare() {

        List<String> attributeItems = Arrays.asList(mAttributeText.getText().toString().split("\\s*,\\s*"));
        JSONObject requestJson      = new JSONObject();

        try {
            requestJson.put("quantity",  mQuantityText.getText().toString());
            requestJson.put("value",     mValueText.getText().toString());
            requestJson.put("priority",  mPriorityText.getText().toString());
            requestJson.put("attribute", mAttributeText.getText().toString());
            requestJson.put("date",      mDateText.getText().toString());
            requestJson.put("email",     mEmailText.getText().toString());

            //in case attributes need to be separately listed
            requestJson.put("attributes", new JSONArray(attributeItems));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        emailSend(mEmailText.getText().toString(), String.valueOf(requestJson));
    }

    /**
     * Send email by using intent (simpler way)
     *
     * Possible enhancement:
     * Option 1: add custom client and send via SMTP external account
     *           require javax.mail and SMTP account setup
     * Option 2: pass data to API server and trigger send email from there
     *           can use build in PHP function mail() to send email
     * @param recipient
     * @param emailBody
     */
    private void emailSend(String recipient, String emailBody) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{recipient});
        i.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        i.putExtra(Intent.EXTRA_TEXT   , emailBody);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainForm.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
