package com.beta.cls.angelcar.gao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/***************************************
 * สร้างสรรค์ผลงานดีๆ
 * โดย humnoy Android Developer
 * ลงวันที่ 3/3/59. เวลา 16:09
 ***************************************/
public class TopicCollectionGao {

    @SerializedName("topic") @Expose List<TopicGao> topic;
    public List<TopicGao> getTopic() {
        return topic;
    }

    public void setTopic(List<TopicGao> topic) {
        this.topic = topic;
    }
}
