package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemFactory
{
    public static <T extends IItem> T createItem(@NotNull final IItem.Type itemType, @NotNull final JSONObject jsonObject) throws JSONException
    {
        switch (itemType)
        {
            case Account:
                return (T)Account.createFromJSONObject(jsonObject);

            case Category:
                return (T)Category.createFromJSONObject(jsonObject);

            case Currency:
                return (T)Currency.createFromJSONObject(jsonObject);

            case Group:
                return (T)Group.createFromJSONObject(jsonObject);

            case Transaction:
                return (T)Transaction.createFromJSONObject(jsonObject);

            case TransactionStatus:
                return (T)TransactionStatus.createFromJSONObject(jsonObject);

            case User:
                return null;

            default:
                throw new RuntimeException("Invalid item type");
        }
    }
}
