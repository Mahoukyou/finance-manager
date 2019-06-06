package com.wdowiak.financemanager;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDatabase extends SQLiteOpenHelper implements IDatabaseInterface
{
    public LocalDatabase(Context context)
    {
        super(context, "FinanceManager", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = "CREATE TABLE transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "source_account_id INTEGER, " + // todo, foreign key
                "target_account_id INTEGER, " + // todo, foreign key
                "amount INTEGER NOT NULL, " +
                "description TEXT NOT NULL, " +
                "entry_date INTEGER NOT NULL, " +
                "status_id INTEGER NOT NULL, " + // todo, foreign key
                "category_id INTEGER NOT NULL " + // todo, foreign key
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transactions");

        onCreate(db);
    }

    public void createSampleData()
    {
        for(int i = 0; i < 10; ++i)
        {
            Transaction transaction = new Transaction();
            transaction.sourceAccount = i;
            transaction.targetAccount = i;
            transaction.amount = i * 250;
            transaction.description = "Random desc";

            newTransaction(transaction);
        }
    }

    @Override
    public ArrayList<Transaction> getTransactions()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Transaction> transactions = new ArrayList<>();

        Cursor cursor = db.query("transactions", null, null, null, null, null, null);
        while(cursor.moveToNext())
        {
            transactions.add(getTransactionFromCursor(cursor));
        }

        cursor.close();
        db.close();

        return transactions;

    }

    @Override
    public boolean newTransaction(Transaction transaction)
    {
        ContentValues values = new ContentValues();
        values.put("source_account_id", transaction.sourceAccount);
        values.put("target_account_id", transaction.targetAccount);
        values.put("amount", transaction.amount);
        values.put("description", transaction.description);
        values.put("entry_date", transaction.description);
        values.put("status_id", System.currentTimeMillis());
        values.put("category_id", 0);

        SQLiteDatabase db = getWritableDatabase();
        final long result = db.insertWithOnConflict("transactions", null, values, SQLiteDatabase.CONFLICT_ABORT);

        db.close();

        return result != -1;
    }

    private Transaction getTransactionFromCursor(@org.jetbrains.annotations.NotNull Cursor cursor)
    {
        Transaction transaction = new Transaction();
        //int id = cursor.getInt(cursor.getColumnIndex("id"));
        transaction.sourceAccount = cursor.getInt(cursor.getColumnIndex("source_account_id"));
        transaction.targetAccount = cursor.getInt(cursor.getColumnIndex("target_account_id"));
        transaction.amount = cursor.getInt(cursor.getColumnIndex("amount")) / 100.0f;
        transaction.description = cursor.getString(cursor.getColumnIndex("description"));

        return transaction;
    }

}
