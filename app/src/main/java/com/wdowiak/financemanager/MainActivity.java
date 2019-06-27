package com.wdowiak.financemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.wdowiak.financemanager.accounts.AccountsDisplayFragment;
import com.wdowiak.financemanager.categories.CategoriesDisplayFragment;
import com.wdowiak.financemanager.currencies.CurrenciesDisplayFragment;
import com.wdowiak.financemanager.dashboard.DashboardFragment;
import com.wdowiak.financemanager.data.LoggedInUser;
import com.wdowiak.financemanager.data.LoginRepository;
import com.wdowiak.financemanager.groups.GroupsDisplayFragment;
import com.wdowiak.financemanager.transaction_statuses.TransactionStatusesDisplayFragment;
import com.wdowiak.financemanager.transactions.TransactionDisplayFragment;
import com.wdowiak.financemanager.ui.login.LoginActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      /*  FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        setUserInfo(navigationView);

        displayFragment(TransactionDisplayFragment.newInstance());

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
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_dashboard:
                displayFragment(DashboardFragment.newInstance());
                break;

            case R.id.nav_accounts:
                displayFragment(AccountsDisplayFragment.newInstance());
                break;

            case R.id.nav_transactions:
                displayFragment(TransactionDisplayFragment.newInstance());
                break;

            case R.id.nav_groups:
                displayFragment(GroupsDisplayFragment.newInstance());
                break;

            case R.id.nav_categories:
                displayFragment(CategoriesDisplayFragment.newInstance());
                break;

            case R.id.nav_currencies:
                displayFragment(CurrenciesDisplayFragment.newInstance());
                break;
                
            case R.id.nav_statuses:
                displayFragment(TransactionStatusesDisplayFragment.newInstance());
                break;


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

    final void displayFragment(final Fragment instance)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, instance).commitNow();
    }
}
