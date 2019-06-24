package com.wdowiak.financemanager.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.LoginRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoriesApi
{
    public static void getCategories(final Api.IQueryCallback<ArrayList<Category>> callback)
    {
        Api.asyncQuery(Request.Method.GET, queryCategoriesUrl, new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    final JSONArray categoriesJSONArray = response.getJSONArray("Items");

                    ArrayList<Category> categories = new ArrayList<>();
                    for(int i = 0; i < categoriesJSONArray.length(); ++i)
                    {
                        categories.add(Category.createFromJSONObject(categoriesJSONArray.getJSONObject(i)));
                    }

                    callback.onSuccess(categories);
                }
                catch (JSONException error)
                {
                    callback.onError(error);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error);
            }
        });
    }

    public static void getCategoryById(final long categoryId, final Api.IQueryCallback<Category> callback)
    {
        // hack or the right way?
        final String queryCategoryByIdUrl = queryCategoriesUrl + categoryId;

        Api.asyncQuery(Request.Method.GET, queryCategoryByIdUrl, new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    // No cateogry found for this id
                    if(response == null)
                    {
                        callback.onSuccess(null);
                        return;
                    }

                    callback.onSuccess(Category.createFromJSONObject(response));
                }
                catch (JSONException error)
                {
                    callback.onError(error);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error);
            }
        });
    }

    public static void createCategory(final Category category, final Api.IQueryCallback<Category> callback)
    {
        final JSONObject params = category.createJSONObject();

        Api.asyncQuery(Request.Method.POST, queryCategoriesUrl, params, new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    if(response == null)
                    {
                        callback.onSuccess(null);
                        return;
                    }

                    callback.onSuccess(Category.createFromJSONObject(response));
                }
                catch (JSONException error)
                {
                    callback.onError(error);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error);
            }
        });
    }

    public static void updateCategory(final Category category, final Api.IQueryCallback<Category> callback)
    {
        final JSONObject params = category.createJSONObject();
        final String updateCategoryUrl = queryCategoriesUrl + category.getId();

        Api.asyncQuery(Request.Method.PUT, updateCategoryUrl, params, new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    if(response == null)
                    {
                        callback.onSuccess(null);
                        return;
                    }

                    callback.onSuccess(Category.createFromJSONObject(response));
                }
                catch (JSONException error)
                {
                    callback.onError(error);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error);
            }
        });
    }

    public static void deleteCategoryById(final long categoryId, final Api.IQueryCallback<String> callback)
    {
        // hack or the right way?
        final String deleteCategoryByIdUrl = queryCategoriesUrl + categoryId;

        Api.asyncStringQuery(Request.Method.DELETE, deleteCategoryByIdUrl, new Api.IApiStringCallback()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccess(response);
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error);
            }
        });
    }

    final static String queryCategoriesUrl = EndpointUrl.url + "v1/categories/";
}
