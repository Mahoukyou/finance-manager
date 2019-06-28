package com.wdowiak.financemanager.attachments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.wdowiak.financemanager.R;

import java.util.ArrayList;

public class AttachmentsAdapter extends ArrayAdapter
{
    private ArrayList<ImageAttachment> data;
    Context context;

    private static class ViewHolder
    {
        ImageView imageView;
        ImageButton deleteButton;
    }

    public AttachmentsAdapter(ArrayList<ImageAttachment> data, Context context)
    {
        super(context, R.layout.attachment_item, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public ImageAttachment getItem(int position)
    {
        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        View result;

        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_item, parent, false);

            viewHolder.imageView = convertView.findViewById(R.id.attachment_imageView);
            viewHolder.deleteButton = convertView.findViewById(R.id.attachment_deleteButton);

            convertView.setTag(viewHolder);
            result = convertView;
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        final ImageAttachment attachment = getItem(position);
        viewHolder.imageView.setImageBitmap(attachment.getImageAsBitmap());

        viewHolder.deleteButton.setTag(attachment.getId());
        return result;
    }
}
