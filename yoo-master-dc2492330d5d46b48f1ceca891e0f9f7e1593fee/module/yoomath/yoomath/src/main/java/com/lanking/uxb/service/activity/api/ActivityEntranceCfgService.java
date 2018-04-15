package com.lanking.uxb.service.activity.api;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.activity.ActivityEntranceCfg;

public interface ActivityEntranceCfgService {

	ActivityEntranceCfg findByApp(YooApp app);
}
