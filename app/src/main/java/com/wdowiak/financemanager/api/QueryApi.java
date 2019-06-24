package com.wdowiak.financemanager.api;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.ItemFactory;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueryApi
{
    public static <T extends IItem> void getItems(final IItem.Type itemType, final Api.IQueryCallback<ArrayList<T>> callback)
    {
        Api.asyncQuery(Request.Method.GET, Api.getQueryUrl(itemType), new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    final JSONArray itemsJSONArray = response.getJSONArray("Items");

                    ArrayList<T> categories = new ArrayList<>();
                    for(int i = 0; i < itemsJSONArray.length(); ++i)
                    {
                        categories.add(ItemFactory.createItem(itemType, itemsJSONArray.getJSONObject(i)));
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

    public static <T extends IItem> void getItemById(
            final long itemId,
            final IItem.Type itemType,
            final Api.IQueryCallback<T> callback)
    {
        // hack or the right way?
        final String queryByIdUrl = Api.getQueryUrl(itemType) + itemId;

        Api.asyncQuery(Request.Method.GET, queryByIdUrl, new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    // No item found for this id
                    if(response == null)
                    {
                        callback.onSuccess(null);
                        return;
                    }

                    callback.onSuccess(ItemFactory.createItem(itemType, response));
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

    public static <T extends IItem> void createItem(
            @NotNull final T item,
            final IItem.Type itemType,
            final Api.IQueryCallback<T> callback)
    {
        final JSONObject params = item.createJSONObject();

        Api.asyncQuery(Request.Method.POST, Api.getQueryUrl(itemType), params, new Api.IApiCallback()
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

                    callback.onSuccess(ItemFactory.createItem(itemType, response));
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

    public static <T extends IItem> void updateItem(
            @NotNull final T item,
            final IItem.Type itemType,
            final Api.IQueryCallback<T> callback)
    {
        final JSONObject params = item.createJSONObject();
        final String updateItemUrl = Api.getQueryUrl(itemType) + item.getId();

        Api.asyncQuery(Request.Method.PUT, updateItemUrl, params, new Api.IApiCallback()
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

                    callback.onSuccess(ItemFactory.createItem(itemType, response));
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

    public static void deleteItemById(
            final long itemId,
            final IItem.Type itemType,
            final Api.IQueryCallback<String> callback)
    {
        // hack or the right way?
        final String deleteCategoryByIdUrl = Api.getQueryUrl(itemType) + itemId;

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
}
