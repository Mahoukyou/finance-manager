package com.wdowiak.financemanager.api;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.wdowiak.financemanager.data.LoginRepository;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// TODO, apis(get, create, update, delete) for every data class can be done by a single generic "class"
// redo this, too lazy to do it now, so copy paste it is

public class Api
{
    public interface IApiCallback
    {
        void onResponse(JSONObject response);
        void onErrorResponse(VolleyError error);
    }

    public interface IApiStringCallback
    {
        void onResponse(String response);
        void onErrorResponse(VolleyError error);
    }

    public interface IQueryCallback<T>
    {
        void onSuccess(T result);
        void onError(Exception error);
    }

    public static void asyncQuery(final int method, final String queryUrl, JSONObject params, final IApiCallback callback)
    {
        final JsonObjectRequest request = new JsonObjectRequest(
                method,
                queryUrl,
                params,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        callback.onResponse(response);
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        callback.onErrorResponse(error);
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders()
            {
                return Api.getHeaders();
            }
        };

        final RequestQueue requestQueue = Volley.newRequestQueue(NetworkManager.getInstance().getContext());
        requestQueue.add(request);
    }

    public static void asyncStringQuery(final int method, final String queryUrl, final IApiStringCallback callback)
    {
        final StringRequest request = new StringRequest(
                method,
                queryUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        callback.onResponse(response);
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        callback.onErrorResponse(error);
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders()
            {
                return Api.getHeaders();
            }
        };

        final RequestQueue requestQueue = Volley.newRequestQueue(NetworkManager.getInstance().getContext());
        requestQueue.add(request);
    }

    private static Map<String, String> getHeaders()
    {
        Map<String, String>  params = new HashMap<>();
        params.put("User-Agent", "Finance Manager");
        params.put("Accept-Language", "en");
        params.put("Authorization", LoginRepository.getInstance().getAuthToken().getFullAuthToken());

        return params;
    }
}
