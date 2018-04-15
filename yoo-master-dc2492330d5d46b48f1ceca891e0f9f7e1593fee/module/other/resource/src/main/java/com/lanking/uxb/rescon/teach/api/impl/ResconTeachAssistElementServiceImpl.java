package com.lanking.uxb.rescon.teach.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.lanking.cloud.domain.common.resource.teachAssist.AbstractTeachAssistElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalog;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalogElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.util.CollectionUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistCatalogElementService;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 模块服务
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementServiceImpl implements ResconTeachAssistElementService, ApplicationContextAware,
		InitializingBean {
	private Map<TeachAssistElementType, ResconAbstractTeachAssistElementHandle> handleMap = Maps.newHashMap();
	private ApplicationContext app;

	@Autowired
	private ResconTeachAssistCatalogElementService catalogElementService;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		ResconAbstractTeachAssistElementHandle handle = handleMap.get(form.getType());
		if (handle != null) {
			handle.save(form);
		}
	}

	@Override
	@Transactional
	public void delete(TeachAssistElementForm form) {
		ResconAbstractTeachAssistElementHandle handle = handleMap.get(form.getType());
		if (null != handle) {
			handle.delete(form);
			catalogElementService.deleteByElement(form.getId());
		}
	}

	@Override
	@Transactional
	public void sequence(TeachAssistElementForm form) {
		if (form.getSequence() != null && form.getId() != null && form.getUserId() != null) {
			ResconAbstractTeachAssistElementHandle handle = handleMap.get(form.getType());
			if (handle != null) {
				catalogElementService.updateSequence(form.getId(), form.getCatalogId(), form.getSequence());
				handle.sequence(form.getId(), form.getSequence(), form.getUserId());
			}
		}
	}

	@Override
	public List<AbstractTeachAssistElement> get(long catalogId) {
		List<TeachAssistCatalogElement> ces = catalogElementService.findByCatalog(catalogId);
		List<AbstractTeachAssistElement> retList = new ArrayList<AbstractTeachAssistElement>(ces.size());
		Set<TeachAssistElementType> types = new HashSet<TeachAssistElementType>(ces.size());
		for (TeachAssistCatalogElement e : ces) {
			types.add(e.getType());
		}

		for (TeachAssistElementType t : types) {
			ResconAbstractTeachAssistElementHandle handle = handleMap.get(t);
			if (null != handle) {
				retList.addAll(handle.get(catalogId));
			}
		}
		return retList;
	}

	@Override
	@Transactional
	public void saveContent(TeachAssistElementForm form, long elementId) {
		ResconAbstractTeachAssistElementHandle handle = handleMap.get(form.getType());
		if (handle != null) {
			handle.saveContent(form, elementId);
		}
	}

	@Override
	@Transactional
	public void updateSequence(long id, int sequence, TeachAssistElementType type) {
		ResconAbstractTeachAssistElementHandle handle = handleMap.get(type);
		if (handle != null) {
			handle.updateContentSequence(id, sequence);
		}
	}

	@Override
	@Transactional
	public void deleteContent(long id, TeachAssistElementType type) {
		ResconAbstractTeachAssistElementHandle handle = handleMap.get(type);
		if (handle != null) {
			handle.deleteContent(id);
		}

	}

	@Override
	public List getContents(long id, TeachAssistElementType type) {
		ResconAbstractTeachAssistElementHandle handle = handleMap.get(type);
		if (null != handle) {
			return handle.getContents(id);
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public AbstractTeachAssistElement findOne(long id, TeachAssistElementType type) {
		ResconAbstractTeachAssistElementHandle handle = handleMap.get(type);
		if (null != handle) {
			return handle.findOne(id);
		}

		return null;
	}

	@Override
	public List getContents(Collection<Long> ids, TeachAssistElementType type) {
		ResconAbstractTeachAssistElementHandle handle = handleMap.get(type);
		if (null != handle) {
			return handle.mgetContents(ids);
		}

		return Collections.EMPTY_LIST;
	}

	@Override
	@Transactional
	public void copy(Map<Long, TeachAssistCatalog> map, long userId) {
		Map<Long, Map<TeachAssistElementType, Set<Long>>> typeCatalogMap = Maps.newHashMap();
		for (Map.Entry<Long, TeachAssistCatalog> entry : map.entrySet()) {
			List<TeachAssistCatalogElement> ces = catalogElementService.findByCatalog(entry.getKey());
			for (TeachAssistCatalogElement e : ces) {
				Map<TeachAssistElementType, Set<Long>> typeMap = typeCatalogMap.get(e.getTeachassistCatalogId());
				if (typeMap == null) {
					typeMap = Maps.newHashMap();
				}
				Set<Long> ids = typeMap.get(e.getType());
				if (CollectionUtils.isEmpty(ids)) {
					ids = Sets.newHashSet();
				}

				ids.add(e.getElementId());
				typeMap.put(e.getType(), ids);

				typeCatalogMap.put(e.getTeachassistCatalogId(), typeMap);
			}
		}

		for (Map.Entry<Long, Map<TeachAssistElementType, Set<Long>>> entryOne : typeCatalogMap.entrySet()) {
			Long catalogId = entryOne.getKey();
			Long newCatalogId = map.get(catalogId).getId();

			for (Map.Entry<TeachAssistElementType, Set<Long>> entryTwo : entryOne.getValue().entrySet()) {
				ResconAbstractTeachAssistElementHandle handle = handleMap.get(entryTwo.getKey());
				if (handle != null) {
					handle.copy(newCatalogId, entryTwo.getValue(), userId);
				}
			}
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.app = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (ResconAbstractTeachAssistElementHandle handle : app.getBeansOfType(
				ResconAbstractTeachAssistElementHandle.class).values()) {
			handleMap.put(handle.getType(), handle);
		}
	}
}
