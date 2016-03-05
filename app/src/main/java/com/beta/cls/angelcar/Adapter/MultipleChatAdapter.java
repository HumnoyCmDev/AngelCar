package com.beta.cls.angelcar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.gao.MessageGao;
import com.hndev.library.view.AngelCarMessage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 3/2/59. เวลา 10:56
 ***************************************/
public class MultipleChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MessageGao> messagesGao;
    private String messageBy ;

    public MultipleChatAdapter(List<MessageGao> messagesGao, String messageBy) {
        this.messagesGao = messagesGao;
        this.messageBy = messageBy;
    }

    public void setMessagesGao(List<MessageGao> messagesGao) {
        this.messagesGao = messagesGao;
    }

    @Override
    public int getItemViewType(int position) {
        String messageByGao = messagesGao.get(position).getMessageBy();
        return byUser(messageBy,messageByGao);
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
        MessageGao blogMessage = messagesGao.get(position);

        switch (holder.getItemViewType()){
            case 0 :
                ViewHolderLeft holderLeft = (ViewHolderLeft) holder;
                holderLeft.angelCarMessage.setMessage(blogMessage.getMessageText());
                holderLeft.angelCarMessage.setIconProfile(blogMessage.getUserProfileImage());
                break;
            case 1:
                ViewHolderRight holderRight = (ViewHolderRight) holder;
                holderRight.angelCarMessage.setMessage(blogMessage.getMessageText());
                holderRight.angelCarMessage.setIconProfile(blogMessage.getUserProfileImage());
                break;
        }

        Document doc = Jsoup.parse(blogMessage.getMessageText());
        Elements e = doc.select("img");
        if (!e.isEmpty()) {
            Log.i("log jsoup", "onBindViewHolder: " + e.size());
            Log.i("log jsoup", "onBindViewHolder: " + e.select("img").text());
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
        return messagesGao.size();
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
