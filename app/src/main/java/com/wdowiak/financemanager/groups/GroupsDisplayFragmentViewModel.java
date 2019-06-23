package com.wdowiak.financemanager.groups;

import androidx.lifecycle.ViewModel;

import com.wdowiak.financemanager.data.Category;
import com.wdowiak.financemanager.data.Group;

import java.util.ArrayList;

public class GroupsDisplayFragmentViewModel extends ViewModel
{
    public ArrayList<Group> groupsData;
    public GroupsAdapter groupsAdapter;
}
