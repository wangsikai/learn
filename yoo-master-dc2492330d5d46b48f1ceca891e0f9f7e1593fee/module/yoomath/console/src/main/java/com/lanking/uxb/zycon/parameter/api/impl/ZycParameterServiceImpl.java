/**
 * 
 */
package com.lanking.uxb.zycon.parameter.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.zycon.parameter.api.ZycParameterService;
import com.lanking.uxb.zycon.parameter.form.ParameterForm;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 * 
 */
@Service
@Transactional(readOnly = true)
public class ZycParameterServiceImpl implements ZycParameterService {

	@Autowired
	@Qualifier("ParameterRepo")
	private Repo<Parameter, Long> repo;

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Transactional
	@Override
	public int save(ParameterForm form) {
		// 存储
		int count = 0;
		if (form.getId() == null) {
			// Key不重复
			count = (int) repo.find("$zycGetParameterCountByKey", Params.param("key", form.getKey())).count();
			if (count > 0) {
				return count;
			}
			Parameter p = new Parameter();
			p.setKey(form.getKey());
			p.setNote(form.getNote());
			p.setValue(form.getValue());
			p.setProduct(form.getProduct());
			p.setStatus(Status.ENABLED);
			repo.save(p);
			return count;
		} else {
			// 编辑
			Parameter p = repo.get(form.getId());
			p.setProduct(form.getProduct());
			p.setNote(form.getNote());
			p.setValue(form.getValue());
			repo.save(p);
			return count;
		}
	}

	@Override
	public Page<Parameter> getAllList(Pageable p) {
		// Params params = Params.param("product", Product.YOOMATH.getValue());
		return repo.find("$zycAllParameters").fetch(p);
	}

	@Transactional
	@Override
	public int dalete(Long id) {
		Params params = Params.param("id", id);
		params.put("status", Status.DELETED.getValue());
		return repo.execute("$zycUpdateStatusById", params);
	}

	@Override
	public void syncData() {
		ClusterEvent<String> e = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.PARAMETER.name());
		sender.send(e);

	}
}
