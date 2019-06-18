package com.wdowiak.financemanager.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.wdowiak.financemanager.Transaction;
import com.wdowiak.financemanager.data.LoginRepository;
import com.wdowiak.financemanager.data.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class TransactionsApi
{
    public interface ITransactionCallback
    {
        void OnSuccess(ArrayList<Transaction> transactions);
        void OnError(Exception error);
    }

    public static void getTransactions(final ITransactionCallback callback)
    {
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                queryTransactionsUrl,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            final JSONArray transactionsJSONArray = response.getJSONArray("Items");

                            ArrayList<Transaction> transactions = new ArrayList<>();

                            for(int i = 0; i < transactionsJSONArray.length(); ++i)
                            {
                                transactions.add(Transaction.createFromJSONObject(transactionsJSONArray.getJSONObject(i)));
                            }

                            callback.OnSuccess(transactions);

                        }
                        catch (JSONException error)
                        {
                            callback.OnError(error);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        onErrorResponse(error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("User-Agent", "Finance Manager");
                params.put("Accept-Language", "en");
                params.put("Authorization", LoginRepository.getInstance().getAuthToken().getFullAuthToken());

                return params;
            }
        };

        final RequestQueue requestQueue = Volley.newRequestQueue(NetworkManager.getInstance().getContext());
        requestQueue.add(request);
    }

    final static String queryTransactionsUrl = EndpointUrl.url + "v1/transactions";
}
