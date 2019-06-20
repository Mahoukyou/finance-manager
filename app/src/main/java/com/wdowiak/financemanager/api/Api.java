package com.wdowiak.financemanager.api;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.wdowiak.financemanager.data.LoginRepository;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Api
{
    public interface IApiCallback
    {
        void onResponse(JSONObject response);
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
}
