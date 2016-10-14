package com.jiguang.jbox.util;


import android.widget.Toast;

import com.activeandroid.query.Select;
import com.jiguang.jbox.AppApplication;
import com.jiguang.jbox.data.Channel;
import com.jiguang.jbox.data.Developer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class AppUtil {

    /**
     * 打上 tag。
     */
    public static void setTags() {
        List<Developer> devs = new Select().from(Developer.class).execute();

        if (devs == null) {
            return;
        }

        Set<String> tags = new HashSet<>();  // 订阅 Channel 的 tag
        for (Developer dev : devs) {
            List<Channel> channels = new Select().from(Channel.class)
                    .where("DevKey = ?", dev.key)
                    .execute();
            for (Channel c : channels) {
                tags.add(dev.key + "_" + c.name);
            }
        }

        JPushInterface.setTags(AppApplication.getAppContext(), tags, new TagAliasCallback() {
            @Override
            public void gotResult(int status, String desc, Set<String> set) {
                if (status == 0) {
                    Toast.makeText(AppApplication.getAppContext(), "订阅成功", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    LogUtil.LOGI("JBox", desc);
                }
            }
        });
    }

}