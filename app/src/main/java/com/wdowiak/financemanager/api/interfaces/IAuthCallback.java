package com.wdowiak.financemanager.api.interfaces;

public interface IAuthCallback
{
    void onSuccessfullAuth(String authToken);
    void onUnsuccessfullAuth(String error);
}
