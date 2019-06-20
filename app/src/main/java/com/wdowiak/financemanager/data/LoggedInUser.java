package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.Contract;

public class LoggedInUser
{
    public LoggedInUser(long id, String login, String firstName, String lastName, String email)
    {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Contract(pure = true)
    public final long getId()
    {
        return id;
    }

    @Contract(pure = true)
    public final String getLogin()
    {
        return login;
    }

    @Contract(pure = true)
    public final String getFirstName()
    {
        return firstName;
    }

    @Contract(pure = true)
    public final String getLastName()
    {
        return lastName;
    }

    @Contract(pure = true)
    public final String getEmail()
    {
        return email;
    }

    private long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    // todo avatar
}
