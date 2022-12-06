package com.github.surick.bigfish.common.utils;

import cn.hutool.extra.mail.MailUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.github.surick.bigfish.common.constants.ConfigConstants;
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
        HttpUtil.get(ConfigConstants.FT_PUSH + "title=" + "更新啦！" + "&desp=" + name + "更新啦！");
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
                HttpUtil.createPost(ConfigConstants.TR_ADD)
                        .header("Authorization", ConfigConstants.TR_AUTH)
                        .header("X-Transmission-Session-Id", ConfigConstants.TR_SESSION_ID)
                        .cookie(ConfigConstants.TR_COOKIE)
                        .body(JSON.toJSONString(params)).execute().body();
        log.info("tr------{}", resp);
    }

    public static void push2Email(String name) {
        MailUtil.send(ConfigConstants.EMAIL, "更新啦！", name + "更新啦！", false);
    }

}
