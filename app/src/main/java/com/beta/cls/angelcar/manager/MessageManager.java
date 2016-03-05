package com.beta.cls.angelcar.manager;

import com.beta.cls.angelcar.gao.MessageCollectionGao;
import com.beta.cls.angelcar.gao.MessageGao;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 29/2/59. เวลา 11:24
 ***************************************/
public class MessageManager {
    private MessageCollectionGao messageGao;

    public MessageCollectionGao getMessageGao() {
        return messageGao;
    }

    public void setMessageGao(MessageCollectionGao messageGao) {
        this.messageGao = messageGao;
    }

    public int getMessageSize(){
        return messageGao.getMessage().size();
    }

    public void updateDataToLastPosition(MessageCollectionGao messageGa){
        messageGao.getMessage().addAll(getMessageSize(),messageGa.getMessage());
    }

    public int getMaximumId(){
        if (messageGao == null)
            return 0;
        if (messageGao.getMessage().size() == 0)
            return 0;
        int maxId = messageGao.getMessage().get(0).getMessageId();
        for (int i = 0; i < messageGao.getMessage().size(); i++)
            maxId = Math.max(maxId, messageGao.getMessage().get(i).getMessageId());
        return maxId;
    }


}
