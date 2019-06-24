package com.wdowiak.financemanager.categories;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wdowiak.financemanager.CommonDetailViewActivity;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.groups.GroupAddEditActivity;

public class CategoryDetailActivity extends CommonDetailViewActivity<Category>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        // todo, no aaddedit for account just yet
        addEditClass = GroupAddEditActivity.class;
        itemType = IItem.Type.Category;

        afterCreate();
    }

    @Override
    protected final void updateDetailViewInfo(final Category category)
    {
        if(category == null)
        {
            throw new NullPointerException("Category is not valid");
        }

        TextView textView = findViewById(R.id.category_name);
        textView.setText(category.getName());

        // todo, transactions info
    }

}
