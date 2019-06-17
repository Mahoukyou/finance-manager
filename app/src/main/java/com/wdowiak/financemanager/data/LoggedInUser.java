package com.wdowiak.financemanager.data;

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

    public long getId()
    {
        return id;
    }

    public String getLogin()
    {
        return login;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
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
