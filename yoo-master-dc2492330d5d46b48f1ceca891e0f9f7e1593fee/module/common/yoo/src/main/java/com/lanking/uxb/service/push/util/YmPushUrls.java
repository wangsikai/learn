package com.lanking.uxb.service.push.util;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.push.type.OpenPath;

/**
 * @since 2.2.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月2日
 */
public final class YmPushUrls {
	private final static String MATH_STUDENT_URL_PREFIX = "math_student://yoomath.com";
	private final static String MATH_TEACHER_URL_PREFIX = "math_teacher://yoomath.com";

	public final static String MATH_STUDENT_DAILYPRACTICE_HOME = "math_student://yoomath.com/dailypractice-home";

	public static String url(YooApp app, OpenPath path, ValueMap vm) {
		if (app == YooApp.MATH_STUDENT) {
			if (path != null) {
				StringBuilder sb = new StringBuilder(MATH_STUDENT_URL_PREFIX);
				sb.append("/");
				sb.append(path.getName());
				if (vm != null) {
					boolean first = true;
					for (String key : vm.keySet()) {
						if (first) {
							sb.append("?");
							first = false;
						}
						sb.append(key).append("=").append(vm.get(key).toString());
					}
				}
				return sb.toString();
			} else {
				return MATH_STUDENT_URL_PREFIX;
			}
		} else if (app == YooApp.MATH_TEACHER) {
			if (path != null) {
				StringBuilder sb = new StringBuilder(MATH_TEACHER_URL_PREFIX);
				sb.append("/");
				sb.append(path.getName());
				if (vm != null) {
					boolean first = true;
					for (String key : vm.keySet()) {
						if (first) {
							sb.append("?");
							first = false;
						}
						sb.append(key).append("=").append(vm.get(key).toString());
					}
				}
				return sb.toString();
			} else {
				return MATH_TEACHER_URL_PREFIX;
			}
		}
		return StringUtils.EMPTY;
	}

}
