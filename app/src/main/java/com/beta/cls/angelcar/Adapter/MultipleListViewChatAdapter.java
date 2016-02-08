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
import com.hndev.library.view.UIMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 22/1/59.
 */
public class MultipleListViewChatAdapter extends BaseAdapter {
    private Context context;
    private List<BlogMessage> messages;
    private String messageBy ;

    public MultipleListViewChatAdapter(Context context, List<BlogMessage> messages,String messageBy) {
        this.context = context;
        this.messages = messages;
        this.messageBy = messageBy;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        String by = getItem(position).getMessageby();
        return byUser(messageBy,by);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BlogMessage message = getItem(position);
        int msBy = getItemViewType(position);
        switch (msBy){
            case 0 : convertView = inflatelayoutMyTalk(convertView,parent,message);
                break;
            case 1 : convertView = inflatelayoutTalk(convertView,parent,message);
                break;
        }

        return convertView;
    }

    //inflate layout
    private View inflatelayoutTalk(View view, ViewGroup parent, BlogMessage message) {
        TextLeftViewHolder holder;
        if (view != null) {
            holder = (TextLeftViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.list_view_item_chat_left, parent, false);
            holder = new TextLeftViewHolder(view);
            view.setTag(holder);
        }
        //coding
        holder.textViewChat.setText(message.getMessagetext());
        Picasso.with(context)
                .load(message.getUserprofileimage())
                .error(R.drawable.ic_hndeveloper)
                .into(holder.ic);
        return view;
    }

    private View inflatelayoutMyTalk(View view, ViewGroup parent, BlogMessage message) {
        TextRightViewHolder holder;
        if (view != null) {
            holder = (TextRightViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.list_view_item_chat_right, parent, false);
            holder = new TextRightViewHolder(view);
            view.setTag(holder);
        }
        //coding
        holder.textViewChat.setText(message.getMessagetext());
        Picasso.with(context)
                .load(message.getUserprofileimage())
                .error(R.drawable.ic_hndeveloper)
                .into(holder.ic);
        return view;
    }

    // View Holder
    public class TextLeftViewHolder {
        @Bind(R.id.list_view_item_chat_left_text)
        UIMessage textViewChat;

        @Bind(R.id.list_view_item_chat_left_image)
        CircularImageView ic;

        public TextLeftViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    public class TextRightViewHolder {
        @Bind(R.id.list_view_item_chat_right_text)
        UIMessage textViewChat;

        @Bind(R.id.list_view_item_chat_right_image)
        CircularImageView ic;

        public TextRightViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    private int byUser(String messageBy, String by) {
        if (messageBy.equals("shop")) {
            if (by.equals("shop")) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (by.equals("user")) {
                return 0;
            } else {
                return 1;
            }
        }
    }

//    private int byUser(String messageBy, String by) {
//        if (messageBy.equals("shop")) {
//            return by.equals("shop") ? 0 : 1;
//        } else {
//            return by.equals("shop") ? 0 : 1;
//        }
//    }

}
