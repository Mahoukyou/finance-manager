package com.wdowiak.financemanager.attachments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

public class ImageAttachment
{
    private int id;
    private long transactionId;

    @NotNull
    private Bitmap bitmap;

    ImageAttachment(final int id, final long transactionId, @NotNull final Bitmap bitmap)
    {
        this.id = id;
        this.transactionId = transactionId;
        this.bitmap = bitmap;
    }

    ImageAttachment(final int id, final long transactionId, final byte[] data)
    {
        this.id = id;
        this.transactionId = transactionId;
        this.bitmap = convertDataToBitmap(data);
    }

    public int getId()
    {
        return id;
    }

    public long getTransactionId()
    {
        return transactionId;
    }

    public byte[] getImageAsDataArray()
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Bitmap getImageAsBitmap()
    {
        return bitmap;
    }

    static private Bitmap convertDataToBitmap(byte[] data)
    {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
