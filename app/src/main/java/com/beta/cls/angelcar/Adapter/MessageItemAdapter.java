package com.beta.cls.angelcar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.util.BlogMessage;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 26/1/59.
 */
public class MessageItemAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<BlogMessage> messages;

    public MessageItemAdapter(Context mContext,List<BlogMessage> message) {
        this.mContext = mContext;
        this.messages = message;
        Collections.sort(messages, new Comparator<BlogMessage>() {
            @Override
            public int compare(BlogMessage lhs, BlogMessage rhs) {
                return rhs.getMessagestamp().compareToIgnoreCase(lhs.getMessagestamp());
            }
        });
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public BlogMessage getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }else {
            convertView = mInflater.inflate(R.layout.chat_item_message_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        BlogMessage message = getItem(position);

        Picasso.with(mContext)
                .load(message.getUserprofileimage())
                .error(R.drawable.me)
                .into(holder.icon);

        holder.txtDisPlayName.setText(message.getDisplayname());
        holder.txtMessage.setText(message.getMessagetext());
        holder.txtTime.setText(subTime(message.getMessagestamp()));
        return convertView;
    }

    private String subTime(String time){
        String date = "2016-01-22";
        int start = date.length()+1;
        return time.substring(start,time.length()-3);
    }

    public class ViewHolder{
        @Bind(R.id.chat_item_message_dis_play_name)
        TextView txtDisPlayName;
        @Bind(R.id.chat_item_message_image)
        CircularImageView icon;
        @Bind(R.id.chat_item_message_message)
        TextView txtMessage;
        @Bind(R.id.chat_item_message_time)
        TextView txtTime;

        public ViewHolder(View v) {
            ButterKnife.bind(this,v);
        }
    }

}
