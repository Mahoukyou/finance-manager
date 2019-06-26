package com.wdowiak.financemanager.data;

import com.wdowiak.financemanager.commons.Helpers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public final class Transaction implements IItem
{
    public Transaction(
            double amount,
            @NotNull String description,
            @Nullable Account sourceAccount,
            @Nullable Account targetAccount,
            @NotNull Category category,
            @NotNull TransactionStatus status,
            @NotNull Date date)
    {
        this.amount = amount;
        this.description = description;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.category = category;
        this.transactionStatus = status;
        this.date = date;
    }

    public Transaction(
            long id,
            double amount,
            @NotNull String description,
            @Nullable Account sourceAccount,
            @Nullable Account targetAccount,
            @NotNull Category category,
            @NotNull TransactionStatus status,
            @NotNull Date date)
    {
        this(amount, description, sourceAccount, targetAccount, category, status, date);
        this.id = id;
    }

    private Transaction() {}


    public static final Transaction createFromJSONObject(final JSONObject jsonObject) throws JSONException
    {
        final Transaction transaction = new Transaction();

        // todo redo obviously, just for test if it works
        transaction.id = jsonObject.getLong("TransactionId");
        transaction.amount = jsonObject.getDouble("Amount");
        transaction.description = jsonObject.getString("Description");

        if(!jsonObject.isNull("SourceAccount"))
        {
            transaction.sourceAccount = Account.createFromJSONObject(jsonObject.getJSONObject("SourceAccount"));
        }

        if(!jsonObject.isNull("TargetAccount"))
        {
            transaction.targetAccount = Account.createFromJSONObject(jsonObject.getJSONObject("TargetAccount"));
        }

        if(!jsonObject.isNull("Category"))
        {
            transaction.category = Category.createFromJSONObject(jsonObject.getJSONObject("Category"));
        }

        if(!jsonObject.isNull("TransactionStatus"))
        {
            transaction.transactionStatus = TransactionStatus.createFromJSONObject(jsonObject.getJSONObject("TransactionStatus"));
        }

        final String stringDate = jsonObject.getString("EntryDate");
        try
        {
            transaction.date = Helpers.getSimpleDateFormatToParse().parse(stringDate);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }

        return transaction;
    }

    @NotNull
    @Contract(" -> new")
    @Override
    public final JSONObject createJSONObject()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("amount", String.valueOf(getAmount()));
            jsonObject.put("description", getDescription());

            if(getSourceAccount() != null)
            {
                final JSONObject sourceAccountJson = new JSONObject();
                sourceAccountJson.put("accountId", getSourceAccount().getId());
                jsonObject.put("sourceAccount", sourceAccountJson);
            }
            else
            {
                jsonObject.put("sourceAccount", JSONObject.NULL);
            }


            if(getTargetAccount() != null)
            {
                final JSONObject targetAccountJson = new JSONObject();
                targetAccountJson.put("accountId", getTargetAccount().getId());
                jsonObject.put("targetAccount", targetAccountJson);
            }
            else
            {
                jsonObject.put("targetAccount", JSONObject.NULL);
            }

            final JSONObject categoryJson = new JSONObject();
            categoryJson.put("categoryId", getCategory().getId());
            jsonObject.put("category", categoryJson);

            final JSONObject statusJson = new JSONObject();
            statusJson.put("transactionStatusId", getStatus().getId());
            jsonObject.put("transactionStatus", statusJson);

            jsonObject.put("EntryDate", Helpers.getSimpleDateFormatToFormat().format(date));
        }
        catch (JSONException ex)
        {
            /// .... gotta catch the json or it wont compile...
            throw new RuntimeException(ex);
        }

        return jsonObject;
    }

    @Contract(pure = true)
    @Override
    public final long getId()
    {
        return id;
    }

    @Nullable
    @Contract(pure = true)
    public final String getName()
    {
        throw new RuntimeException("Unused interface method");
    }

    @Contract(pure = true)
    public final double getAmount()
    {
        return amount;
    }

    @Contract(pure = true)
    public final String getDescription()
    {
        return description;
    }

    @Contract(pure = true)
    public final Account getSourceAccount()
    {
        return sourceAccount;
    }

    @Contract(pure = true)
    public final Account getTargetAccount()
    {
        return targetAccount;
    }

    @Contract(pure = true)
    public final Category getCategory()
    {
        return category;
    }

    @Contract(pure = true)
    public final TransactionStatus getStatus()
    {
        return transactionStatus;
    }

    @Contract(pure = true)
    public final Date getDate()
    {
        return date;
    }

    long id;
    double amount;
    String description;

    Account sourceAccount;
    Account targetAccount;
    Category category;
    TransactionStatus transactionStatus;

    Date date;
}