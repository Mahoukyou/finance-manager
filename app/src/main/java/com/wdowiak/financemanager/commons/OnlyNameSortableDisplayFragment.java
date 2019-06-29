package com.wdowiak.financemanager.commons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.IItem;
import com.wdowiak.financemanager.transaction_sorting.TransactionSortSettings;

import org.apache.commons.lang3.ObjectUtils;

abstract public class OnlyNameSortableDisplayFragment<T extends IItem, ItemAdapter extends ArrayAdapter>
        extends CommonDisplayFragment<T, ItemAdapter>
{
       @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.sort_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.sort_menu_popup:
                showSortingPopupMenu();
                break;
        }

        return true;
    }

    private void showSortingPopupMenu()
    {
        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), getActivity().findViewById(R.id.sort_menu_popup));
        popup.getMenuInflater().inflate(R.menu.sort_menu_popup, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch(item.getItemId())
                {

                    case R.id.sort_ascending:
                        getViewModel().getSortSettings().setSortType(CommonSortSettings.ESortType.Ascending);
                        break;

                    case R.id.sort_descending:
                        getViewModel().getSortSettings().setSortType(CommonSortSettings.ESortType.Descending);
                        break;
                }

                sortItems();
                createOrUpdateAdapter();

                return true;
            }
        });

        popup.show();
    }

    @Override
    protected void sortItems()
    {
        getViewModel().getData().sort((lhs, rhs) -> modifySortResultOnSortType(
                ObjectUtils.compare(lhs.getName(), rhs.getName())));
    }
}
