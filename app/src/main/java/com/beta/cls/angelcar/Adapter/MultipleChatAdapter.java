package com.beta.cls.angelcar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.util.BlogMessage;
import com.hndev.library.view.AngelCarMessage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 3/2/59.
 */
public class MultipleChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<BlogMessage> messages;
    private String messageBy ;
    private Context context;


    public MultipleChatAdapter(Context context,List<BlogMessage> messages, String messageBy) {
        this.context = context;
        this.messages = messages;
        this.messageBy = messageBy;
    }

    @Override
    public int getItemViewType(int position) {
        String by = messages.get(position).getMessageby();
        return byUser(messageBy,by);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 0:
                View viewLeft = inflater.inflate(R.layout.item_chat_left,parent,false);
                return new ViewHolderLeft(viewLeft);
            default:
                View viewRight = inflater.inflate(R.layout.item_chat_right,parent,false);
                return new ViewHolderRight(viewRight);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // get item
        BlogMessage blogMessage = messages.get(position);

        switch (holder.getItemViewType()){
            case 0 :
                ViewHolderLeft holderLeft = (ViewHolderLeft) holder;
                holderLeft.angelCarMessage.setMessage(blogMessage.getMessagetext());
                holderLeft.angelCarMessage.setIconProfile(blogMessage.getUserprofileimage());
                break;
            case 1:
                ViewHolderRight holderRight = (ViewHolderRight) holder;
                holderRight.angelCarMessage.setMessage(blogMessage.getMessagetext());
                holderRight.angelCarMessage.setIconProfile(blogMessage.getUserprofileimage());
                break;
        }

    }

    public class ViewHolderLeft extends RecyclerView.ViewHolder {
        @Bind(R.id.item_chat_left)
        AngelCarMessage angelCarMessage;
        public ViewHolderLeft(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolderRight extends RecyclerView.ViewHolder {
        @Bind(R.id.item_chat_right)
        AngelCarMessage angelCarMessage;
        public ViewHolderRight(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
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

}
