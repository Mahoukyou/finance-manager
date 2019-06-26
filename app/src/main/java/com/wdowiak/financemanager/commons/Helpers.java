package com.wdowiak.financemanager.commons;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class Helpers
{
    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat getSimpleDateFormatToFormat()
    {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }

    @NotNull
    @Contract(" -> new")
    public static SimpleDateFormat getSimpleDateFormatToParse()
    {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }
}
