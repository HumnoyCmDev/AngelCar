package com.beta.cls.angelcar.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.dao.MessageDao;
import com.beta.cls.angelcar.listener.OnClickChatListener;
import com.hndev.library.view.AngelCarMessage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 22/1/59.
 */
public class MultipleListViewChatAdapter extends BaseAdapter {
    private List<MessageDao> messages;
    private String messageBy ;
//    private OnClickChatListener onClickChatListener;
//
//    public void setOnClickChatListener(OnClickChatListener onClickChatListener) {
//        this.onClickChatListener = onClickChatListener;
//    }

    public MultipleListViewChatAdapter(String messageBy) {
        this.messageBy = messageBy;
    }


    public void setMessages(List<MessageDao> messages) {
        this.messages = messages;
    }

    @Override
    public int getCount() {
        if (messages == null) return 0;
        return messages.size();
    }

    @Override
    public MessageDao getItem(int position) {
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
        String messageByGao = getItem(position).getMessageBy();
        return byUser(messageBy,messageByGao);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageDao message = getItem(position);
        int msBy = getItemViewType(position);
        switch (msBy){
            case 0 : convertView = inflateLayoutChatLeft(convertView,parent,message,position);
                break;
            case 1 : convertView = inflateLayoutChatRight(convertView,parent,message,position);
                break;
        }
        return convertView;
    }

    //inflate layout
    private View inflateLayoutChatRight(View view, ViewGroup parent, MessageDao message, int position) {
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
//        if (position == 0)
//            holder.angelCarMessage.setBackground(Color.parseColor("#50E3C2"));
//        else if (position == 1)
//            holder.angelCarMessage.setBackground(Color.parseColor("#7ED321"));

        return view;
    }

    private View inflateLayoutChatLeft(View view, ViewGroup parent, MessageDao message, int position) {
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
//        if (position == 0)
//            holder.angelCarMessage.setBackground(Color.parseColor("#50E3C2"));
//        else if (position == 1)
//            holder.angelCarMessage.setBackground(Color.parseColor("#7ED321"));

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

    private int byUser(String messageBy, String messageByGao) {
        /*0 = ซ้าย || 1 = ขวา*/
        if (messageBy.equals(messageByGao))
            return 1;
        else
            return 0;

        /*@ messageBy ได้มาจาก DetailActivity ส่งเข้ามา
        *ถ้า messageBy ตรงกับใน messageByGao เรียงแชทไว้ขวา*/
    }

}
