package com.example.engy.filmasia.firabase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.engy.filmasia.R;

import java.util.List;

/**
 * Created by Engy on 2/28/2018.
 */

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    public ChatMessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChatMessage> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView=((Activity)getContext()).getLayoutInflater().inflate(R.layout.chat_item,parent,false);
        }
        ImageView img= convertView.findViewById(R.id.chat_item_img);
        TextView name= convertView.findViewById(R.id.chat_item_sender_name);
        TextView msgText= convertView.findViewById(R.id.chat_item_msg);

        ChatMessage chatMessage=getItem(position);
        if(chatMessage!=null){
            boolean isPhoto=chatMessage.getPhotoUrl() !=null;
            if(isPhoto){
                img.setVisibility(View.VISIBLE);
                msgText.setVisibility(View.GONE);
                Glide.with(img.getContext()).load(chatMessage.getPhotoUrl()).into(img);
            }
            else{
                img.setVisibility(View.GONE);
                msgText.setVisibility(View.VISIBLE);
                msgText.setText(chatMessage.getTextMsg());
            }
            name.setText(chatMessage.getSender());
        }


        return convertView;
    }
}
