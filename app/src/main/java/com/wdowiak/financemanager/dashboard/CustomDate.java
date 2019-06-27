package com.wdowiak.financemanager.dashboard;

import org.jetbrains.annotations.Contract;

import java.util.Date;
import java.util.Calendar;
import java.util.Objects;

public class CustomDate
{
    CustomDate(Date date, DataSpanSettings.EType spanType)
    {
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

    public String getDayMonth()
    {
        return day + "/" + month;
    }

    public String getMonthYear()
    {
        return month + "/" + year;
    }

    public int year, month = 0, day = 1;
}
