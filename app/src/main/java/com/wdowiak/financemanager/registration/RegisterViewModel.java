package com.wdowiak.financemanager.registration;

import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wdowiak.financemanager.R;

public class RegisterViewModel extends ViewModel
{
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();

    public LiveData<RegisterFormState> getRegisterFormState()
    {
        return registerFormState;
    }

    void dataChanged(
            @Nullable String login,
            @Nullable String firstName,
            @Nullable String lastName,
            @Nullable String email,
            @Nullable String password,
            @Nullable String repeatPassword)
    {
        @Nullable Integer loginError = null;
        @Nullable Integer firstNameError = null;
        @Nullable Integer lastNameError = null;
        @Nullable Integer emailError = null;
        @Nullable Integer passwordError = null;
        @Nullable Integer repeatPasswordError = null;

        if(login == null || login.length() < 2)
        {
            loginError = R.string.morethan1char;
        }

        if(firstName == null || firstName.length() < 2)
        {
            firstNameError = R.string.morethan1char;
        }

        if(lastName == null || firstName.length() < 2)
        {
            lastNameError = R.string.morethan1char;
        }

        if(email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailError = R.string.invalid_email;
        }

        if(password == null || password.length() < 3)
        {
            passwordError = R.string.invalid_password;
        }

        if(repeatPassword == null || !repeatPassword.equals(password))
        {
            repeatPasswordError = R.string.pass_match;
        }

        registerFormState.setValue(new RegisterFormState(
                loginError,
                firstNameError,
                lastNameError,
                emailError,
                passwordError,
                repeatPasswordError));
    }
}
