package com.wdowiak.financemanager.attachments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wdowiak.financemanager.R;

import java.io.IOException;
import java.util.ArrayList;

import static com.wdowiak.financemanager.commons.IntentExtras.ADD_ATTACHMENT_FROM_CAMERA_REQUEST;
import static com.wdowiak.financemanager.commons.IntentExtras.ADD_ATTACHMENT_FROM_STORAGE_REQUEST;
import static com.wdowiak.financemanager.commons.IntentExtras.ADD_ITEM_REQUEST;
import static com.wdowiak.financemanager.commons.IntentExtras.EDIT_ITEM_REQUEST;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_ITEM_ID;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_CREATED;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_DELETED;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_RESULT_ITEM_WAS_UPDATED;

public class AttachmentsViewActivity extends AppCompatActivity
{
    AttachmentViewModel viewModel;

    GridView attachments;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachments_view);

        attachments = findViewById(R.id.attachments);

        viewModel = ViewModelProviders.of(this).get(AttachmentViewModel.class);
        viewModel.setDb(new AttachmentsDB(this));

        if(!getIntent().hasExtra(INTENT_EXTRA_ITEM_ID))
        {
            Toast.makeText(this, "empty intent", Toast.LENGTH_SHORT);
            finish();
        }

        viewModel.setTransactionId(getIntent().getExtras().getLong(INTENT_EXTRA_ITEM_ID));

        queryAttachments();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_ATTACHMENT_FROM_STORAGE_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Uri selectedImage = data.getData();

            try
            {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                final ImageAttachment attachment = new ImageAttachment(-1, getTaskId(), bitmap);


            }
            catch (IOException error)
            {
                Toast.makeText(this, "Could not add the attachment: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(requestCode == ADD_ATTACHMENT_FROM_CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {

        }
    }

    private void queryAttachments()
    {
        showProgressBar(true);
        new AsyncAttachmentsQuery().execute();
    }

    private void displayAttachments()
    {
        AttachmentsAdapter adapter = viewModel.getAttachmentsAdapter();
        if(adapter == null)
        {
            adapter = new AttachmentsAdapter(viewModel.getAttachmentsData(), this);
            attachments.setAdapter(adapter);
            viewModel.setAttachmentsAdapter(adapter);
        }
        else
        {
            adapter.clear();
            adapter.addAll(viewModel.getAttachmentsData());
            adapter.notifyDataSetChanged();
        }

        showProgressBar(false);
    }

    protected final void showProgressBar(final boolean show)
    {
        attachments.setVisibility(show ? View.GONE : View.VISIBLE);

        LinearLayout progressBarLayout = findViewById(R.id.progress_bar_layout);
        progressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    class AsyncAttachmentsQuery extends AsyncTask<Void, Void, ArrayList<ImageAttachment>>
    {
        @Override
        protected ArrayList<ImageAttachment> doInBackground(final Void... params)
        {
            return viewModel.getDb().getAttachments(viewModel.getTransactionId());
        }

        @Override
        protected void onPostExecute(final ArrayList<ImageAttachment> result)
        {
            viewModel.setAttachmentsData(result);
            displayAttachments();
        }
    }

    class AsyncAddImageAttachment extends AsyncTask<ImageAttachment, Void, Void>
    {
        @Override
        protected Void doInBackground(final ImageAttachment... params)
        {
            viewModel.getDb().addAttachment(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            queryAttachments();
        }
    }

    public void onAddImageFromStorageIntent(final View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ADD_ATTACHMENT_FROM_STORAGE_REQUEST);
    }

    private void onAddImageFromCameraIntent()
    {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, ADD_ATTACHMENT_FROM_CAMERA_REQUEST);
    }
}
