package com.wdowiak.financemanager.attachments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.commons.IntentExtras;

import java.util.ArrayList;

public class AttachmentDetailActivity extends Activity
{
    AttachmentsDB db;
    PhotoView photoView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attachment_detail_activity);

        photoView = findViewById(R.id.attachmentDetail_photoView);

        if(!getIntent().hasExtra(IntentExtras.INTENT_EXTRA_ITEM_ID))
        {
            Toast.makeText(this, "Invalid attachment id", Toast.LENGTH_SHORT);
            finish();
        }

        final int attachmentId = getIntent().getIntExtra(IntentExtras.INTENT_EXTRA_ITEM_ID, -1);

        db = new AttachmentsDB(this);
        queryAttachment(attachmentId);
    }


    private void queryAttachment(final int id)
    {
        showProgressBar(true);
        new AsyncAttachmentById().execute(id);
    }

    protected final void showProgressBar(final boolean show)
    {
        photoView.setVisibility(show ? View.GONE : View.VISIBLE);

        LinearLayout progressBarLayout = findViewById(R.id.progress_bar_layout);
        progressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    class AsyncAttachmentById extends AsyncTask<Integer, Void, ImageAttachment>
    {
        @Override
        protected ImageAttachment doInBackground(final Integer... params)
        {
            return db.getAttachmentById(params[0]);
        }

        @Override
        protected void onPostExecute(final ImageAttachment result)
        {
            photoView.setImageBitmap(result.getImageAsBitmap());
            showProgressBar(false);
        }
    }
}