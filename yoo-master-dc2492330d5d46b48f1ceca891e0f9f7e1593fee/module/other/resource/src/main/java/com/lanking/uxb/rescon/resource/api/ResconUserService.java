package com.lanking.uxb.rescon.resource.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.uxb.rescon.resource.value.VResconUser;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ResconUserService {
	VResconUser get(long id);

	Map<Long, VResconUser> mget(Collection<Long> ids);

	List<VResconUser> megtList(List<Long> userIds);
}
