package com.wdowiak.financemanager.attachments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wdowiak.financemanager.R;

import java.io.IOException;
import java.util.ArrayList;

import static com.wdowiak.financemanager.commons.IntentExtras.ADD_ATTACHMENT_FROM_CAMERA_REQUEST;
import static com.wdowiak.financemanager.commons.IntentExtras.ADD_ATTACHMENT_FROM_STORAGE_REQUEST;
import static com.wdowiak.financemanager.commons.IntentExtras.INTENT_EXTRA_ITEM_ID;

public class AttachmentsViewActivity extends AppCompatActivity
{
    private static final int PERMISSION_REQUEST_ADD_ATTACHMENT_FROM_STORAGE = 1;
    private static final int PERMISSION_REQUEST_ADD_ATTACHMENT_FROM_CAMERA = 2;

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
                final ImageAttachment attachment = new ImageAttachment(-1, viewModel.getTransactionId(), bitmap);

                new AsyncAddImageAttachment().execute(attachment);
            }
            catch (IOException error)
            {
                Toast.makeText(this, "Could not add the attachment: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(requestCode == ADD_ATTACHMENT_FROM_CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageAttachment attachment = new ImageAttachment(-1, viewModel.getTransactionId(), bitmap);

            new AsyncAddImageAttachment().execute(attachment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.attachments_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add_from_camera:
                addImageFromCameraIntent();
                break;

            case R.id.add_from_storage:
                addImageFromStorageIntent();
                break;

            case R.id.remove_all_attachments:
                removeAllAttachments();
                break;
        }

        return true;
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

    class AsyncDeleteImageAttachment extends AsyncTask<Integer, Void, Void>
    {
        @Override
        protected Void doInBackground(final Integer... params)
        {
            viewModel.getDb().deleteAttachmentById(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            queryAttachments();
        }
    }

    class AsyncDeleteAllAttachments extends AsyncTask<Long, Void, Void>
    {
        @Override
        protected Void doInBackground(final Long... params)
        {
            viewModel.getDb().deleteAllAttachments(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            queryAttachments();
        }
    }

    protected void addImageFromStorageIntent()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_ADD_ATTACHMENT_FROM_STORAGE);
        }
        else
        {
            showMediaPicker();
        }
    }

    protected void addImageFromCameraIntent()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_ADD_ATTACHMENT_FROM_CAMERA);
        }
        else
        {
            showCameraPicker();
        }
    }

    private void showMediaPicker()
    {
        showProgressBar(true);

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ADD_ATTACHMENT_FROM_STORAGE_REQUEST);
    }

    private void showCameraPicker()
    {
        showProgressBar(true);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, ADD_ATTACHMENT_FROM_CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[])
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_ADD_ATTACHMENT_FROM_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    showMediaPicker();
                }
                else
                {
                    Toast.makeText(this, "Cannot add an attachment from storage without permission", Toast.LENGTH_LONG).show();
                }
                break;

            case PERMISSION_REQUEST_ADD_ATTACHMENT_FROM_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    showCameraPicker();
                }
                else
                {
                    Toast.makeText(this, "Cannot add an attachment from camera without permission", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void onDeleteAttachment(final View view)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        final int attachmentId = (int)view.getTag();

                        showProgressBar(true);
                        new AsyncDeleteImageAttachment().execute(attachmentId);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete all attachments?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    protected void removeAllAttachments()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        showProgressBar(true);
                        new AsyncDeleteAllAttachments().execute(viewModel.getTransactionId());

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this attachment?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}