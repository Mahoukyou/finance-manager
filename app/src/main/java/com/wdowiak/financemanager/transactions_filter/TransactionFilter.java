package com.wdowiak.financemanager.transactions_filter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TransactionFilter implements Parcelable
{
    @Nullable
    Long sourceAccountId;

    @Nullable
    Long targetAccountId;

    @Nullable
    Long categoryId;

    @Nullable
    Long statusId;

    @Nullable
    String description;

    @Nullable
    Double minAmount;

    @Nullable
    Double maxAmount;

    public TransactionFilter(
            @Nullable Long sourceAccountId,
            @Nullable Long targetAccountId,
            @Nullable Long categoryId,
            @Nullable Long statusId,
            @Nullable String description,
            @Nullable Double minAmount,
            @Nullable Double maxAmount)
    {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.categoryId = categoryId;
        this.statusId = statusId;
        this.description = description;
        this.minAmount = minAmount;
        this.minAmount = maxAmount;
    }

    @Nullable
    Long getSourceAccountId()
    {
        return sourceAccountId;
    }

    @Nullable
    Long getTargetAccountId()
    {
        return targetAccountId;
    }

    @Nullable
    Long getCategoryId()
    {
        return categoryId;
    }

    @Nullable
    Long getStatusId()
    {
        return statusId;
    }

    @Nullable
    String getDescription()
    {
        return description;
    }

    @Nullable
    Double getMinAmount()
    {
        return minAmount;
    }

    @Nullable
    Double getMaxAmount()
    {
        return maxAmount;
    }


    protected TransactionFilter(@NotNull Parcel in)
    {
        sourceAccountId = in.readByte() == 0x00 ? null : in.readLong();
        targetAccountId = in.readByte() == 0x00 ? null : in.readLong();
        categoryId = in.readByte() == 0x00 ? null : in.readLong();
        statusId = in.readByte() == 0x00 ? null : in.readLong();
        description = in.readString();
        minAmount = in.readByte() == 0x00 ? null : in.readDouble();
        maxAmount = in.readByte() == 0x00 ? null : in.readDouble();
    }

    public String getFilterString()
    {
        String queryString = parameterToString(true, "SourceAccountId", getSourceAccountId());
        queryString += parameterToString(queryString.isEmpty(), "TargetAccountId", getTargetAccountId());
        queryString += parameterToString(queryString.isEmpty(), "CategoryId", getCategoryId());
        queryString += parameterToString(queryString.isEmpty(), "StatusId", getStatusId());
        queryString += parameterToString(queryString.isEmpty(), "Description", getDescription());
        queryString += parameterToString(queryString.isEmpty(), "MinAmount", getMinAmount());
        queryString += parameterToString(queryString.isEmpty(), "MaxAmount", getMaxAmount());

        // single &
        if(queryString.length() == 1)
        {
            return "";
        }

        return queryString;
    }

    @Contract(pure = true)
    static final <T> String parameterToString(final boolean isEmpty, final String parameterName, final T value)
    {
        String result = "";

        if(value != null)
        {
            // Anything else beside string will either be null or have an actual value
            if(value instanceof String)
            {
                String strValue = (String)value;
                if(strValue.isEmpty())
                {
                    return "";
                }
            }

            if(!isEmpty)
            {
                result = "&";
            }

            result += parameterName + "=" + value;
        }

        return result;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        if (sourceAccountId == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeLong(sourceAccountId);
        }

        if (targetAccountId == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeLong(targetAccountId);
        }
        if (categoryId == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeLong(categoryId);
        }

        if (statusId == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeLong(statusId);
        }

        dest.writeString(description);

        if (minAmount == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(minAmount);
        }

        if (maxAmount == null)
        {
            dest.writeByte((byte) (0x00));
        }
        else
        {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(maxAmount);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TransactionFilter> CREATOR = new Parcelable.Creator<TransactionFilter>()
    {
        @NotNull
        @Contract("_ -> new")
        @Override
        public TransactionFilter createFromParcel(Parcel in)
        {
            return new TransactionFilter(in);
        }

        @NotNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public TransactionFilter[] newArray(int size)
        {
            return new TransactionFilter[size];
        }
    };
}