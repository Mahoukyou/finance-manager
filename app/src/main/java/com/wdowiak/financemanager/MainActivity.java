package com.wdowiak.financemanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.wdowiak.financemanager.api.TransactionsApi;
import com.wdowiak.financemanager.data.LoggedInUser;
import com.wdowiak.financemanager.data.LoginRepository;
import com.wdowiak.financemanager.transactions.TransactionDetailActivity;
import com.wdowiak.financemanager.transactions.TransactionsAdapter;
import com.wdowiak.financemanager.ui.login.LoginActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    ListView transactionsListView;
    ArrayList<Transaction> transactionsData;
    TransactionsAdapter transactionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        transactionsListView = findViewById(R.id.transactions_listview);
        transactionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), TransactionDetailActivity.class);
                intent.putExtra(TransactionDetailActivity.INTENT_EXTRA_TRANSACTION_ID, transactionsData.get(i).getId());
                startActivity(intent);
            }
        });

        setUserInfo(navigationView);
        queryAndDisplayTransactions();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_logout:
                logoutAndClose("Logged out");
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void setUserInfo(final NavigationView navigationView)
    {
        final LoginRepository loginRepository = LoginRepository.getInstance();
        if(!loginRepository.isLoggedIn())
        {
            logoutAndClose("You are not logged in");
        }

        final LoggedInUser user = loginRepository.getLoggedInUser();

        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.nav_header_user_name);
        textView.setText(user.getFirstName() + " " + user.getLastName());

        textView = navigationView.getHeaderView(0).findViewById(R.id.nav_header_user_email);
        textView.setText(user.getEmail());
    }

    void logoutAndClose(final String message)
    {
        LoginRepository.getInstance().logout();

        final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        finish();
    }

    void queryAndDisplayTransactions()
    {
        TransactionsApi.getTransactions(new TransactionsApi.ITransactionCallback<ArrayList<Transaction>>()
        {
            @Override
            public void OnSuccess(ArrayList<Transaction> transactions)
            {
                transactionsData = transactions;
                if(transactionsAdapter == null)
                {
                    transactionsAdapter = new TransactionsAdapter(transactionsData, getApplicationContext());
                    transactionsListView.setAdapter(transactionsAdapter);
                }
                else
                {
                    transactionsAdapter.notifyDataSetChanged();
                }

                Toast.makeText(MainActivity.this, transactions.get(0).toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnError(Exception error)
            {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    final void onTransactionItemClicked(final View view)
    {

    }
}
