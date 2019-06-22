package com.wdowiak.financemanager.categories;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.CategoriesApi;
import com.wdowiak.financemanager.data.Category;

public class CategoryDetailActivity extends AppCompatActivity
{
    public final static String INTENT_EXTRA_CATEGORY_ID = "INTENT_EXTRA_CATEGORY_ID";

    Long categoryId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        categoryId = getIntent().getExtras().getLong(INTENT_EXTRA_CATEGORY_ID);
        queryCategory();
    }

    final private void queryCategory()
    {
        CategoriesApi.getCategoryById(categoryId, new Api.IQueryCallback<Category>() {
            @Override
            public void onSuccess(Category category)
            {
                if(category == null)
                {
                    Toast.makeText(getApplicationContext(), "Category[id= " + categoryId + "] does not exist", Toast.LENGTH_SHORT);
                    finish();

                    return;
                }

                updateDetailViewInfo(category);
                showDetailViewLayout();
            }

            @Override
            public void onError(Exception error)
            {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    final void showDetailViewLayout()
    {
        final LinearLayout detailViewLayout = findViewById(R.id.detail_view_layout);
        detailViewLayout.setVisibility(View.VISIBLE);

        final LinearLayout progressBarLayout = findViewById(R.id.progress_bar_layout);
        progressBarLayout.setVisibility(View.GONE);
    }

    final void updateDetailViewInfo(final Category category)
    {
        if(category == null)
        {
            throw new NullPointerException("Category is not valid");
        }

        TextView textView = findViewById(R.id.category_name);
        textView.setText(category.getName());

        // todo, transactions info
    }

    public final void beginEditAccount(final View view)
    {
       // Intent intent = new Intent(getApplicationContext(), TransactionAddEditActivity.class);
       // intent.putExtra(TransactionAddEditActivity.INTENT_EXTRA_TRANSACTION_ID, transactionId);
       // startActivity(intent);
    }
}
