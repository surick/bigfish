package com.github.surick.common.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin
 * @since 2022/9/8
 */
@Slf4j
public class PushUtil {

    public static void push2Wx(String name) {
        HttpUtil.get(
                "https://sctapi.ftqq.com/xxx.send?" +
                        "title=" + "更新啦！" + "&desp=" + name + "更新啦！");
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
}
