package com.wdowiak.financemanager.categories;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProviders;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.CommonAddEditActivity;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.IItem;

public class CategoryAddEditActivity extends CommonAddEditActivity<Category, CategoryAddEditFormState>
{
    EditText categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add_edit);

        itemType = IItem.Type.Category;

        categoryName = findViewById(R.id.category_name);
        categoryName.addTextChangedListener(afterTextChangedListener);

        afterCreate();
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
            getViewModel().dataChanged(categoryName.getText().toString());
        }
    };

    protected CategoryAddEditViewModel createViewModel()
    {
        return ViewModelProviders.of(this).get(CategoryAddEditViewModel.class);
    }

    protected void updateAddEditView()
    {
        final Category category = getViewModel().getQueriedItem();
        if(category == null)
        {
            throw new RuntimeException("Category should not be null");
        }

        final EditText editText = findViewById(R.id.category_name);
        editText.setText(category.getName());
    }

    protected Category createItemFromInput()
    {
        if(!getViewModel().getFormState().getValue().isDataValid())
        {
            return null;
        }

        // todo, redo somehow else?
        Category newCategory = null;
        if(isEdit())
        {
            newCategory = new Category(getViewModel().getItemId(), categoryName.getText().toString());
        }
        else
        {
            newCategory = new Category(categoryName.getText().toString());
        }

        return newCategory;
    }

    protected void onFormStateChanged(CategoryAddEditFormState formState)
    {
        categoryName.setError(formState.getNameError() != null ? getString(formState.getNameError()) : null);
    }

    protected CategoryAddEditViewModel getViewModel()
    {
        return (CategoryAddEditViewModel) super.getViewModel();
    }
}
