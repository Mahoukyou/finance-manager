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

public class LoginActivity extends AppCompatActivity
{
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NetworkManager.createInstance(getApplicationContext());

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register_instead);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        registerButton.setOnClickListener(this::registerInstead);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>()
        {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState)
            {
                if (loginFormState == null)
                {
                    return;
                }

                loginButton.setEnabled(loginFormState.isDataValid());

                if (loginFormState.getUsernameError() != null)
                {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }

                if (loginFormState.getPasswordError() != null)
                {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>()
        {
            @Override
            public void onChanged(@Nullable LoginResult loginResult)
            {
                if (loginResult == null)
                {
                    return;
                }

                loadingProgressBar.setVisibility(View.GONE);

                if (loginResult.getError() != null)
                {
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
                loadingProgressBar.setVisibility(View.VISIBLE);

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
}
