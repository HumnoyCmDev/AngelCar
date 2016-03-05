package com.beta.cls.angelcar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.gao.MessageGao;
import com.hndev.library.view.AngelCarMessage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 22/1/59.
 */
public class MultipleListViewChatAdapter extends BaseAdapter {
    private List<MessageGao> messages;
    private String messageBy ;

    private static final String TAG = "MultipleListViewChatAdapter";

    public MultipleListViewChatAdapter(String messageBy) {
        this.messageBy = messageBy;
    }


    public void setMessages(List<MessageGao> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        if (messages == null) return 0;
        return messages.size();
    }

    @Override
    public MessageGao getItem(int position) {
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
        String by = getItem(position).getMessageBy();
        return byUser(messageBy,by);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageGao message = getItem(position);
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
    private View inflatelayoutTalk(View view, ViewGroup parent, MessageGao message) {
        TextLeftViewHolder holder;
        if (view != null) {
            holder = (TextLeftViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
            holder = new TextLeftViewHolder(view);
            view.setTag(holder);
        }
        //coding
        holder.angelCarMessage.setMessage(message.getMessageText());
        holder.angelCarMessage.setIconProfile(message.getUserProfileImage());


        return view;
    }

    private View inflatelayoutMyTalk(View view, ViewGroup parent, MessageGao message) {
        TextRightViewHolder holder;
        if (view != null) {
            holder = (TextRightViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
            holder = new TextRightViewHolder(view);
            view.setTag(holder);
        }
        //coding
        holder.angelCarMessage.setMessage(message.getMessageText());
        holder.angelCarMessage.setIconProfile(message.getUserProfileImage());
        return view;
    }

    // View Holder
    public class TextLeftViewHolder {
        @Bind(R.id.item_chat_left)
        AngelCarMessage angelCarMessage;
        public TextLeftViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public class TextRightViewHolder {
        @Bind(R.id.item_chat_right)
        AngelCarMessage angelCarMessage;
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
