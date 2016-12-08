package com.example.android.calki;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @version 1.0.2
 *          App Name: Calki
 *          <p>
 *          Note: There is a bug which is caused by android itself not the app which prints a line in the console
 *          in this format: E/Surface: getSlotFromBufferLocked: unknown buffer: 0x7f398f684140
 *          Again this is an Operating System but not the app's
 */
public class Calki extends AppCompatActivity {

    boolean newOp = true;   // A flag for the app to make it recognize a new operation
    double holder = 0;      // A holder variable used for checking and swapping values
    char operation = ' ';   // A variable used to hold the current mathematical operation

    Button del;             // A Button object to reference the DEL button in the UI
    TextView bigText;       // A TextView object to reference the Upper TextView with the big bold text
    TextView smallText;     // A TextView object to reference the Lower TextView with the smaller normal text

    @Override
    /**
     *  A default method created with the project to start the app
     *  It's main job is to initialize the app and linking the layout with this class
     *
     *  This method is also used to setup objects, app status and everything else needed at the start
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This small block is to hide the App Bar that appears at the top of each normal android app
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Sets the content of the app view to the referenced Layout "activity_calki"
        setContentView(R.layout.activity_calki);

        /*
            Set up variables and objects after this comment section
         */
        //Linking the DEL button with this del object
        del = (Button) findViewById(R.id.delete);
        //Setting a Long Click Listener to the DEL button to enable the reset button from the launch of the app
        del.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                bigText.setText("0");
                smallText.setText("");
                newOp = true;
                showToast("Inputs were reset");
                return true;
            }
        });

        //Linking the smallText object with its XML element IDed as "smallResult"
        smallText = (TextView) findViewById(R.id.smallResult);
        //Linking the bigText object with its XML element IDed as "bigResult"
        bigText = (TextView) findViewById(R.id.bigResult);
    }


    /**
     * Set of repeatedly used helper methods put here to apply the principle of 'Separation of Concerns'
     *
     */

    /**
     * This method is used to calculate the values that is stored in both the BigText and SmallText
     * and then post the result in BigText
     * Note that this method is only called when "Equals" button is pressed
     *
     * @param st shortcut for small text which is passed into this method
     * @param bt shortcut for big   text which is passed into this method
     */
    protected void calculate(TextView st, TextView bt) {
        //Making the Small text as the firstAmount because it is written first in the operation
        //e.g. Press (4) then press (+) then press (5). So now 4 is in SmallText and 5 is in BigText
        double firstAmount = Double.parseDouble(st.getText().toString());
        //Making the Big text as the secondAmount because it is written second during the operation
        double secondAmount = Double.parseDouble(bt.getText().toString());

        //Executing depending on the operation
        if (operation == '+')
            holder = firstAmount + secondAmount;
        else if (operation == '-')
            holder = firstAmount - secondAmount;
        else if (operation == '*')
            holder = firstAmount * secondAmount;
        else if (secondAmount == 0) { // Critical Step, which requires checking for division by ZERO
            holder = 0;
            showToast("Invalid division"); //Feedback for the user
        } else {
            holder = firstAmount / secondAmount;
        }


        //Posting the result in the BigText or "bt"
        //But first we chick the double value holder is the same as the integer
        //for the simple purpose of making the result pretty and compact
        if (holder == (int) holder) {
            bt.setText((int) holder + "");
        } else {
            bt.setText(holder + "");
        }

    }

    /**
     * This method is used to calculate the values that is stored in both the BigText and SmallText
     * and then post the result in BigText
     * Note that this method is only called when of the operations buttons are pressed
     * Why? to enable the chain of operations. (i.e. 3+4-5*7/3+45-33 ... etc)
     * Thus the name of this method is: calculateWithOp -> calculate With Operation
     *
     * @param st shortcut for small text which is passed into this method
     * @param bt shortcut for big   text which is passed into this method
     */
    protected void calculateWithOp(TextView st, TextView bt) {
        //Making the Small text as the firstAmount because it is written first in the operation
        //e.g. Press (4) then press (+) then press (5). So now 4 is in SmallText and 5 is in BigText
        double firstAmount = Double.parseDouble(st.getText().toString());
        //Making the Big text as the secondAmount because it is written second during the operation
        double secondAmount = Double.parseDouble(bt.getText().toString());


        //Executing depending on the operation
        if (operation == '+')
            holder = firstAmount + secondAmount;
        else if (operation == '-')
            holder = firstAmount - secondAmount;
        else if (operation == '*')
            holder = firstAmount * secondAmount;
        else if (secondAmount == 0) { // Critical Step, which requires checking for division by ZERO
            holder = 0;
            showToast("Invalid division"); //Feedback for the user
        } else {
            holder = firstAmount / secondAmount;
        }


        //Posting the result in the BigText or "bt"
        //But first we chick the double value holder is the same as the integer
        //for the simple purpose of making the result pretty and compact
        if (holder == (int) holder) {
            st.setText((int) holder + "");
        } else {
            st.setText(holder + "");
        }

    }

    /**
     * This method is to update the BigText contents when either the pressed number with
     * the corresponding number or even with result when the equals button is pressed
     * The method take one parameter "input" which the desired value to be displayed in
     * BigText object
     *
     * @param input The next entry to be added or the new value of the BigText
     */
    protected void updateBigText(String input) {
        //Storing the contents of BigText as String in this Object
        String holder = bigText.getText().toString();

        if (newOp) {//New Operation case
            if (input.equalsIgnoreCase(".")) { //handling the case of the first input in an operation is a DOT
                bigText.setText("0" + input); //i.e. (0.)
                newOp = false; //Flip the flag to make the current ongoing chain of operations as NOT new operation
            } else {
                bigText.setText(input); //Whatever is pressed in the number pad
                newOp = false; //Flip the flag to make the current ongoing chain of operations as NOT new operation
            }
        } else { // The current ongoing chain of operations case
            bigText.setText(holder + input);
        }
    }

    /**
     * Simple method with no parameters, its sole purpose is to check and prevent
     * the user from entering multiple DOTs in the operation for correctness and robustness issues
     */
    protected void checkForDot() {
        //Storing the contents of BigText as String in this Object
        String checker = bigText.getText().toString();

        if (checker.contains(".")) {
            //Do nothing
        } else {
            updateBigText(".");
        }
    }

    /**
     * This method is to handle Three consecutive repetitive tasks, which are:
     * 1- Swap the Contents of SmallText with the Contents of BigText OR with
     * the result of the ongoing chain of operations
     * 2- Set the current ongoing mathematical operation
     * 3- Reset the Value of BigText to ZERO in case of a new Operation
     * Thus it is called swapSetAndReset
     *
     * @param op a primitive char parameter to update the mathematical operation
     */
    protected void swapSetAndReset(char op) {
        //Storing SmallText contents in this helper object
        String sText = smallText.getText().toString();

        //Set the current ongoing mathematical operation
        operation = op;

        //Setting the ongoing operation flag to true to enable the user to update the
        //BigText without bothering him with deleting the old values
        newOp = true;
        //Note: Another benefit is to enable the chain of operation i.g. 3*3*3*3*3*3... etc

        //The Swap process and Resetting
        if (sText.length() == 0) { //Is is a new operation? which means no value in SmallText
            smallText.setText(bigText.getText().toString());
            bigText.setText("0");
        } else { //continue the chain of operation
            calculateWithOp(smallText, bigText);
        }

    }

    /**
     * This method enables the user to copy the result or the content of the BigText object
     * into the devices clipboard
     * <p>
     * This method is invoked by pressing the bigResult TextView
     *
     * @param v refers to the view that is calling this method witch is a TextView
     */
    public void copyToClipboard(View v) {
        //Storing the contents of BigText as String in this Object
        String bText = bigText.getText().toString();

        //Adding the contents of the object to the devices clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Result", bText);
        clipboard.setPrimaryClip(clip);

        showToast("Copied to clipboard"); //Feedback the user

    }

    /**
     * This method enables the user to copy the the content of the SmallText object
     * into the devices clipboard
     * <p>
     * This method is invoked by pressing the smallResult TextView
     *
     * @param v refers to the XML Element that is calling this method witch is a TextView
     */
    public void copyToClipboardSmall(View v) {
        //Storing the contents of SmallText as String in this Object
        String sText = smallText.getText().toString();

        //Adding the contents of the object to the devices clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Intermediate Result", sText);
        clipboard.setPrimaryClip(clip);

        showToast("Copied to clipboard"); //Feedback the user
    }


    /**
     * Simple method to show a toast that is holding a specific message
     * to the user
     *
     * @param message contains the message that intended to be shown to the user
     */
    public void showToast(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
    ///////////////////////////////////////////
    //// END OF HELPER METHODS
    ///////////////////////////////////////////

    /**
     * The following methods are for the numbers and the DOT
     * <p>
     * Each method correspond to a number stated by its name
     * When a button is pressed in the UI is calls its corresponding method in here
     * to update the contents of the BigText TextView
     */
    public void one(View v) {
        updateBigText("1");
    }

    public void two(View v) {
        updateBigText("2");
    }

    public void three(View v) {
        updateBigText("3");
    }

    public void four(View v) {
        updateBigText("4");
    }

    public void five(View v) {
        updateBigText("5");
    }

    public void six(View v) {
        updateBigText("6");
    }

    public void seven(View v) {
        updateBigText("7");
    }

    public void eight(View v) {
        updateBigText("8");
    }

    public void nine(View v) {
        updateBigText("9");
    }

    public void zero(View v) {
        updateBigText("0");
    }

    public void dot(View v) {
        checkForDot();
    } //checkForDot() checks first then calls updateBigText() with the proper input

    ///////////////////////////////////////////
    //// END OF NUMBERS METHODS AND THE DOT METHOD
    ///////////////////////////////////////////


    /**
     * The following methods are for operations
     */
    public void divide(View v) {
        swapSetAndReset('/');
    }

    public void multiply(View v) {
        swapSetAndReset('*');
    }

    public void minus(View v) {
        swapSetAndReset('-');
    }

    public void plus(View v) {
        swapSetAndReset('+');
    }

    /**
     * This method is used to delete the last digit entered by the user
     * Also if pressed and held it will reset the calculator so the user
     * can start a new operation immediately
     *
     * @param v refers to the XML Element that is calling this method
     */
    public void delete(View v) {
        //Handling different scenarios when DEL is pressed
        if (bigText.getText().toString().length() <= 1) { //Setting the BigText to ZERO when only one digit left and resetting the calculator
            bigText.setText("0");
            newOp = true;
        } else { //Just delete the last digit
            bigText.setText(bigText.getText().toString().substring(0, bigText.getText().toString().length() - 1));
        }
    }

    /**
     * This method handles the output of the final result of the operation
     * Then resets the status of the calculator to a new operation
     *
     * @param v refers to the XML Element that is calling this method
     */
    public void equals(View v) {
        //Storing the contents of SmallText as String in this Object
        String checker = smallText.getText().toString();

        if (checker.length() == 0) { //No other value to perform an operation with
            //Do nothing
        } else {
            calculate(smallText, bigText);
            smallText.setText("");
        }

        newOp = true; //Resets the status of the calculator to a new operation
    }

    ///////////////////////////////////////////
    //// END OF OPERATIONS METHODS
    ///////////////////////////////////////////
}
