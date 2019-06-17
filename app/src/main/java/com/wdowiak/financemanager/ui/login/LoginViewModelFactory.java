package com.wdowiak.financemanager.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.wdowiak.financemanager.data.LoginDataSource;
import com.wdowiak.financemanager.data.LoginRepository;

public class LoginViewModelFactory implements ViewModelProvider.Factory
{
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(LoginViewModel.class))
        {
            return (T) new LoginViewModel(LoginRepository.getInstance(new LoginDataSource()));
        }
        else
        {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
