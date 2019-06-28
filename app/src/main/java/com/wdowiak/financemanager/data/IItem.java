package com.wdowiak.financemanager.data;

import org.json.JSONObject;

public interface IItem
{
    /* I don't think there is another easy way to call a static function without knowing the classes type
    * (template type deduction) so we have to pass an enum to factory*/
    enum Type
    {
        Account,
        Category,
        Currency,
        Group,
        Transaction,
        TransactionStatus,
        User
    }

    long getId();
    String getName();

    JSONObject createJSONObject();
}