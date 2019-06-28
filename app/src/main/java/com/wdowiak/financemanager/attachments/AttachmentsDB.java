package com.wdowiak.financemanager.attachments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;

import java.util.ArrayList;

public class AttachmentsDB extends SQLiteOpenHelper
{
    public AttachmentsDB(Context context)
    {
        super(context, "Attachments", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                "CREATE TABLE attachmentImage (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "transaction_id INTEGER NOT NULL," +
                "data BLOB NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query = String.format("DROP TABLE IF EXISTS attachments");
        db.execSQL(query);

        onCreate(db);
    }

    public void addAttachment(ImageAttachment imageAttachment)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("transaction_id", imageAttachment.getTransactionId());
        values.put("data", imageAttachment.getImageAsDataArray());

        db.insertWithOnConflict("attachmentImage", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ImageAttachment getAttachmentById(int attachmentId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("attachmentImage", null, "id=?", new String[] {String.valueOf(attachmentId)}, null, null, null);
        if(cursor.moveToFirst())
        {
            long transactionId  = cursor.getLong(cursor.getColumnIndex("transaction_id"));
            byte[] data = cursor.getBlob(cursor.getColumnIndex("data"));


            db.close();
            return new ImageAttachment(attachmentId, transactionId, data);
        }

        db.close();
        return null;
    }

    public ArrayList<ImageAttachment> getAttachments(final long transaction_id)
    {
        ArrayList<ImageAttachment> imageAttachments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("attachmentImage", null, "transaction_id=?", new String[] {String.valueOf(transaction_id)}, null, null, null);
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            byte[] data = cursor.getBlob(cursor.getColumnIndex("data"));

            imageAttachments.add(new ImageAttachment(id, transaction_id, data));
        }

        db.close();
        return imageAttachments;
    }

    public void deleteAttachmentById(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("attachmentImage", "id=?", new String[] {String.valueOf(id)});
        db.close();
    }

    public void deleteAllAttachments(final long transaction_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("attachmentImage", "transaction_id=?", new String[] {String.valueOf(transaction_id)});
        db.close();
    }
}
