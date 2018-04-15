package com.lanking.uxb.zycon.operation.api;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.activity.ActivityEntranceCfg;
import com.lanking.cloud.sdk.bean.Status;

public interface ZycActivityEntranceCfgService {

	ActivityEntranceCfg findByApp(YooApp app);

	ActivityEntranceCfg updateStatus(YooApp app, Status status, long userId);

	ActivityEntranceCfg update(YooApp app, Status status, Long icon, String uri, long userId);
}
