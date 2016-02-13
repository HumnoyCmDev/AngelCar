package com.beta.cls.angelcar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beta.cls.angelcar.Adapter.MessageItemAdapter;
import com.beta.cls.angelcar.R;
import com.beta.cls.angelcar.activity.ChatMessageActivity;
import com.beta.cls.angelcar.manager.AllMessageAsync;
import com.beta.cls.angelcar.util.BlogMessage;
import com.beta.cls.angelcar.util.PostBlogArrayMessage;
import com.beta.cls.angelcar.util.PostBlogMessage;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by humnoy on 26/1/59.
 */
public class ChatAllFragment extends Fragment{
    @Bind(R.id.list_view)
    ListView listView;
    private static final String TAG = "ChatAllFragment";

    private List<BlogMessage> message;
    private MessageItemAdapter itemAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_view_layout,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPutMessage();
        initListener();
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlogMessage blogMessage = message.get(position);
                Intent intent = new Intent(getActivity(), ChatMessageActivity.class);
                intent.putExtra("messageBy",blogMessage.getMessageby());
                intent.putExtra("BlogMessage", Parcels.wrap(blogMessage));
                startActivity(intent);
            }
        });
    }

    private void initPutMessage() {
        new AllMessageAsync(new AllMessageAsync.CallBackResult() {
            @Override
            public void onPostResult(PostBlogArrayMessage blogArrayMessage,
                                     PostBlogMessage postBlogMessage) {
                message = blogArrayMessage.getMessageViewByAdmin().get(0).getMessage();
                message.addAll(postBlogMessage.getMessage());
                itemAdapter = new MessageItemAdapter(message);
                listView.setAdapter(itemAdapter);

            }
        }).execute();
    }

}
