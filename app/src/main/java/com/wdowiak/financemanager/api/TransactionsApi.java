package com.wdowiak.financemanager.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.data.LoginRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransactionsApi
{
    public interface ITransactionCallback<T>
    {
        void OnSuccess(T result);
        void OnError(Exception error);
    }

    public static void getTransactions(final ITransactionCallback<ArrayList<Transaction>> callback)
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

    public static void getTransactionById(final long transactionId, final ITransactionCallback<Transaction> callback)
    {

/*        // todo,doesnt work
        Map<String, String> params = new HashMap<>();
        params.put("", String.valueOf(transactionId)); // empty key
        params.put("id", String.valueOf(transactionId)); // empty key
*/

        // hack or the right way?
        final String queryTransactionByIdUrl = queryTransactionsUrl + transactionId;

        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                queryTransactionByIdUrl,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            // No transaction found for this id
                            if(response == null)
                            {
                                callback.OnSuccess(null);
                                return;
                            }

                            callback.OnSuccess(Transaction.createFromJSONObject(response));
                        }
                        catch (JSONException error)
                        {
                            error.printStackTrace();
                            callback.OnError(error);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
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

    final static String queryTransactionsUrl = EndpointUrl.url + "v1/transactions/";
}
