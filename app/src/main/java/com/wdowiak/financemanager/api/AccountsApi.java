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
                accountsUrl,
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
        final String queryAccountByIdUrl = accountsUrl + accountId;

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

    public static void createAccount(final Account account, final Api.IQueryCallback<Account> callback)
    {
        final JSONObject params = account.createJSONObject();

        Api.asyncQuery(Request.Method.POST, accountsUrl, params, new Api.IApiCallback()
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

                    callback.onSuccess(Account.createFromJSONObject(response));
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

    public static void updateAccount(final Account account, final Api.IQueryCallback<Account> callback)
    {
        final JSONObject params = account.createJSONObject();
        final String updateAccountUrl = accountsUrl + account.getId();

        Api.asyncQuery(Request.Method.PUT, updateAccountUrl, params, new Api.IApiCallback()
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

                    callback.onSuccess(Account.createFromJSONObject(response));
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

    public static void deleteAccountById(final long accountId, final Api.IQueryCallback<String> callback)
    {
        // hack or the right way?
        final String deleteAccountByIdUrl = accountsUrl + accountId;

        Api.asyncStringQuery(Request.Method.DELETE, deleteAccountByIdUrl, new Api.IApiStringCallback()
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

    final static String accountsUrl = EndpointUrl.url + "v1/accounts/";
}
