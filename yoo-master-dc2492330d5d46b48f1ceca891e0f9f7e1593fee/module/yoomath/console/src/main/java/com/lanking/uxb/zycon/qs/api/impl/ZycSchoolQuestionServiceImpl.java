package com.lanking.uxb.zycon.qs.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.schoolQuestion.cache.SchoolQuestionCacheService;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.zycon.base.api.ZycQuestionService;
import com.lanking.uxb.zycon.qs.api.ZycQuestionSchoolService;
import com.lanking.uxb.zycon.qs.api.ZycQuestionSectionService;
import com.lanking.uxb.zycon.qs.api.ZycSchoolQuestionService;

/**
 * @see com.lanking.uxb.zycon.qs.api.ZycSchoolQuestionService
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Service
@Transactional(readOnly = true)
public class ZycSchoolQuestionServiceImpl implements ZycSchoolQuestionService {
	private static Logger logger = LoggerFactory.getLogger(ZycSchoolQuestionServiceImpl.class);

	@Autowired
	@Qualifier("SchoolQuestionRepo")
	private Repo<SchoolQuestion, Long> repo;
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycQuestionSchoolService schoolService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private SchoolQuestionCacheService schoolQuestionCacheService;
	@Autowired
	private ZycQuestionSectionService questionSectionService;

	// 导入失败的题目信息
	private static final String QUESTION_EXISTS = "题目已经存在此校本。";
	private static final String QUESTION_EXISTS_OTHERSCHOOL = "题目已经存在于其他学校校本题库。";
	private static final String QUESTION_NOT_EXISTS = "题目编号在此系统无法找到。";
	private static final String QUESTION_CERT_NOT_PASS = "题目校验没有通过。";
	private static final String QUESTION_IS_NOT_MATH = "题目不是数学题。";
	private static final String TMP_STORE_PATH = "sq";

	// 文件最多保存30分钟
	public static Integer EXPIRE_MINUTE = 30;
	// 最多可以一次性导入的题目数量
	public static Integer MAX_IMPORT_RECORD = 200;

	@Override
	public Page<SchoolQuestion> page(Pageable pageable, long schoolId, Integer subjectCode) {
		Params params = Params.param();
		if (subjectCode != null) {
			params.put("subjectCode", subjectCode + "%");
		}
		params.put("schoolId", schoolId);
		return repo.find("$zycQuery", params).fetch(pageable);
	}

	@Override
	@Transactional
	public void delete(long id, long schoolId) {
		SchoolQuestion sq = repo.get(id);
		// 将学校ID更新为0
		questionService.updateSchool(sq.getQuestionId(), 0);
		repo.delete(sq);
		schoolService.incrQuestionCount(schoolId, -1L);
		indexService.syncDelete(IndexType.SCHOOL_QUESTION, id);
	}

	@Override
	@Transactional
	public Map<String, Object> importQuestion(List<String> questionCodes, long schoolId) throws IOException {
		if (CollectionUtils.isEmpty(questionCodes)) {
			return null;
		}

		Integer successCount = 0;
		Integer errorCount = 0;

		Map<String, String> errorMap = Maps.newHashMap();
		List<Long> questionIds = Lists.newArrayList();

		Integer tmpCount = 0;
		Map<String, Question> questionMap = questionService.mgetByCode(questionCodes);
		List<Long> schoolQuestionIds = Lists.newArrayList();
		for (String code : questionCodes) {
			Question question = questionMap.get(code);
			if (null == question) {
				errorMap.put(code, QUESTION_NOT_EXISTS);
				errorCount++;
				continue;
			}
			if (question.getSchoolId() != 0) {
				errorMap.put(code, QUESTION_EXISTS_OTHERSCHOOL);
				errorCount++;
				continue;
			}

			if (exists(code, schoolId)) {
				errorMap.put(code, QUESTION_EXISTS);
				errorCount++;
				continue;
			}
			if (question.getStatus() != CheckStatus.PASS) {
				errorMap.put(code, QUESTION_CERT_NOT_PASS);
				errorCount++;
				continue;
			}
			// 非数学题不可以导入
			if (!(question.getSubjectCode() == 102 || question.getSubjectCode() == 202 || question.getSubjectCode() == 302)) {
				errorMap.put(code, QUESTION_IS_NOT_MATH);
				errorCount++;
				continue;
			}
			questionIds.add(question.getId());
			SchoolQuestion schoolQuestion = new SchoolQuestion();
			schoolQuestion.setType(question.getType());
			schoolQuestion.setQuestionId(question.getId());
			schoolQuestion.setCreateAt(new Date());
			schoolQuestion.setDifficulty(question.getDifficulty());
			schoolQuestion.setSubjectCode(question.getSubjectCode());
			schoolQuestion.setSchoolId(schoolId);
			schoolQuestion.setTypeCode(question.getTypeCode());

			repo.save(schoolQuestion);

			schoolQuestionIds.add(schoolQuestion.getId());
			successCount++;
		}

		// 增加学校题库绑定数量
		schoolService.incrQuestionCount(schoolId, successCount);
		// 更新题目的学校ID
		if (CollectionUtils.isNotEmpty(questionIds)) {
			questionService.updateSchool(questionIds, schoolId);
		}
		if (schoolQuestionIds.size() != 0) {
			indexService.syncUpdate(IndexType.SCHOOL_QUESTION, schoolQuestionIds);
		}
		if (questionIds.size() != 0) {
			updateSchoolCache(schoolId, questionIds);
		}

		Map<String, Object> result = Maps.newHashMap();
		result.put("errorCount", errorCount);
		result.put("successCount", successCount);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("错误题目列表");
		sheet.setDefaultColumnWidth((short) 15);

		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("题目编码");
		row.createCell(1).setCellValue("失败原因");

		int rowCount = 1;
		for (Map.Entry<String, String> entry : errorMap.entrySet()) {
			row = sheet.createRow(rowCount);
			row.createCell(0).setCellValue(entry.getKey());
			row.createCell(1).setCellValue(entry.getValue());
			rowCount++;
		}

		String storepath = Env.getString("zycon.file.store.path.tmp") + File.separator + TMP_STORE_PATH;
		File folder = new File(storepath);
		// 如果不存在此文件夹则创建此目录
		if (!folder.exists()) {
			folder.mkdirs();
		}

		Long systemMillis = System.currentTimeMillis();
		String fileName = systemMillis + "_" + (systemMillis + TimeUnit.MINUTES.toMillis(EXPIRE_MINUTE));
		OutputStream out = new FileOutputStream(storepath + File.separator + fileName + ".xls");
		workbook.write(out);
		out.flush();

		result.put("fileName", fileName);

		return result;
	}

	@Override
	public Map<String, Object> getCodeFromImportFile(HttpServletRequest request) {
		Workbook workbook = null;
		List<String> rowLst = new ArrayList<String>();
		int count = 0;
		try {
			InputStream in = request.getInputStream();
			if (!in.markSupported()) {
				in = new PushbackInputStream(in, 1024);
			}
			if (POIFSFileSystem.hasPOIFSHeader(in)) {
				workbook = new HSSFWorkbook(in);
			} else if (POIXMLDocument.hasOOXMLHeader(in)) {
				workbook = new XSSFWorkbook(OPCPackage.open(in));
			}

			Sheet sheet = workbook.getSheetAt(0);
			int rowCount = sheet.getPhysicalNumberOfRows();
			if (rowCount >= (MAX_IMPORT_RECORD + 1)) {
				return null;
			}
			for (int r = 1; r < rowCount; r++) {
				Row row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				for (int c = 0; c < 1; c++) {
					Cell cell = row.getCell(c);
					String code = cell.toString().trim();
					if (code.equals("")) {
						continue;
					}
					if (rowLst.contains(code)) {
						count++;
					} else {
						rowLst.add(code);
					}
				}
			}
		} catch (Exception e) {
			logger.error("zycon import school question error {}", e);
		}
		Map<String, Object> map = Maps.newHashMap();
		map.put("rowLst", rowLst);
		map.put("count", count);
		return map;
	}

	@Override
	public InputStream download(String fileName) throws FileNotFoundException {
		String path = Env.getString("zycon.file.store.path.tmp") + File.separator + TMP_STORE_PATH + File.separator
				+ fileName + ".xls";
		return new FileInputStream(path);
	}

	/**
	 * 此校本是否已经导入过此题
	 *
	 * @param code
	 *            导入的Code
	 * @return true/false
	 */
	private boolean exists(String code, long schoolId) {
		long count = repo.find("$zycExists", Params.param("qCode", code).put("schoolId", schoolId)).count();
		return count > 0;
	}

	@Override
	@Async
	public void updateSchoolCache(Long schoolId, List<Long> questionIds) {
		List<Integer> textBookCodes = questionSectionService.findByQuestionIds(questionIds);
		schoolQuestionCacheService.setTextbookFlag(schoolId, textBookCodes, "1");
	}

	@Override
	public long countSchoolQuestion(long schoolId) {
		return repo.find("$countBySchoolId", Params.param("schoolId", schoolId)).count();
	}

	@Override
	public Map<Long, Long> mgetCountSchoolQuestion(Collection<Long> schoolIds) {
		List<Map> listMap = repo.find("$countBySchoolIds", Params.param("schoolIds", schoolIds)).list(Map.class);
		Map<Long, Long> retMap = new HashMap<Long, Long>(listMap.size());
		for (Map m : listMap) {
			retMap.put(Long.valueOf(m.get("schoolid").toString()), Long.valueOf(m.get("c").toString()));
		}
		return retMap;
	}
}
