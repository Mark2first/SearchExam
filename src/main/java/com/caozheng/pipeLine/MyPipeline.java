package com.caozheng.pipeLine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyPipeline implements Pipeline {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println(resultItems);
        // 获取文章数据
        Map<String, Object> data = new HashMap<>();
        data.put("url",resultItems.getRequest().getUrl());
        data.put("id",resultItems.get("id"));
        data.put("title",resultItems.get("title"));
        data.put("createTime",resultItems.get("createTime"));
        data.put("content",resultItems.get("content"));
        data.put("read",resultItems.get("read"));
        data.put("comment",resultItems.get("comment"));
        try{
            String json = MAPPER.writeValueAsString(data);
            FileUtils.write(new File("./data.json"),json + "\n", "UTF-8",true);
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
