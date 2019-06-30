package com.wdowiak.financemanager.dashboard;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.api.Api;
import com.wdowiak.financemanager.api.QueryApi;
import com.wdowiak.financemanager.commons.Helpers;
import com.wdowiak.financemanager.commons.IntentExtras;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.data.Transaction;
import com.wdowiak.financemanager.transactions_filter.TransactionFilter;
import com.wdowiak.financemanager.transactions_filter.TransactionsFilterActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

import static com.wdowiak.financemanager.commons.IntentExtras.GET_FILTER_REQUEST;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_TRANSACTION_FILTER_PARCELABLE;

public class DashboardFragment extends Fragment
{
    private DashboardFragmentViewModel viewModel;

    private ColumnChartView chart;

    LinearLayout viewLayout, progressBarLayout;
    TextView totalAmount;

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
        chart = view.findViewById(R.id.chart);
        viewLayout = view.findViewById(R.id.view_layout);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        totalAmount = view.findViewById(R.id.dashboard_total);

        getActivity().setTitle("Dashboard");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(DashboardFragmentViewModel.class);

        showProgressBar(true);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date beginDate = calendar.getTime();

        TransactionFilter defaultFilter = new TransactionFilter(
                null,
                null,
                null,
                null ,
                null,
                null,
                null,
                Helpers.getSimpleDateFormatToFormat().format(beginDate), // last month todo
                Helpers.getSimpleDateFormatToFormat().format(currentDate));

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
                        if(viewModel.getSpanType() != DataSpanSettings.EType.Daily)
                        {
                            viewModel.setSpanType(DataSpanSettings.EType.Daily);
                            queryWithFilter();
                        }
                        break;

                    case R.id.span_monthly:
                        if(viewModel.getSpanType() != DataSpanSettings.EType.Monthly)
                        {
                            viewModel.setSpanType(DataSpanSettings.EType.Monthly);
                            queryWithFilter();
                        }
                        break;

                    case R.id.span_yearly:
                        if(viewModel.getSpanType() != DataSpanSettings.EType.Yearly)
                        {
                            viewModel.setSpanType(DataSpanSettings.EType.Yearly);
                            queryWithFilter();
                        }
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
        TimelyData timelyData = DashboardUtilities.createTimelyData(transactions, viewModel.getSpanType());

        List<AxisValue> xAxisValues = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        int counter = 0;
        for(SingleTimelyData singleTimelyData : timelyData.sortedTimelyData)
        {
            List<SubcolumnValue> subColumn = new ArrayList<>();
            subColumn.add(new SubcolumnValue((float)singleTimelyData.amount, ChartUtils.pickColor()));

            xAxisValues.add(new AxisValue(counter++).setLabel(singleTimelyData.date.toString()));

            Column column = new Column(subColumn);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);

        Axis axisX = new Axis(xAxisValues);
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);
        axisX.setMaxLabelChars(10);
        data.setAxisXBottom(axisX);

        Axis axisY = new Axis().setHasLines(true);
        axisY.setMaxLabelChars(6);
        axisY.setTextColor(Color.BLACK);
        axisY.setName("Amount");
        data.setAxisYLeft(axisY);

        chart.setColumnChartData(data);
        chart.invalidate();


        totalAmount.setText(String.format("%.2f", timelyData.totalAmount));

        showProgressBar(false);
    }
}
