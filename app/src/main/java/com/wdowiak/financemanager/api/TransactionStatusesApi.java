package com.wdowiak.financemanager.api;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.wdowiak.financemanager.data.TransactionStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransactionStatusesApi
{
    public static void getTransactionStatuses(final Api.IQueryCallback<ArrayList<TransactionStatus>> callback)
    {
        Api.asyncQuery(Request.Method.GET, transactionStatusUrl, new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    final JSONArray categoriesJSONArray = response.getJSONArray("Items");

                    ArrayList<TransactionStatus> categories = new ArrayList<>();
                    for(int i = 0; i < categoriesJSONArray.length(); ++i)
                    {
                        categories.add(TransactionStatus.createFromJSONObject(categoriesJSONArray.getJSONObject(i)));
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

    public static void getTransactionStatusById(final long transactionStatusId, final Api.IQueryCallback<TransactionStatus> callback)
    {
        // hack or the right way?
        final String queryTransactionStatusByIdUrl = transactionStatusUrl + transactionStatusId;

        Api.asyncQuery(Request.Method.GET, queryTransactionStatusByIdUrl, new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    // No transactions status found for this id
                    if(response == null)
                    {
                        callback.onSuccess(null);
                        return;
                    }

                    callback.onSuccess(TransactionStatus.createFromJSONObject(response));
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

    public static void createTransactionStatus(final TransactionStatus transactionStatus, final Api.IQueryCallback<TransactionStatus> callback)
    {
        final JSONObject params = transactionStatus.createJSONObject();

        Api.asyncQuery(Request.Method.POST, transactionStatusUrl, params, new Api.IApiCallback()
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

                    callback.onSuccess(TransactionStatus.createFromJSONObject(response));
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

    public static void updateTransactionStatus(final TransactionStatus transactionStatus, final Api.IQueryCallback<TransactionStatus> callback)
    {
        final JSONObject params = transactionStatus.createJSONObject();
        final String updateTransactionStatusUrl = transactionStatusUrl + transactionStatus.getId();

        Api.asyncQuery(Request.Method.PUT, updateTransactionStatusUrl, params, new Api.IApiCallback()
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

                    callback.onSuccess(TransactionStatus.createFromJSONObject(response));
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

    public static void deleteTransactionStatusById(final long transactionStatusId, final Api.IQueryCallback<String> callback)
    {
        // hack or the right way?
        final String deleteTransactionStatusByIdUrl = transactionStatusUrl + transactionStatusId;

        Api.asyncStringQuery(Request.Method.DELETE, deleteTransactionStatusByIdUrl, new Api.IApiStringCallback()
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

    final static String transactionStatusUrl = EndpointUrl.url + "v1/transactionstatuses/";
}
