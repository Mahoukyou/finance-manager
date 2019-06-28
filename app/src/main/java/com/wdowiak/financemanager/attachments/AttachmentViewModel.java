package com.wdowiak.financemanager.attachments;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class AttachmentViewModel extends ViewModel
{
    private long transactionId;
    private AttachmentsAdapter attachmentsAdapter;
    private ArrayList<ImageAttachment> attachmentsData;
    private AttachmentsDB db;

    public long getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(final long transactionId)
    {
        this.transactionId = transactionId;
    }

    public AttachmentsAdapter getAttachmentsAdapter()
    {
        return attachmentsAdapter;
    }

    public void setAttachmentsAdapter(final AttachmentsAdapter attachmentsAdapter)
    {
        this.attachmentsAdapter = attachmentsAdapter;
    }

    public ArrayList<ImageAttachment> getAttachmentsData()
    {
        return attachmentsData;
    }

    public void setAttachmentsData(final ArrayList<ImageAttachment> attachmentsData)
    {
        this.attachmentsData = attachmentsData;
    }


    public AttachmentsDB getDb() {
        return db;
    }

    public void setDb(AttachmentsDB db) {
        this.db = db;
    }
}
