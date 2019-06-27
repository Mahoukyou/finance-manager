package com.wdowiak.financemanager.dashboard;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.commons.IntentExtras;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.transactions_filter.TransactionFilter;
import com.wdowiak.financemanager.transactions_filter.TransactionsFilterActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.wdowiak.financemanager.commons.IntentExtras.GET_FILTER_REQUEST;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE;

public class DashboardFragment extends Fragment
{
    private DashboardFragmentViewModel viewModel;

    private BarChart chart;

    LinearLayout viewLayout, progressBarLayout;

    @NotNull
    @Contract(" -> new")
    public static DashboardFragment newInstance()
    {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        View view =  inflater.inflate(R.layout.dashboard_fragment, container, false);
        chart = view.findViewById(R.id.barchart);
        viewLayout = view.findViewById(R.id.view_layout);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);

        getActivity().setTitle("Dashboard");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(DashboardFragmentViewModel.class);

        showProgressBar(true);

        TransactionFilter defaultFilter = new TransactionFilter(
                null,
                null,
                null,
                null ,
                null,
                100.0,
                1000.0,
                null, // last month todo
                null);

        viewModel.setTransactionFilter(defaultFilter);
        queryWithFilter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_FILTER_REQUEST && resultCode == Activity.RESULT_OK)
        {
            if(data.hasExtra(INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE))
            {
                final TransactionFilter filter = data.getExtras().getParcelable(INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE);
                viewModel.setTransactionFilter(filter);
                queryWithFilter();
            }
        }
    }

    protected void queryWithFilter()
    {
        //showProgressBar(true);
        String filterString = "";
        if(viewModel.getTransactionFilter() != null)
        {
            filterString = viewModel.getTransactionFilter().getFilterString();
        }

        QueryApi.getItemsFiltered(IItem.Type.Transaction, filterString, new Api.IQueryCallback<ArrayList<Transaction>>()
        {
            @Override
            public void onSuccess(ArrayList<Transaction> items)
            {
                // async request returned when activity was destroyed
                if(getActivity() == null || getActivity().getApplicationContext() == null)
                {
                    return;
                }

                populateGraphWithData(items);


                //showProgressBar(false);
            }

            @Override
            public void onError(Exception error)
            {
                if(getActivity() != null && getActivity().getApplicationContext() != null)
                {
                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_filter:
                Intent intent = new Intent(getActivity().getApplicationContext(), TransactionsFilterActivity.class);
                startActivityForResult(intent, IntentExtras.GET_FILTER_REQUEST);
                break;

            case R.id.menu_span:
                showSpanPopup();
        }

        return true;
    }

    private void showSpanPopup()
    {
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), getActivity().findViewById(R.id.menu_span));
        popup.getMenuInflater().inflate(R.menu.span_filter_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch(item.getItemId())
                {
                    case R.id.span_daily:
                        break;

                    case R.id.span_monthly:
                        break;

                    case R.id.span_yearly:
                        break;
                }

                return true;
            }
        });

        popup.show();
    }


    protected final void showProgressBar(final boolean show)
    {
        viewLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        progressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private CustomDate[] x_axis;
    private void populateGraphWithData(ArrayList<Transaction> transactions)
    {
        chart.clear();
        chart.setData(null);
        chart.invalidate();
        TimelyData timelyData = DashboardUtilities.createTimelyData(transactions, DataSpanSettings.EType.Monthly);


        x_axis = new CustomDate[timelyData.timelyDataHashMap.size()];
        ArrayList<BarEntry> entries = new ArrayList<>();

        int i = 0;
        for(SingleTimelyData singleTimelyData : timelyData.timelyDataHashMap.values())
        {
            x_axis[i] = singleTimelyData.date;
            entries.add(new BarEntry(i++, (float)singleTimelyData.amount));
        }

        BarDataSet ds = new BarDataSet(entries, String.valueOf("TEST"));
        ds.setColors(ColorTemplate.VORDIPLOM_COLORS);
       // sets.add(ds);
        XAxis axis = chart.getXAxis();
        axis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                try
                {
                    return x_axis[(int)value].getMonthYear();
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }

                return "";
            }
        });
        axis.setGranularity(1);

        BarData d = new BarData(ds);
        chart.setData(d);

        chart.invalidate();

        showProgressBar(false);
    }
}
