package com.konye.lande;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class RegisterActivity extends AppCompatActivity {

    EditText nameEditText, surnameEditText, sexEditText, emailEditText, passwordEditText,
    confirmPasswordEditText;
    Dialog addCardDialog, otpDialog;
    TextView registerHeader;
    Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerHeader = (TextView) findViewById(R.id.register_header);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        surnameEditText = (EditText) findViewById(R.id.surname_edit_text);
        sexEditText = (EditText) findViewById(R.id.sex_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.reg_password_edit_text);
        confirmPasswordEditText = (EditText) findViewById(R.id.reg_confirm_password_edit_text);
        registerButton = (Button) findViewById(R.id.register_button);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("helvetica_font_normal.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to show  the dialog
                addCardDialog = new Dialog(RegisterActivity.this);
                addCardDialog.setContentView(R.layout.add_card_dialog);
                addCardDialog.setCancelable(true);

                Button addCardConfirmButton = (Button) addCardDialog.findViewById(R.id.add_card_confirm_butn);
                Button addCardSkipButton = (Button) addCardDialog.findViewById(R.id.add_card_skipButton);

                addCardConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addCardDialog.cancel();
                        otpDialog = new Dialog(RegisterActivity.this);
                        otpDialog.setContentView(R.layout.otp_dialog);
                        otpDialog.setCancelable(true);

                        //set up widgets in the dialog layout
                        TextView otpTextView = (TextView) otpDialog.findViewById(R.id.otp_textView);
                        EditText otpConfirmPassword = (EditText) otpDialog.findViewById(R.id.otp_confirm_editText);
                        Button  otpConfirmButton = (Button) otpDialog.findViewById(R.id.otp_confirm_butn);

                        otpConfirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                                startActivity(intent);
                            }
                        });
                        otpDialog.show();
                    }
                });
                addCardSkipButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                        startActivity(intent);
                    }
                });
                addCardDialog.show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}

