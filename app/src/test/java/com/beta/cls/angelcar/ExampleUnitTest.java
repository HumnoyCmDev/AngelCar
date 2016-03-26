package com.beta.cls.angelcar;

import com.beta.cls.angelcar.util.LineUp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


    }

    @Test
    public void lineUpTestCase() throws Exception{
        String topic = "sdfsdhjghks";
        String detail = "hjkdahgiuerhg";
        String append = topic+"``"+detail;
        assertEquals(topic, LineUp.getInstance().subTopic(append));
        assertEquals(detail, LineUp.getInstance().subDetail(append));
        assertEquals(topic,LineUp.getInstance().subTopic(topic));

    }


}