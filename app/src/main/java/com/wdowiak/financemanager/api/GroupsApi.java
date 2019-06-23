package com.wdowiak.financemanager.api;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.wdowiak.financemanager.data.Group;
import com.wdowiak.financemanager.data.NewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupsApi
{
    public static void getGroups(final Api.IQueryCallback<ArrayList<Group>> callback)
    {
        Api.asyncQuery(Request.Method.GET, getGroupsUrl, new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    final JSONArray groupsJSONArray = response.getJSONArray("Items");

                    ArrayList<Group> groups = new ArrayList<>();
                    for(int i = 0; i < groupsJSONArray.length(); ++i)
                    {
                        groups.add(Group.createFromJSONObject(groupsJSONArray.getJSONObject(i)));
                    }

                    callback.onSuccess(groups);
                }
                catch (JSONException error)
                {
                    callback.onError(error);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error);
            }
        });
    }

    public static void getGroupById(final long groupId, final Api.IQueryCallback<Group> callback)
    {
        // hack or the right way?
        final String queryGroupById = getGroupsUrl + groupId;

        Api.asyncQuery(Request.Method.GET, queryGroupById, new JSONObject(), new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    // No group found for this id
                    if(response == null)
                    {
                        callback.onSuccess(null);
                        return;
                    }

                    callback.onSuccess(Group.createFromJSONObject(response));
                }
                catch (JSONException error)
                {
                    callback.onError(error);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error);
            }
        });
    }

    public static void createGroup(final NewGroup group, final Api.IQueryCallback<Group> callback)
    {
        JSONObject params = group.createJSONObject();
        if(params == null)
        {
            callback.onError(new Exception("Invalid group"));
        }

        Api.asyncQuery(Request.Method.POST, getGroupsUrl, params, new Api.IApiCallback()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    if(response == null)
                    {
                        callback.onSuccess(null);
                        return;
                    }

                    callback.onSuccess(Group.createFromJSONObject(response));
                }
                catch (JSONException error)
                {
                    callback.onError(error);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error);
            }
        });
    }

    public static void deleteGroupById(final long groupId, final Api.IQueryCallback<String> callback)
    {
        // hack or the right way?
        final String deleteGroupById = getGroupsUrl + groupId;

        Api.asyncStringQuery(Request.Method.DELETE, deleteGroupById, new Api.IApiStringCallback()
        {
            @Override
            public void onResponse(String response)
            {
                callback.onSuccess(response);
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                callback.onError(error);
            }
        });
    }

    final static String getGroupsUrl = EndpointUrl.url + "v1/groups/";
}
