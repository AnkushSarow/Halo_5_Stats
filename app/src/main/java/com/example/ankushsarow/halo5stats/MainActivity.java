package com.example.ankushsarow.halo5stats;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText gtInput;
    private TextView invalidText;
    private Button statsButton;
    public final String USER_GT = "USER_GT";
    private String userGT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gtInput = (EditText) findViewById(R.id.gt_input);
        invalidText = (TextView) findViewById(R.id.invalid_gt);
        statsButton = (Button) findViewById(R.id.stats_button);

        /**
         * Handle the user pressing the enter key on input into the EditText field
         * by hiding the keyboard
         */
        gtInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    return true;
                }

                return false;
            }
        });

        /**
         * Handle the user pressing the stats button - Retrieve the text from gamertag input,
         * if it is invalid, display the invalid text field, otherwise, switch activities
         * to the stats overview activity
         */
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userGT = gtInput.getText().toString();

                //System.out.println(userGT);

                //GT input was not valid
                //invalidText.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                intent.putExtra(USER_GT, userGT);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
