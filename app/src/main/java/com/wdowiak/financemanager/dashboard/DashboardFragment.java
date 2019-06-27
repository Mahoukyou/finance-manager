package com.wdowiak.financemanager.dashboard;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wdowiak.financemanager.R;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class DashboardFragment extends Fragment
{
    private DashboardFragmentViewModel viewModel;

    @NotNull
    @Contract(" -> new")
    public static DashboardFragment newInstance()
    {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState
    ) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(DashboardFragmentViewModel.class);
    }
}
