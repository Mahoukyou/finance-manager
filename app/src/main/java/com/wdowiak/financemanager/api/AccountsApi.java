package com.wdowiak.financemanager.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wdowiak.financemanager.data.Account;
import com.wdowiak.financemanager.data.LoginRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountsApi
{
    public interface IAccountsCallback<T>
    {
        void OnSuccess(T result);
        void OnError(Exception error);
    }

    public static void getAccounts(final IAccountsCallback<ArrayList<Account>> callback)
    {
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                queryAccountsUrl,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            final JSONArray accountsJSONArray = response.getJSONArray("Items");

                            ArrayList<Account> accounts = new ArrayList<>();

                            for(int i = 0; i < accountsJSONArray.length(); ++i)
                            {
                                accounts.add(Account.createFromJSONObject(accountsJSONArray.getJSONObject(i)));
                            }

                            callback.OnSuccess(accounts);

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

    public static void getAccountById(final long accountId, final IAccountsCallback<Account> callback)
    {
        // hack or the right way?
        final String queryAccountByIdUrl = queryAccountsUrl + accountId;

        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                queryAccountByIdUrl,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            // No account found for this id
                            if(response == null)
                            {
                                callback.OnSuccess(null);
                                return;
                            }

                            callback.OnSuccess(Account.createFromJSONObject(response));
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

    final static String queryAccountsUrl = EndpointUrl.url + "v1/accounts/";
}
