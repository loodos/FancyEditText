package com.loodos.fancyedittextsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loodos.fancyedittext.FancyEditText;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private FancyEditText mEmailView;
    private FancyEditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin() {
        mPasswordView.clearFocus();
        mEmailView.clearFocus();
        if (isEmailValid(mEmailView.getText().toString()) && isPasswordValid(mPasswordView.getText().toString())) {
            mEmailView.success();
            mPasswordView.success();
            Toast.makeText(this, getString(R.string.logged_in), Toast.LENGTH_SHORT).show();
        }
        if (!isEmailValid(mEmailView.getText().toString()) && isPasswordValid(mPasswordView.getText().toString())) {
            mEmailView.setError(null);
            mPasswordView.success();
            Toast.makeText(this, getString(R.string.check_email), Toast.LENGTH_SHORT).show();
        }
        if (isEmailValid(mEmailView.getText().toString()) && !isPasswordValid(mPasswordView.getText().toString())) {
            mEmailView.success();
            mPasswordView.setError(null);
            Toast.makeText(this, getString(R.string.check_pwd), Toast.LENGTH_SHORT).show();
        }
        if (!isEmailValid(mEmailView.getText().toString()) && !isPasswordValid(mPasswordView.getText().toString())) {
            mEmailView.setError(null);
            mPasswordView.setError(null);
            Toast.makeText(this, getString(R.string.check_fields), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 5;
    }
}

