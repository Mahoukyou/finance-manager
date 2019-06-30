package com.wdowiak.financemanager.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Network;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wdowiak.financemanager.MainActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.NetworkManager;
import com.wdowiak.financemanager.data.LoggedInUser;
import com.wdowiak.financemanager.registration.RegisterActivity;
import com.wdowiak.financemanager.ui.login.LoginViewModel;
import com.wdowiak.financemanager.ui.login.LoginViewModelFactory;

import lecho.lib.hellocharts.model.Line;

public class LoginActivity extends AppCompatActivity
{
    private LoginViewModel loginViewModel;

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    LinearLayout loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NetworkManager.createInstance(getApplicationContext());

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register_instead);
        loadingProgressBar = findViewById(R.id.progress_bar_layout);

        registerButton.setOnClickListener(this::registerInstead);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }

                loginButton.setEnabled(loginFormState.isDataValid());

                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }

                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>()
        {
            @Override
            public void onChanged(@Nullable LoginResult loginResult)
            {
                if (loginResult == null) {

                    return;
                }

                if (loginResult.getError() != null)
                {
                    showProgessBar(false);
                    loginButton.setEnabled(loginViewModel.getLoginFormState().getValue().isDataValid());
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null)
                {
                    updateUiWithUser(loginResult.getSuccess());
                }

                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    showProgessBar(true);
                    loginViewModel.login(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showProgessBar(true);

                loginButton.setEnabled(false);

                loginViewModel.login(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void registerInstead(View view)
    {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    private void updateUiWithUser(LoggedInUser model)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString)
    {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showProgessBar(boolean show)
    {
        final int login_view_visibility = show ? View.GONE : View.VISIBLE;
        usernameEditText.setVisibility(login_view_visibility);
        passwordEditText.setVisibility(login_view_visibility);
        loginButton.setVisibility(login_view_visibility);
        registerButton.setVisibility(login_view_visibility);

        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
