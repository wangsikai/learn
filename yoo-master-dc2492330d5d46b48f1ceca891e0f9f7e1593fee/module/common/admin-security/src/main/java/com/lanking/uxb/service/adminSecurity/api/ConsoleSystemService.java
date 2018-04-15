package com.lanking.uxb.service.adminSecurity.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.support.common.auth.ConsoleSystem;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.adminSecurity.form.ConsoleSystemForm;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ConsoleSystemService {
	ConsoleSystem save(ConsoleSystemForm form);

	ConsoleSystem get(Long id);

	List<ConsoleSystem> findAll();

	List<ConsoleSystem> mgetList(Collection<Long> systemId);

	ConsoleSystem updateStatus(long id, Status status);

	Map<Long, ConsoleSystem> mget(Collection<Long> systemIds);
}
