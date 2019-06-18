package com.wdowiak.financemanager.api;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.wdowiak.financemanager.data.AuthToken;
import com.wdowiak.financemanager.data.LoggedInUser;
import com.wdowiak.financemanager.data.LoginRepository;
import com.wdowiak.financemanager.data.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AuthApi
{
    public static Result<AuthToken> authUser(final String login, final String password)
    {
        Map<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        final JsonObjectRequest request = new JsonObjectRequest(authUrl, new JSONObject(params), future, future);

        final RequestQueue requestQueue = Volley.newRequestQueue(NetworkManager.getInstance().getContext());
        requestQueue.add(request);

        try
        {
            JSONObject result;
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

    public static Result<LoggedInUser> getUser(final String login)
    {
        Map<String, String> params = new HashMap<>();
        params.put("", login); // empty key name

        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, queryUserUrl, new JSONObject(params), future, future) {

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
                final JSONObject userJSONObject= result.getJSONArray("Items").getJSONObject(0);

                final int userId = userJSONObject.getInt("UserId");
                final String firstName = userJSONObject.getString("Firstname");
                final String lastName = userJSONObject.getString("Lastname");
                final String email = userJSONObject.getString("Email");
                final String avatarPath = userJSONObject.getString("AvatarPath");

                LoggedInUser user = new LoggedInUser(userId, login, firstName, lastName, email);
                return new Result.Success<>(user);
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

    final static String authUrl = EndpointUrl.url + "v1/users/authenticate";
    final static String queryUserUrl = EndpointUrl.url + "v1/users";
}
