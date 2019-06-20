package com.wdowiak.financemanager.api;


import android.content.Context;

public class NetworkManager
{
    private static NetworkManager instance = null;

    private NetworkManager(Context context)
    {
        this.context = context;
    }

    public static NetworkManager getInstance()
    {
        if(instance == null)
        {
            throw new RuntimeException("Network manager instance is not valid");
        }

        return instance;
    }

    public static void createInstance(Context context)
    {
        instance = new NetworkManager(context);
    }

    // todo for now
    public Context getContext()
    {
        return context;
    }

    private Context context;
}
