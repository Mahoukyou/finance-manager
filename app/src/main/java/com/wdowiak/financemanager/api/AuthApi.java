package com.wdowiak.financemanager.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.wdowiak.financemanager.api.interfaces.IAuthCallback;
import com.wdowiak.financemanager.data.AuthToken;
import com.wdowiak.financemanager.data.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AuthApi
{
    public AuthApi(Context context, IAuthCallback callback)
    {
        this.context = context;
        this.authCallback = callback;
    }

    public Result<AuthToken> authUser(final String login, final String password)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        final JsonObjectRequest request = new JsonObjectRequest(authUrl, new JSONObject(params), future, future);

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);

        try
        {
            JSONObject result = null;
            try
            {
                result = future.get(30, TimeUnit.SECONDS);
            }
            catch (TimeoutException error)
            {
                return new Result.Error(error);
            }

            if(result == null)
            {
                return new Result.Error(new Exception("Empty query result returned"));
            }

            try
            {
                final String token = result.getString("Token");
                if(!token.isEmpty())
                {
                    return new Result.Success<>(new AuthToken(token));
                }
                else
                {
                    return new Result.Error(new Exception("Empty token returned"));
                }
            }
            catch (JSONException error)
            {
                return new Result.Error(error);
            }
        }
        catch (InterruptedException error)
        {
            error.printStackTrace();

            return new Result.Error(error);
        }
        catch(ExecutionException error)
        {
            error.printStackTrace();

            return new Result.Error(error);
        }
    }

    final static String authUrl = EndpointUrl.url + "/v1/users/authenticate";

    Context context;
    IAuthCallback authCallback;
}
