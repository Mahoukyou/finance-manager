package com.wdowiak.financemanager;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestAPI implements IDatabaseInterface
{
    RestAPI(Context context)
    {
        this.context = context;
    }


    static final String endpoint_url = "http://192.168.1.25:51234/api/";

    public ArrayList<Transaction> getTransactions()
    {
        return new ArrayList<>();
    }

    public boolean newTransaction(Transaction transaction)
    {
        return false;
    }

    public String getUser()
    {



        return "";
    }

    public final void authUser(final String login, final String password, final IAuthCallback authCallback)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        final JsonObjectRequest request = new JsonObjectRequest(
                endpoint_url + "/v1/users/authenticate",
                new JSONObject(params),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            String token = response.getString("Token");
                            if(!token.isEmpty())
                            {
                                auth_token = token;
                                authCallback.onSuccessfullAuth();
                            }
                            else
                            {
                                authCallback.onUnsuccessfullAuth();
                            }
                        }
                        catch (JSONException e)
                        {
                            authCallback.onUnsuccessfullAuth();
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        authCallback.onUnsuccessfullAuth();
                    }
                }
        );

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public final void logout()
    {
        auth_token = "";
    }

    // TODO, for now keep the token here
    private String auth_token;

    Context context;
}
