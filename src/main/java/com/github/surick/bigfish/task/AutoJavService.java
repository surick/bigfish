package com.github.surick.bigfish.task;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.github.surick.bigfish.common.constants.ConfigConstants;
import com.github.surick.bigfish.common.utils.JavUtil;
import com.github.surick.bigfish.common.utils.PushUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jin
 * @since 2022/9/7
 */
@Service
public class AutoJavService {

    public static void run() {
        List<String> actors = JavUtil.getActors(
                HttpUtil.createGet("https://javdb.com/users/collection_actors")
                        .setHttpProxy("127.0.0.1", 7890).cookie(ConfigConstants.JAV_COOKIE).execute().body());

        for (String actor : actors) {
            List<String> videoList = JavUtil.getVideoList(
                    HttpUtil.createGet("https://javdb.com" + actor + "?t=d")
                            .setHttpProxy("127.0.0.1", 7890).cookie(ConfigConstants.JAV_COOKIE).execute().body());

            String last = videoList.get(0);
            List<String> magnets = JavUtil.getMagnet(
                    HttpUtil.createGet("https://javdb.com" + last)
                            .setHttpProxy("127.0.0.1", 7890).execute().body());

            PushUtil.push2Transmission(magnets.get(0));
        }
    }

    public static void run2() {
        StringBuilder sb = new StringBuilder();

        List<String> actors = JavUtil.getActors(
                HttpUtil.createGet("https://javdb.com/users/collection_actors")
                        .setHttpProxy("127.0.0.1", 7890).cookie(ConfigConstants.JAV_COOKIE).execute().body());

        for (String actor : actors) {
            String name = JavUtil.isUpdate(
                    HttpUtil.createGet("https://javdb.com" + actor + "?t=d")
                            .setHttpProxy("127.0.0.1", 7890).cookie(ConfigConstants.JAV_COOKIE).execute().body());
            if (StrUtil.isNotBlank(name)) {
                sb.append("【").append(name).append("】");
            }
        }
        PushUtil.push2Wx(sb.toString());
        PushUtil.push2Email(sb.toString());
    }

}
