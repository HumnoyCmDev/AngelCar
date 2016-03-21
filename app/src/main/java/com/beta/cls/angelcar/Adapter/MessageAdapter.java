package com.beta.cls.angelcar.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.gao.MessageGao;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 26/1/59.
 */
//// TODO: 20/2/59 New Adapter...
public class MessageAdapter extends BaseAdapter{

    private List<MessageGao> gaos;
    private SimpleDateFormat sf;

    public MessageAdapter() {
        sf = new SimpleDateFormat("HH:mm:ss");
//        this.gaos = gaos;
//        Collections.sort(gaos, new Comparator<MessageGao>() {
//            @Override
//            public int compare(MessageGao lhs, MessageGao rhs) {
//                return rhs.getMessagesTamp().compareTo(lhs.getMessagesTamp());
//            }
//        });
    }

    public void setGao(List<MessageGao> gaos) {
        this.gaos = gaos;
        Collections.sort(gaos, new Comparator<MessageGao>() {
            @Override
            public int compare(MessageGao lhs, MessageGao rhs) {
                return rhs.getMessagesTamp().compareTo(lhs.getMessagesTamp());
            }
        });
    }

    @Override
    public int getCount() {
        if (gaos == null) return 0;
        return gaos.size();
    }

    @Override
    public MessageGao getItem(int position) {
        return gaos.get(position);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_message_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        MessageGao message = getItem(position);

        Picasso.with(parent.getContext())
                .load(message.getUserProfileImage())
                .error(R.drawable.ic_hndeveloper)
                .into(holder.icon);

        holder.txtDisPlayName.setText(message.getDisplayName());
        holder.txtMessage.setText(message.getMessageText());
        holder.txtTime.setText(sf.format(message.getMessagesTamp()));
        return convertView;
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
