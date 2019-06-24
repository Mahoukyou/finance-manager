package com.wdowiak.financemanager.api;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Currency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CurrenciesApi
{
    public static void getCurrencies(final Api.IQueryCallback<ArrayList<Currency>> callback)
    {
        Api.asyncQuery(Request.Method.GET, currenciesUrl, new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    final JSONArray currenciesJSONArray = response.getJSONArray("Items");

                    ArrayList<Currency> currencies = new ArrayList<>();
                    for(int i = 0; i < currenciesJSONArray.length(); ++i)
                    {
                        currencies.add(Currency.createFromJSONObject(currenciesJSONArray.getJSONObject(i)));
                    }

                    callback.onSuccess(currencies);
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

    public static void getCurrencyById(final long currencyId, final Api.IQueryCallback<Currency> callback)
    {
        // hack or the right way?
        final String queryCurrencyByIdUrl = currenciesUrl + currencyId;

        Api.asyncQuery(Request.Method.GET, queryCurrencyByIdUrl, new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    // No currency found for this id
                    if(response == null)
                    {
                        callback.onSuccess(null);
                        return;
                    }

                    callback.onSuccess(Currency.createFromJSONObject(response));
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

    public static void createCurrency(final Currency currency, final Api.IQueryCallback<Currency> callback)
    {
        final JSONObject params = currency.createJSONObject();

        Api.asyncQuery(Request.Method.POST, currenciesUrl, params, new Api.IApiCallback()
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

                    callback.onSuccess(Currency.createFromJSONObject(response));
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

    public static void updateCurrency(final Currency currency, final Api.IQueryCallback<Currency> callback)
    {
        final JSONObject params = currency.createJSONObject();
        final String updateCurrencyUrl = currenciesUrl + currency.getId();

        Api.asyncQuery(Request.Method.PUT, updateCurrencyUrl, params, new Api.IApiCallback()
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

                    callback.onSuccess(Currency.createFromJSONObject(response));
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

    public static void deleteCurrencyByid(final long currencyId, final Api.IQueryCallback<String> callback)
    {
        // hack or the right way?
        final String deleteCurrencyByIdUrl = currenciesUrl + currencyId;

        Api.asyncStringQuery(Request.Method.DELETE, deleteCurrencyByIdUrl, new Api.IApiStringCallback()
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

    final static String currenciesUrl = EndpointUrl.url + "v1/currencies/";
}
