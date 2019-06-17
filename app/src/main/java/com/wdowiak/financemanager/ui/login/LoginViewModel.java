package com.wdowiak.financemanager.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Patterns;

import com.wdowiak.financemanager.api.AuthApi;
import com.wdowiak.financemanager.api.NetworkManager;
import com.wdowiak.financemanager.data.LoginRepository;
import com.wdowiak.financemanager.data.Result;
import com.wdowiak.financemanager.data.LoggedInUser;
import com.wdowiak.financemanager.R;

public class LoginViewModel extends ViewModel
{
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository)
    {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState()
    {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult()
    {
        return loginResult;
    }

    public void login(String username, String password)
    {
        new AsyncLoginJob().execute(username, password);
    }

    public void loginDataChanged(String username, String password)
    {
        if (!isUserNameValid(username))
        {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        }
        else if (!isPasswordValid(password))
        {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
        else
        {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username)
    {
        if (username == null)
        {
            return false;
        }
        if (username.contains("@"))
        {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        }
        else
        {
            return !username.trim().isEmpty();
        }
    }

    private boolean isPasswordValid(String password)
    {
        return password != null && password.trim().length() > 2;
    }

    class AsyncLoginJob extends AsyncTask<String, Void, Result<LoggedInUser>>
    {
        @Override
        protected Result<LoggedInUser> doInBackground(String... params)
        {
            return LoginRepository.getInstance().login(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Result<LoggedInUser> result)
        {
            if (result instanceof Result.Success)
            {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(data));
            }
            else
            {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        }
    }
}

