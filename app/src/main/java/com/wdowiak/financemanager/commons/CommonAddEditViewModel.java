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

    public ItemType getQueriedItem()
    {
        return queriedItem;
    }

    public void setQueriedItem(ItemType queriedItem)
    {
        this.queriedItem = queriedItem;
    }

    public LiveData<Form> getFormState()
    {
        return formState;
    }

    protected void setFormState(Form formState)
    {
        this.formState.setValue(formState);
    }

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    abstract public boolean hasDataChanged();

 //   abstract void createFormState();
}
