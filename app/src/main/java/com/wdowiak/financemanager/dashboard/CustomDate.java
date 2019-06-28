package com.wdowiak.financemanager.dashboard;

import org.jetbrains.annotations.Contract;

import java.util.Date;
import java.util.Calendar;
import java.util.Objects;

public class CustomDate
{
    CustomDate(Date date, DataSpanSettings.EType spanType)
    {
        this.spanType = spanType;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        switch (spanType)
        {
            case Daily:
                day = calendar.get(Calendar.DAY_OF_MONTH);
                // FALLTHROUGH
            case Monthly:
                month = calendar.get(Calendar.MONTH);
                // FALLTHROUGH
            case Yearly:
                year = calendar.get(Calendar.YEAR);
                break;

            default:
                throw new RuntimeException("Span type not implemented");
        }


    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        CustomDate rhs = (CustomDate) o;
        return year == rhs.year &&
                month == rhs.month &&
                day == rhs.day;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString()
    {
        switch (spanType)
        {
            case Daily:
                return getDayMonthYear();
            case Monthly:
                return getMonthYear();
            case Yearly:
                return getYear();
            default:
                throw new RuntimeException("Span type not implemented");
        }
    }

    public String getDayMonthYear()
    {
        return day + "/" + (month+1) + "/" + year;
    }

    public String getMonthYear()
    {
        return month + "/" + year;
    }

    public String getYear()
    {
        return String.valueOf(year);
    }

    /* returns true if lhs < rhs */
    public boolean lessThan(CustomDate rhs)
    {
        if(year < rhs.year)
        {
            return true;
        }

        if(month < rhs.month)
        {
            return true;
        }

        if(day < rhs.day)
        {
            return true;
        }

        return false;
    }

    private int year, month = 0, day = 1;
    private DataSpanSettings.EType spanType;
}
