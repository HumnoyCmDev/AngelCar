package com.beta.cls.angelcar.manager;

import com.beta.cls.angelcar.dao.MessageCollectionDao;
import com.beta.cls.angelcar.dao.MessageDao;

import java.util.ArrayList;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 29/2/59. เวลา 11:24
 ***************************************/
public class MessageManager {
    private MessageCollectionDao messageDao;

    public MessageCollectionDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(MessageCollectionDao messageDao) {
        this.messageDao = messageDao;
    }

    public int getCount(){
        if (messageDao == null) return 0;
        if (messageDao.getMessage() == null) return 0;

        return messageDao.getMessage().size();
    }

    public void updateDataToLastPosition(MessageCollectionDao gao){
        if (messageDao == null){
            messageDao = new MessageCollectionDao();
        }
        if (messageDao.getMessage() == null){
            messageDao.setMessage(new ArrayList<MessageDao>());
        }
        messageDao.getMessage().addAll(getCount(),gao.getMessage());
    }

    public int getMaximumId(){
        if (messageDao == null)
            return 0;
        if (messageDao.getMessage().size() == 0)
            return 0;
        int maxId = messageDao.getMessage().get(0).getMessageId();
        for (int i = 0; i < messageDao.getMessage().size(); i++)
            maxId = Math.max(maxId, messageDao.getMessage().get(i).getMessageId());
        return maxId;
    }


}
