package com.github.surick.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.github.surick.common.enums.RssType;
import com.github.surick.model.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Jin
 * @since 2022/9/7
 */
@Slf4j
public class RssUtils {

    private RssUtils() {

    }

    /**
     * 处理 XML 数据
     *
     * @param xml XML
     * @return 第一个参数是站点标题, 第二个参数是文章列表
     */
    public static Map<String, List<Resource>> parseXML(String xml) {
        try {
            if (xml == null) {
                return null;
            }
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            String type = root.getName();
            if (RssType.RSS.name().equalsIgnoreCase(type)) {
                return rssType(root);
            } else if (RssType.FEED.name().equalsIgnoreCase(type)) {
                return feedType(root);
            }
        } catch (DocumentException ignore) {
        }
        return null;
    }

    /**
     * 处理 feed 格式的数据
     *
     * @param root 根节点
     * @return 第一个参数是站点标题, 第二个参数是文章列表
     */
    public static Map<String, List<Resource>> feedType(Element root) {
        String title = String.valueOf(root.elements("title").get(0).getData());
        List<Element> elements = root.elements("entry");
        List<Resource> entries = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        for (Element e : elements) {
            String t = (String) e.elements("title").get(0).getData();
            String l = (String) e.elements("link").get(0).attribute("href").getData();
            String desc = (String) e.elements("summary").get(0).getData();
            Date d = null;
            try {
                String date = String.valueOf(e.elements("updated").get(0).getData()).trim();
                d = simpleDateFormat.parse(date);
            } catch (Exception ignore) {
                try {
                    d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00");
                } catch (ParseException ignore1) {
                }
            }
            entries.add(Resource.builder().title(t).link(l).desc(desc).pushDate(DateUtil.formatDate(d)).build());
            log.info("get----{}", entries);
        }
        Map<String, List<Resource>> res = new HashMap<>();
        res.put(title, entries);
        return res;
    }

    /**
     * 处理 rss 格式的数据
     *
     * @param root 根节点
     * @return 第一个参数是站点标题, 第二个参数是文章列表
     */
    public static Map<String, List<Resource>> rssType(Element root) {
        Element channel = root.elements("channel").get(0);
        String title = String.valueOf(channel.elements("title").get(0).getData());
        List<Element> elements = channel.elements("item");
        List<Resource> entries = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        for (Element e : elements) {
            String t = (String) e.elements("title").get(0).getData();
            String l = (String) e.elements("link").get(0).getData();
            String desc = (String) e.elements("description").get(0).getData();
            Date d = null;
            try {
                String date = String.valueOf(e.elements("pubDate").get(0).getData()).trim();
                d = simpleDateFormat.parse(date);
            } catch (Exception ignore) {
                try {
                    d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00");
                } catch (ParseException ignore1) {
                }
            }
            entries.add(Resource.builder().title(t).link(l).desc(desc).pushDate(DateUtil.formatDate(d)).build());
            log.info("get----{}", entries);
        }
        Map<String, List<Resource>> res = new HashMap<>();
        res.put(title, entries);
        return res;
    }

    /**
     * 获取网页数据
     *
     * @param url 网页链接
     * @return html
     */
    public static String get(String url) {
        try {
            StringBuilder stringBuilder = new StringBuilder();

            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder().url(url).build();;

            Response response;
            response = okHttpClient.newCall(request).execute();
            try (ResponseBody responseBody = response.body()) {
                BufferedReader bufferedReader = new BufferedReader(Objects.requireNonNull(responseBody).charStream());
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                response.close();
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取简介,图片等信息
     *
     * @return WebDetails
     */
    // public static WebDetails getWebDetails(String html) {
    //     WebDetails webDetails = new WebDetails();
    //     org.jsoup.nodes.Document doc = Jsoup.parse(html);
    //     Elements es = doc.select("meta[property]");
    //     if (es.size() == 0) {
    //         es = doc.select("meta[name=description]");
    //         if (es.size() != 0) {
    //             webDetails.description = es.get(0).attr("content");
    //         }
    //     } else {
    //         for (org.jsoup.nodes.Element e : es) {
    //             if (e.attr("property").equals("og:description")) {
    //                 webDetails.description = e.attr("content");
    //             }
    //             if (e.attr("property").equals("og:image")) {
    //                 webDetails.imageUrl = e.attr("content");
    //             }
    //         }
    //     }
    //     return webDetails;
    // }
    public static void main(String[] args) {
        parseXML(HttpUtil.get("http://www.ruanyifeng.com/blog/atom.xml"));
    }
}
