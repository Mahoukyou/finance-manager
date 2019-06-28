package com.wdowiak.financemanager.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.UserHandle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.AuthApi;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.NewUser;

public class RegisterActivity extends AppCompatActivity
{
    RegisterViewModel viewModel;
    Button registerButton;
    EditText login, firstName, lastName, email, password, repeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.register);
        login = findViewById(R.id.login);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repeatPassword = findViewById(R.id.repeat_password);

        login.addTextChangedListener(afterTextChangedListener);
        firstName.addTextChangedListener(afterTextChangedListener);
        lastName.addTextChangedListener(afterTextChangedListener);
        email.addTextChangedListener(afterTextChangedListener);
        password.addTextChangedListener(afterTextChangedListener);
        repeatPassword.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(this::onRegister);

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        viewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>()
        {
            @Override
            public void onChanged(RegisterFormState registerFormState)
            {
                registerButton.setEnabled(registerFormState.isDataValid());

                login.setError(registerFormState.getLoginError() != null ? getResources().getString(registerFormState.getLoginError()) : null);
                firstName.setError(registerFormState.getFirstNameError() != null ? getResources().getString(registerFormState.getFirstNameError()) : null);
                lastName.setError(registerFormState.getLastNameError() != null ? getResources().getString(registerFormState.getLastNameError()) : null);
                email.setError(registerFormState.getEmailError() != null ? getResources().getString(registerFormState.getEmailError()) : null);
                password.setError(registerFormState.getPasswordError() != null ? getResources().getString(registerFormState.getPasswordError()) : null);
                repeatPassword.setError(registerFormState.getRepeatPasswordError() != null ? getResources().getString(registerFormState.getRepeatPasswordError()) : null);
            }
        });
    }

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
            viewModel.dataChanged(
                    login.getText().toString(),
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    repeatPassword.getText().toString());
        }
    };

    private void onRegister(View view)
    {
        if(!viewModel.getRegisterFormState().getValue().isDataValid())
        {
            return;
        }

        AuthApi.createUser(createUserFromInput(), new Api.IQueryCallback<NewUser>()
        {
            @Override
            public void onSuccess(NewUser newUser/*unused*/)
            {
                if(getApplicationContext() == null)
                {
                    return;
                }

                Toast.makeText(getApplicationContext(), "Registered successfully, you can login now", Toast.LENGTH_SHORT);
                finish();
            }

            @Override
            public void onError(Exception error)
            {
                if(getApplicationContext() == null)
                {
                    return;
                }

                Toast.makeText(getApplicationContext(), "Could not register an user: " + error.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    private NewUser createUserFromInput()
    {
        return new NewUser(
                login.getText().toString(),
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                password.getText().toString());
    }

}
