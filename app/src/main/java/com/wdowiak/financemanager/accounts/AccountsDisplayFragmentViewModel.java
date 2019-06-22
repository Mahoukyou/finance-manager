package com.wdowiak.financemanager.accounts;

import androidx.lifecycle.ViewModel;
import com.wdowiak.financemanager.data.Account;
import java.util.ArrayList;

public class AccountsDisplayFragmentViewModel extends ViewModel
{
    public ArrayList<Account> accountsData;
    public AccountsAdapter accountsAdapter;
}
