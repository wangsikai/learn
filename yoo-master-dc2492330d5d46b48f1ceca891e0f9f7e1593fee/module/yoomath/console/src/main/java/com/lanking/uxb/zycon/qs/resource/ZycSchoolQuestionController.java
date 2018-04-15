package com.lanking.uxb.zycon.qs.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.qs.api.ZycSchoolQuestionService;
import com.lanking.uxb.zycon.qs.convert.ZycSchoolQuestionConverter;
import com.lanking.uxb.zycon.qs.value.VZycSchoolQuestion;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@RestController
@RequestMapping(value = "zyc/sq")
public class ZycSchoolQuestionController {
	@Autowired
	private ZycSchoolQuestionService schoolQuestionService;
	@Autowired
	private ZycSchoolQuestionConverter schoolQuestionConverter;

	/**
	 * 查询此学校下绑定题目
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            一页数量
	 * @param schoolId
	 *            学校id
	 * @param subjectCode
	 *            subjectCode
	 * @return {@link Value}
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size,
			@RequestParam(value = "schoolId") long schoolId,
			@RequestParam(value = "subjectCode", required = false) Integer subjectCode) {
		Pageable pageable = P.index(page, size);
		Page<SchoolQuestion> p = schoolQuestionService.page(pageable, schoolId, subjectCode);
		List<VZycSchoolQuestion> vs = schoolQuestionConverter.to(p.getItems());
		VPage<VZycSchoolQuestion> vp = new VPage<VZycSchoolQuestion>();
		vp.setPageSize(size);
		vp.setCurrentPage(page);
		vp.setTotal(p.getTotalCount());
		vp.setTotalPage(p.getPageCount());
		vp.setItems(vs);

		return new Value(vp);
	}

	/**
	 * 删除校本题库中的题目
	 *
	 * @param id
	 *            校本题目的id
	 * @param schoolId
	 *            学校id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delete(@RequestParam(value = "id") long id, @RequestParam(value = "schoolId") long schoolId) {
		schoolQuestionService.delete(id, schoolId);

		return new Value();
	}

	/**
	 * 导入题目
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @param response
	 *            {@link HttpServletResponse}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "import_question", method = { RequestMethod.GET, RequestMethod.POST })
	public Value importQuestion(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "schoolId") long schoolId) throws IOException {

		Map<String, Object> codeMap = schoolQuestionService.getCodeFromImportFile(request);
		List<String> codes = (List<String>) codeMap.get("rowLst");
		Integer count = (Integer) codeMap.get("count");
		if (CollectionUtils.isEmpty(codes)) {
			return new Value(new IllegalArgException());
		}
		Map<String, Object> result = schoolQuestionService.importQuestion(codes, schoolId);
		Map<String, Object> map = Maps.newHashMap();
		map.put("errorCount", result.get("errorCount"));
		map.put("successCount", result.get("successCount"));
		map.put("totalCount", codes.size());
		Long timestamp = System.currentTimeMillis();
		map.put("fileName", result.get("fileName"));
		map.put("count", count);
		return new Value(map);
	}

	/**
	 * 下载导入失败的题目列表
	 *
	 * @param fileName
	 *            文件名
	 * @param request
	 *            {@link HttpServletRequest}
	 * @param response
	 *            {@link HttpServletResponse}
	 */
	@RequestMapping(value = "download", method = { RequestMethod.GET, RequestMethod.POST })
	public void downloadError(@RequestParam(value = "fileName") String fileName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		InputStream is = schoolQuestionService.download(fileName);
		response.reset();
		response.setContentType("bin");
		response.addHeader("Content-Disposition", "attachment; filename=\""
				+ new String("导入错误列表.xls".getBytes("UTF-8"), "ISO-8859-1") + "\"");
		byte[] b = new byte[100];
		int len;
		while ((len = is.read(b)) > 0)
			response.getOutputStream().write(b, 0, len);
		is.close();
	}

}
