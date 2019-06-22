package com.wdowiak.financemanager.categories;

import androidx.lifecycle.ViewModel;

import com.wdowiak.financemanager.data.Category;

import java.util.ArrayList;

public class CategoriesDisplayFragmentViewModel extends ViewModel
{
    public ArrayList<Category> categoriesData;
    public CategoriesAdapter categoriesAdapter;
}
