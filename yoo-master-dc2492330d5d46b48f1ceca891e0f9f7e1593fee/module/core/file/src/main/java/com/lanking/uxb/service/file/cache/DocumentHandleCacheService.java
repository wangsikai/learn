package com.lanking.uxb.service.file.cache;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@Service
public class DocumentHandleCacheService extends AbstractCacheService {

	private ListOperations<String, Long> fileListOpt;

	private static final String DOC2PDF_HANDLE_LIST_KEY = "d2phl";

	private static final String PDF2SWF_HANDLE_LIST_KEY = "p2shl";

	public Long getDoc2Pdf() {
		return fileListOpt.index(DOC2PDF_HANDLE_LIST_KEY, 0);
	}

	public Long popDoc2Pdf() {
		return fileListOpt.leftPop(DOC2PDF_HANDLE_LIST_KEY);
	}

	public void pushDoc2Pdf(long id) {
		fileListOpt.rightPush(DOC2PDF_HANDLE_LIST_KEY, id);
	}

	public Long getPdf2Swf() {
		return fileListOpt.index(PDF2SWF_HANDLE_LIST_KEY, 0);
	}

	public Long popPdf2Swf() {
		return fileListOpt.leftPop(PDF2SWF_HANDLE_LIST_KEY);
	}

	public void pushPdf2Swf(long id) {
		fileListOpt.rightPush(PDF2SWF_HANDLE_LIST_KEY, id);
	}

	@Override
	public String getNs() {
		return "fh";
	}

	@Override
	public String getNsCn() {
		return "文档处理";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		fileListOpt = getRedisTemplate().opsForList();
	}
}
