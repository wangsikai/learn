package com.lanking.uxb.zycon.fallible.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.fallible.ClassFallibleExportRecord;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.zycon.fallible.api.ClassFallibleExportRecordService;
import com.lanking.uxb.zycon.fallible.api.FallibleExportService;
import com.lanking.uxb.zycon.homeclazz.api.ZycHkClazzService;

/**
 * 悠作业学生错题本接口
 * 
 * @since yoomath V1.0
 * @author <a href="mailto:pengcheng.yu@elanking.com">pengcheng.yu</a>
 * @version 2018年1月4日
 */
@ApiAllowed
@RestController
@RequestMapping("zyc/fallible")
public class FallibleQuestionController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ClassFallibleExportRecordService classFallibleExportRecordService;
	@Autowired
	private FallibleExportService fallibleExportService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZycHkClazzService zycHkClazzService;
	/**
	 * 错题下载首页数据.
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "downloadIndex", method = { RequestMethod.POST, RequestMethod.GET })
	public Value downloadIndex(long clazzId) {
		List<Textbook> textbooks = new ArrayList<Textbook>();

		Integer phaseCode = null;
		Teacher teacher = null;

		teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, zyHkClassService.get(clazzId).getTeacherId()));
		phaseCode = teacher.getPhaseCode();

		if (teacher != null) {// 加入班级了
			if (teacher.getTextbookCode() == null) {// 老师没有设置版本教材
				if (teacher.getPhaseCode().intValue() == SubjectService.PHASE_2_MATH) {
					// 初中苏科版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_KE_BAN,
							SubjectService.PHASE_2_MATH);
				} else if (teacher.getPhaseCode().intValue() == SubjectService.PHASE_3_MATH) {
					// 高中苏教版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_JIAO_BAN,
							SubjectService.PHASE_3_MATH);
				}
			} else {
				textbooks = tbService.find(phaseCode, teacher.getTextbookCategoryCode(), teacher.getSubjectCode());
			}
		}

		List<Map<String, Object>> nodes = Lists.newArrayList();
		if (textbooks != null && textbooks.size() > 0) {
			List<Integer> textBookCodes = new ArrayList<Integer>(textbooks.size());

			// 教材数据
			Map<Integer, Map<String, Object>> textBookMap = new HashMap<Integer, Map<String, Object>>(textbooks.size());
			for (Textbook t : textbooks) {
				textBookCodes.add(t.getCode());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", t.getCode());
				map.put("name", t.getName());
				map.put("children", new ArrayList<Map<String, Object>>());
				map.put("count", "");
				map.put("type", "TEXTBOOK");
				textBookMap.put(t.getCode(), map);
				nodes.add(map);
			}

			// 章节数据
			Map<Long, Map<String, Object>> section1s = new HashMap<Long, Map<String, Object>>();
			List<Section> sections = sectionService.findByTextbookCode(textBookCodes, 1);
			for (Section section : sections) {
				if (section.getLevel() == 1) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", section.getCode());
					map.put("name", section.getName());
					map.put("count", 0);
					map.put("textbookCode", section.getTextbookCode());
					map.put("type", "SECTION");

					section1s.put(section.getCode(), map);
				}
			}

			for (Section section : sections) {
				Map<String, Object> s = section1s.get(section.getCode());
				if (s == null) {
					continue;
				}
				if (section.getLevel() == 1 && null != s) {
					((List) textBookMap.get((Integer) s.get("textbookCode")).get("children")).add(s);
				}
			}

			for (int i = nodes.size() - 1; i >= 0; i--) {
				if (((List) nodes.get(i).get("children")).size() == 0) {
					nodes.remove(i);
				}
			}
		}

		return new Value(nodes);
	}

	/**
	 * 生成班级错题本
	 * @param exportType 导出类型（0：全班；1：部分学生）
	 * @param sectionCodes 章节
	 * @param host
	 * @param studentIds 学生列表
	 * @param clazzId 班级id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "createExportDoc")
	public Value createExportDoc(Integer exportType, @RequestParam(value = "sectionCodes") List<Long> sectionCodes,
			String host, @RequestParam(value = "studentIds",required=false) List<Long> studentIds, long clazzId,String clazzName,String schoolName) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Long> allStudentIds = new ArrayList<Long>();
		allStudentIds = zyHkStuClazzService.listClassStudents(clazzId);
		List<Map> studnetsMap =zycHkClazzService.queryClazzMember(clazzId);
		Map<Long,String> stuMap = new HashMap<Long,String>();
		for(Map student : studnetsMap){
			stuMap.put(Long.parseLong(String.valueOf(student.get("id"))),String.valueOf(student.get("realname")));
		}
		if (exportType ==0){//导出全班的学生
			studentIds = allStudentIds;
		}
		if(!StringUtils.isBlank(clazzName)){
			try {
				clazzName = java.net.URLDecoder.decode(clazzName,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(!StringUtils.isBlank(schoolName)){
			try {
				schoolName = java.net.URLDecoder.decode(schoolName,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		//没有要导出的学生
		if(null == studentIds || studentIds.size()==0){
			map.put("recordId", -1);
			return new Value(map);
		}
		ClassFallibleExportRecord record = fallibleExportService.batchWriteFileAndRecordTask(host, sectionCodes, allStudentIds,studentIds, clazzId,exportType,clazzName,stuMap,schoolName);
		map.put("recordId", null!=record?record.getId():-1);
		return new Value(map);
	}

	/**
	 * 判断学生所有教材章节是否有错题.
	 * 
	 * @return
	 */
	@RequestMapping(value = "getTextbookQuestionCount")
	public Value getTextbookQuestionCount() {

		List<Textbook> textbooks = new ArrayList<Textbook>();
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		Integer phaseCode = null;
		Teacher teacher = null;
		Student student = null;
		if (CollectionUtils.isNotEmpty(clazzs)) {
			HomeworkClazz clazz = zyHkClassService.get(clazzs.get(0).getClassId());
			if (clazz.getTeacherId() != null) {
				teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, clazz.getTeacherId()));
				phaseCode = teacher.getPhaseCode();
			}
		} else {
			student = ((Student) studentService.getUser(UserType.STUDENT, Security.getUserId()));
			phaseCode = student.getPhaseCode();
		}
		if (teacher != null) {// 加入班级了
			if (teacher.getTextbookCode() == null) {// 老师没有设置版本教材
				if (teacher.getPhaseCode() == null) {
					return new Value(0);
				}
				if (teacher.getPhaseCode().intValue() == SubjectService.PHASE_2_MATH) {
					// 初中苏科版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_KE_BAN,
							SubjectService.PHASE_2_MATH);
				} else if (teacher.getPhaseCode().intValue() == SubjectService.PHASE_3_MATH) {
					// 高中苏教版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_JIAO_BAN,
							SubjectService.PHASE_3_MATH);
				}
			} else {
				textbooks = tbService.find(phaseCode, teacher.getTextbookCategoryCode(), teacher.getSubjectCode());
			}
		} else if (student != null) {
			if (student.getPhaseCode() == null) {
				return new Value(0);
			}
			Integer stuSubjectCode = student.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
					: SubjectService.PHASE_2_MATH;
			if (student.getTextbookCode() == null) { // 学生没有设置版本教材
				if (student.getPhaseCode().intValue() == SubjectService.PHASE_2_MATH) {
					// 初中苏科版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_KE_BAN,
							SubjectService.PHASE_2_MATH);
				} else if (student.getPhaseCode().intValue() == SubjectService.PHASE_3_MATH) {
					// 高中苏教版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_JIAO_BAN,
							SubjectService.PHASE_3_MATH);
				}
			} else {
				textbooks = tbService.find(phaseCode, student.getTextbookCategoryCode(), stuSubjectCode);
			}
		}

		if (textbooks.size() == 0) {
			return new Value(0);
		}
		List<Integer> textbookCodes = new ArrayList<Integer>(textbooks.size());
		for (Textbook tbook : textbooks) {
			textbookCodes.add(tbook.getCode());
		}

		return new Value(sfqService.getTextbookQuestionCount(Security.getUserId(), textbookCodes));
	}

	/**
	 * 检测文档生成状态.
	 * 
	 * @param hash
	 * @return
	 */
	@RequestMapping(value = "checkDocStatus")
	public Value checkDocStatus(Long recordId) {
		if (recordId == null) {
			return new Value(new MissingArgumentException());
		}

		Map<String, Object> returnmap = new HashMap<String, Object>(3);
		ClassFallibleExportRecord record = classFallibleExportRecordService.get(recordId);
		if (record != null) {
			returnmap.put("recordId", recordId);
			returnmap.put("status", record.getStatus());
			returnmap.put("record", record);
		}
		return new Value(returnmap);
	}

	/**
	 * 下载文档.
	 * 
	 * @param request
	 * @param response
	 * @param recordId
	 *            兑换记录ID
	 * @param hash
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request, HttpServletResponse response, Long recordId,
			String host,long clazzId) {
		if (recordId == null) {
			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return;
		}

		ClassFallibleExportRecord record = classFallibleExportRecordService.get(recordId);

		// 会员免费使用

		if (record == null) {
			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return;
		}

		// 获取文档存储路径
		String storePath = new StringBuffer(Env.getString("word.file.store.path"))
				.append("/stuFallible2/batchExport/").append(clazzId).append("/").append(record.getId())
				.append(".zip").toString();
		File file = new File(storePath);
		if (file.exists()) {
			this.fileDownload(record, request, response, file);
		} else {
			try {
				response.setHeader("Content-Type", "html/text; charset=UTF-8");
				response.getWriter().write("该班级下所选学生没有错题本可以生成！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return;
		}
	}
	
	/**
	 * 文件下载
	 * */
	private void fileDownload(ClassFallibleExportRecord record, HttpServletRequest request,
			HttpServletResponse response, File file) {
		OutputStream out = null;
		FileInputStream inputStream = null;
		FileChannel fileChannel = null;
		ByteBuffer bb = null;
		WritableByteChannel outChannel = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today = format.format(new Date());
		try {
			@SuppressWarnings("deprecation")
			String filename = new String((record.getName()+"("+today+")").getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
			String agent = request.getHeader("User-Agent").toLowerCase();
			if (agent.indexOf("msie") > 0 || agent.indexOf("trident") > 0 || agent.indexOf("edge") > 0) {
				filename = URLEncoder.encode(record.getName(), "UTF-8").replace("+", " ");
			}

			response.setHeader("Content-Disposition",
					"inline; filename=\"" + filename + ".zip\"");
			response.setHeader("Content-Type", "application/octet-stream; charset=UTF-8");

			out = response.getOutputStream();
			inputStream = new FileInputStream(file);
			fileChannel = inputStream.getChannel();

			outChannel = Channels.newChannel(out);
			fileChannel.transferTo(0, fileChannel.size(), outChannel); // NIO管道输出
		} catch (Exception e) {
			logger.error("export word fail ", e);
		} finally {
			if (null != bb) {
				bb.clear();
			}
			try {
				if (outChannel != null) {
					outChannel.close();
				}
				if (fileChannel != null) {
					fileChannel.close();
				}
				if (out != null) {
					out.flush();
					out.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 分页查询当前学生的错题下载记录.
	 * 
	 * @param pageNo
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "queryRecord")
	public Value queryRecord(Integer pageNo, Integer pageSize) {
		pageNo = pageNo == null ? 1 : pageNo;
		pageSize = pageSize == null ? 20 : pageSize;
		Page<ClassFallibleExportRecord> cp = classFallibleExportRecordService.query(P.offset((pageNo - 1) * pageSize, pageSize));
		VPage<ClassFallibleExportRecord> vp = new VPage<ClassFallibleExportRecord>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(pageNo);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(cp.getItems());
		return new Value(vp);
	}
	
	/**
	 * 生成学生错题本之前先进行检查看是否有正在导出的
	 * @param clazzId
	 * @return
	 */
	@RequestMapping(value="preExportDoc")
	public Value preExportDoc(Long clazzId){
		//先查询是否有正在生成错题本
		ClassFallibleExportRecord record = classFallibleExportRecordService.findByClassIdandStatus(clazzId, 1);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		//默认-1，表示既没有正在导出的也没有已经生成的错题本文件,
		returnMap.put("status", -1);
		if (null != record){
			//status=1,说明正在生成错题本
			returnMap.put("status", 1);
		}else{
			//如果不存在正在导出中的那么查询看以前是否有导出记录
			record = classFallibleExportRecordService.findByClassIdandStatus(clazzId, 2);
			
			if (null != record){
				// 获取文档存储路径
				String storePath = new StringBuffer(Env.getString("word.file.store.path"))
						.append("/stuFallible2/batchExport/").append(clazzId).append("/").append(record.getId())
						.append(".zip").toString();
				File file = new File(storePath);
				if (file.exists()) {
					//status=2,说明以前有生成过错题本
					returnMap.put("status", 2);
					returnMap.put("record",record);
				}else{
					returnMap.put("status", -1);
				}
				
			}
		}
		return new Value(returnMap);
	}
}
