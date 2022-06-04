package com.caozheng;

import com.caozheng.pipeLine.MyPipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.List;

public class cnBlogProcessor2 implements PageProcessor {

    private Site site = Site.me().setRetrySleepTime(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        Html html = page.getHtml();

        int j=0;

        // 链接详情
        List<String> list = html.css(".postTitle a").links().all();
        page.addTargetRequests(list);

        // 文章id
        String id = list.get(j);
        id = id.replace("https://www.cnblogs.com/tencent-cloud-native/p/","")
                .replace(".html","");
        page.putField("id",id);

        // 标题
        String title = html.xpath("//a[@id='cb_post_title_url']/span/text()").toString();
        page.putField("title",title);

        // 发表时间
        page.putField("createTime",html.xpath("//div[@class='postDesc']/span[@id='post-date']/text()").toString());

        // 正文
        page.putField("content",html.xpath("//div[@id='cnblogs_post_body']").toString());

        // 阅读数
        page.putField("read",html.xpath("//span[@id='post_view_count']/text()").toString());

        // 评论数
        page.putField("comment",html.xpath("//span[@id='post_comment_count']/text()").toString());

        // 获取多个页面的内容
        if (page.getResultItems().get("title") == null){
            page.setSkip(true);
            // 分页链接
            for(int i=0;i<7;i++){   // 这里的i设置为小于目前爬取的页面
                page.addTargetRequest("https://cnblogs.com/tencent-cloud-native/default.html?page="+i);
            }
        }
        j++;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new cnBlogProcessor2())
                .addUrl("https://cnblogs.com/tencent-cloud-native")
                .addPipeline(new MyPipeline())
                .thread(1)
                .run();
    }
}
