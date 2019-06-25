package com.wdowiak.financemanager.commons;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wdowiak.financemanager.data.IItem;

abstract public class CommonAddEditViewModel<ItemType extends IItem, Form extends CommonAddEditFormState>
    extends ViewModel
{
    private ItemType queriedItem = null;
    private MutableLiveData<Form> formState = new MutableLiveData<>();

    private Long itemId = null;

    ItemType getQueriedItem()
    {
        return queriedItem;
    }

    void setQueriedItem(ItemType queriedItem)
    {
        this.queriedItem = queriedItem;
    }

    LiveData<Form> getFormState()
    {
        return formState;
    }

    Long getItemId()
    {
        return getItemId();
    }

    void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    abstract boolean hasDataChanged();

 //   abstract void createFormState();
}
