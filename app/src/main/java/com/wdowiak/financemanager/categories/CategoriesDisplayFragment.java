package com.wdowiak.financemanager.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.commons.CommonDisplayFragment;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.IItem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CategoriesDisplayFragment extends CommonDisplayFragment<Category, CategoriesAdapter>
{
    @NotNull
    @Contract(" -> new")
    public static CategoriesDisplayFragment newInstance()
    {
        return new CategoriesDisplayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        itemType = IItem.Type.Category;
        detailClass = CategoryDetailActivity.class;
        addEditClass = CategoryAddEditActivity.class;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CategoriesDisplayFragmentViewModel.class);

        queryAndDisplayItems();
    }

    @Override
    protected CategoriesDisplayFragmentViewModel getViewModel()
    {
        return (CategoriesDisplayFragmentViewModel) super.getViewModel();
    }

    @Override
    protected CategoriesAdapter createItemAdapter()
    {
        return new CategoriesAdapter(viewModel.getData(), getActivity().getApplicationContext());
    }
}
