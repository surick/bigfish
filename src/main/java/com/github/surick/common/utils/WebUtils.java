package com.github.surick.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.github.surick.model.WebDetails;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WebUtils {
    public static final String PATH = "C:\\Users\\jk103\\Desktop\\test\\";

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
            Request request = new Request.Builder().url(url).build();
            ;

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

    public static WebDetails getWebDetails(String html) {
        WebDetails webDetails = new WebDetails();
        Document doc = Jsoup.parse(html);
        Elements es = doc.select("img");
        if (es.size() == 0) {
            // es = doc.select("meta[name=description]");
            // if (es.size() != 0) {
            //     webDetails.setName(es.get(0).attr("title"));
            // }
        } else {
            for (Element e : es) {

                if (StrUtil.isNotBlank(e.attr("onerror"))) {
                    webDetails.setName(e.attr("title"));
                    webDetails.setUrl(e.attr("data-src"));

                    log.info("get---------{}", webDetails);

                    HttpUtil.downloadFileFromUrl(webDetails.getUrl(),
                            PATH + webDetails.getName().split("_")[0] + "\\" + webDetails.getName() + "." + FileUtil.getSuffix(webDetails.getUrl()));
                }
            }
        }
        return webDetails;
    }

    public static List<String> getMagnet(String html) {
        List<String> magnets = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements es = doc.select("a");
        if (es.size() > 0) {
            for (Element e : es) {
                if (StrUtil.isNotBlank(e.attr("href"))) {

                    String magnet = e.attr("href");
                    if (magnet.contains("magnet:?xt")) {
                        magnets.add(magnet);
                        log.info("get----- {}", magnet);
                    }
                }
            }
        }
        return magnets;
    }

    public static List<String> getActors(String html) {
        List<String> actors = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements es = doc.select("a");
        if (es.size() > 0) {
            for (Element e : es) {
                if (StrUtil.isNotBlank(e.attr("href")) && StrUtil.isNotBlank(e.attr("title"))) {

                    String actor = e.attr("href");
                    if (actor.contains("/actors/")) {
                        actors.add(actor);
                        log.info("get----- {}", actor);
                    }
                }
            }
        }
        return actors;
    }

    public static List<String> getVideoList(String html) {
        List<String> videos = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements es = doc.select("a");
        if (es.size() > 0) {
            for (Element e : es) {
                if (StrUtil.isNotBlank(e.attr("href")) && StrUtil.isNotBlank(e.attr("title"))) {

                    String video = e.attr("href");
                    if (video.contains("/v/")) {
                        videos.add(video);
                        log.info("get----- {}", video);
                    }
                }
            }
        }
        return videos;
    }

    public static void push2Transmission(String magnet) {
        Map<String, Object> params = new HashMap<>();

        params.put("method", "torrent-add");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("filename", magnet);
        arguments.put("paused", false);
        arguments.put("download-dir", "/volume2/downloads");
        params.put("arguments", arguments);
        params.put("tag", "");

        String resp =
                HttpUtil.createPost("http://192.168.2.6:9091/transmission/rpc")
                        .header("Authorization", "")
                        .header("X-Transmission-Session-Id", "")
                        .cookie("")
                        .body(JSON.toJSONString(params)).execute().body();
        log.info("tr------{}", resp);
    }

    public static String isUpdate(String html) {
        Document doc = Jsoup.parse(html);
        Elements es = doc.select("div[class=\"meta\"]");
        Elements title = doc.select("title");
        String name = title.get(0).html().split("\\|")[0];
        if (es.size() > 0) {
            for (Element e : es) {
                if (StrUtil.isNotBlank(e.html())) {

                    String lastDate = e.html();
                    if (DateUtil.parse(lastDate).after(new Date())) {
                        log.info("{} -----update-----", name);
                        return name;
                    }
                }
            }
        }
        return "";
    }

    public static void push2Wx(String name) {
        HttpUtil.get(
                "https://sctapi.ftqq.com/xxx.send?" +
                        "title=" + "更新啦！" + "&desp=" + name + "更新啦！");
    }

    public static void main(String[] args) {

        // getWebDetails(HttpUtil.get(""));
        // getMagnet(HttpUtil.createGet("").setHttpProxy("127.0.0.1", 7890).execute().body());
        // getActors(HttpUtil.createGet("").setHttpProxy("127.0.0.1", 7890).cookie(COOKIE).execute().body());
        //
        // getVideoList(HttpUtil.createGet("").setHttpProxy("127.0.0.1", 7890).cookie(COOKIE).execute().body());
        //  isUpdate(HttpUtil.createGet("").setHttpProxy("127.0.0.1", 7890).cookie(AutoJavService.COOKIE).execute().body());
        //  push2Transmission("");
    }
}
