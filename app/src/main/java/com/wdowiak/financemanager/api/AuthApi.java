package com.wdowiak.financemanager.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.wdowiak.financemanager.api.interfaces.IAuthCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Future;

public class AuthApi
{
    AuthApi(Context context, IAuthCallback callback)
    {
        this.authCallback = callback;
    }

    public void authUser(final String login, final String password, Future<String> future)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        final JsonObjectRequest request = new JsonObjectRequest(
                EndpointUrl.url + "/v1/users/authenticate",
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
                                authCallback.onSuccessfullAuth(token);
                            }
                            else
                            {
                                authCallback.onUnsuccessfullAuth("Empty token returned");
                            }
                        }
                        catch (JSONException error)
                        {
                            authCallback.onUnsuccessfullAuth(error.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    future.
                    authCallback.onUnsuccessfullAuth(error.toString());
                }
        });

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    Context context;
    IAuthCallback authCallback;
}
