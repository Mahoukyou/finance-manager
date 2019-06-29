package com.wdowiak.financemanager.dashboard;

import org.jetbrains.annotations.Contract;

import java.util.Date;
import java.util.Calendar;
import java.util.Objects;

public class CustomDate implements Comparable<CustomDate>
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
                return getDayMonthYearString();
            case Monthly:
                return getMonthYearString();
            case Yearly:
                return getYearString();
            default:
                throw new RuntimeException("Span type not implemented");
        }
    }

    public String getDayMonthYearString()
    {
        return getDay() + "/" + getMonthYearString();
    }

    public String getMonthYearString()
    {
        return (getMonth()+1) + "/" + getYearString();
    }

    public String getYearString()
    {
        return String.valueOf(getYear());
    }

    public int getYear()
    {
        return year;
    }

    public int getMonth()
    {
        return month;
    }

    public int getDay()
    {
        return day;
    }

    public DataSpanSettings.EType getSpanType()
    {
        return spanType;
    }

    @Override
    public int compareTo(CustomDate customDate)
    {
        if(getYear() != customDate.getYear())
        {
            return getYear() < customDate.getYear() ? -1 : 1;
        }

        if(getMonth() != customDate.getMonth())
        {
            return getMonth() < customDate.getMonth() ? -1 : 1;
        }

        if(getDay() != customDate.getDay())
        {
            return getDay() < customDate.getDay() ? -1 : 1;
        }

        return 0;
    }

    private int year, month = 0, day = 1;
    private DataSpanSettings.EType spanType;
}
