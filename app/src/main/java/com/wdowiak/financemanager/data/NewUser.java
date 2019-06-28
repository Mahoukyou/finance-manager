package com.wdowiak.financemanager.data;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;

public class NewUser implements IItem
{
    @NotNull
    private String login;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    public NewUser(
            @NotNull String login,
            @NotNull String firstName,
            @NotNull String lastName,
            @NotNull String email,
            @NotNull String password)
    {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public long getId()
    {
        /*unused*/
        return -1;
    }

    @Override
    public String getName()
    {
        /*unused*/
        return null;
    }

    @Override
    public JSONObject createJSONObject()
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("email", email);
        params.put("avatarPath", "kok.jpg");

        return new JSONObject(params);
    }
}
