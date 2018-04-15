package com.lanking.uxb.service.resources.api.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.core.UnSupportedOperationException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

@Transactional(readOnly = true)
@Service
public class StudentHomeworkServiceImpl implements StudentHomeworkService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> studentHomeworkRepo;

	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private HomeworkQuestionService hqService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private StudentHomeworkAnswerService shaService;
	@Autowired
	private ZyStudentHomeworkQuestionService zyStuHkQuestionService;

	@Transactional(readOnly = true)
	@Override
	public StudentHomework get(long id) {
		return studentHomeworkRepo.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public StudentHomework getByHomeworkAndStudentId(long homeworkId, long studentId) {
		return this.mgetByHomeworksAndStudentId(Lists.newArrayList(homeworkId), studentId).get(homeworkId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<StudentHomework> listByHomework(long homeworkId) {
		return studentHomeworkRepo.find("$findStudentHomework", Params.param("homeworkId", homeworkId)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, StudentHomework> mgetByHomeworksAndStudentId(Collection<Long> homeworkIds, long studentId) {
		List<StudentHomework> list = studentHomeworkRepo
				.find("$queryByStudent", Params.param("homeworkIds", homeworkIds).put("studentId", studentId)).list();
		Map<Long, StudentHomework> map = new HashMap<Long, StudentHomework>(list.size());
		for (StudentHomework studentHomework : list) {
			map.put(studentHomework.getHomeworkId(), studentHomework);
		}
		return map;
	}

	@Transactional
	@Override
	public Homework commitHomework(long homeworkId, long studentId) throws HomeworkException {
		Homework homework = homeworkService.get(homeworkId);
		if (homework == null) {
			throw new HomeworkException(HomeworkException.HOMEWORK_NOT_EXIST);
		} else if (homework.getStatus() == HomeworkStatus.PUBLISH) {
			StudentHomework studentHomework = studentHomeworkRepo
					.find("$findStudentHomework", Params.param("homeworkId", homeworkId).put("studentId", studentId))
					.get();
			if (studentHomework == null) {
				throw new UnSupportedOperationException();
			} else if (studentHomework.getStatus() == StudentHomeworkStatus.NOT_SUBMIT) {
				studentHomework.setStatus(StudentHomeworkStatus.SUBMITED);
				studentHomework.setSubmitAt(new Date());
				studentHomework.setStuSubmitAt(studentHomework.getSubmitAt());
				studentHomeworkRepo.save(studentHomework);
				// 增加作业的提交数量
				homeworkService.incrCommitCount(homeworkId);
				homework.setStudentHomeworkId(studentHomework.getId());
				return homework;
			} else if (studentHomework.getStatus() == StudentHomeworkStatus.SUBMITED) {
				// 作业已经被学生提交过了
				throw new ZuoyeException(ZuoyeException.ZUOYE_STUDENT_COMMITED);
			} else {
				throw new UnSupportedOperationException();
			}
		} else if (homework.getStatus() == HomeworkStatus.NOT_ISSUE) {
			// 正常服务有任务对截止时间的学生作业自动提交，不会出现此情况：作业状态为待批改，但是学生作业仍然未提交
			// 此处为了避免出现任务未及时调用等未知异常造成的问题，以及前台学生作业状态不正确显示的问题，对学生作业进行处理
			StudentHomework studentHomework = studentHomeworkRepo
					.find("$findStudentHomework", Params.param("homeworkId", homeworkId).put("studentId", studentId))
					.get();
			studentHomework.setStatus(StudentHomeworkStatus.SUBMITED);
			studentHomework.setSubmitAt(homework.getDeadline());
			studentHomework.setStuSubmitAt(studentHomework.getSubmitAt());
			studentHomeworkRepo.save(studentHomework);
			// 增加作业的提交数量
			homeworkService.incrCommitCount(homeworkId);
			homework.setStudentHomeworkId(studentHomework.getId());
			if (System.currentTimeMillis() - homework.getDeadline().getTime() > 1 * 60 * 1000) {
				logger.error("[commitHomework error] 作业状态为NOT_ISSUE，但学生作业状态为NOT_SUBMIT，超时1分钟，请检查自动提交任务是否正常运行! "
						+ "studentHomeworkId = {}", studentHomework.getId());
			}
			return homework;
			// throw new UnSupportedOperationException();
		} else if (homework.getStatus() == HomeworkStatus.ISSUED) {
			StudentHomework studentHomework = studentHomeworkRepo
					.find("$findStudentHomework", Params.param("homeworkId", homeworkId).put("studentId", studentId))
					.get();
			if (studentHomework.getStuSubmitAt() == null) {// 被提前下发
				throw new ZuoyeException(ZuoyeException.ZUOYE_ISSUED_INADVANCE);
			} else {
				throw new ZuoyeException(ZuoyeException.ZUOYE_ISSUED);
			}
		} else {
			throw new UnSupportedOperationException();
		}
	}

	/**
	 * @param homework
	 * @param studentIds
	 * @param createAt
	 * @throws HomeworkException
	 */
	@Transactional
	@Override
	public void publishHomework(Homework homework, Set<Long> studentIds, Date createAt) throws HomeworkException {
		publishHomeworkWithDelStatus(homework, studentIds, createAt, null);
	}

	/**
	 * @since 小优快批，2018-2-27，订正题不再通过布置作业时处理，同时去除子题的搜索，不支持万年不用的复合题
	 * 
	 * @param homework
	 * @param studentIds
	 * @param createAt
	 * @param delStatus
	 * @throws HomeworkException
	 */
	@Transactional
	@Override
	public void publishHomeworkWithDelStatus(Homework homework, Set<Long> studentIds, Date createAt, Status delStatus)
			throws HomeworkException {
		if (CollectionUtils.isNotEmpty(studentIds)) {
			List<Long> questionIds = hqService.getQuestion(homework.getId());
			List<Question> qs = questionService.mgetList(questionIds);

			for (Long studentId : studentIds) {
				StudentHomework p = new StudentHomework();
				p.setCreateAt(new Date());
				p.setHomeworkId(homework.getId());
				p.setStatus(StudentHomeworkStatus.NOT_SUBMIT);
				p.setStudentId(studentId);
				// 如果传入的删除状态不为空，设置
				if (delStatus != null) {
					p.setDelStatus(delStatus);
				}
				p.setStudentCorrected(homework.getCorrectingType() == HomeworkCorrectingType.TEACHER);
				studentHomeworkRepo.save(p);
				boolean autoManualAllCorrected = true;
				for (Question question : qs) {
					StudentHomeworkQuestion shq = shqService.create(p.getId(), question.getId(), false, false,
							question.getType());
					if (question.getAnswerNumber() != null && question.getAnswerNumber() > 0) {
						for (int i = 1; i <= question.getAnswerNumber(); i++) {
							shaService.create(shq.getId(), i);
						}
					}
					if (autoManualAllCorrected && question.getType() != Type.QUESTION_ANSWERING) {
						autoManualAllCorrected = false;
					}
				}
				if (autoManualAllCorrected) {// 一个学生的一份作业全部为简答题的话,将auto_manual_all_corrected设置为true
					p.setAutoManualAllCorrected(autoManualAllCorrected);
					studentHomeworkRepo.save(p);
				}
			}
		}
	}

	@Transactional
	@Override
	public void issue(long homeworkId) throws HomeworkException {
		studentHomeworkRepo.execute("$issue", Params.param("issueAt", new Date()).put("homeworkId", homeworkId));
	}

	@Transactional(readOnly = true)
	@Override
	public CursorPage<Long, StudentHomework> query(long courseId, long studentId, StudentHomeworkStatus status,
			String keyword, CursorPageable<Long> cpr) {
		Params params = Params.param("courseId", courseId).put("studentId", studentId);
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", "%" + keyword + "%");
		}
		if (status != null) {
			params.put("status", status.getValue());
		}
		return studentHomeworkRepo.find("$query", params).fetch(cpr);
	}

	@Override
	public CursorPage<Long, StudentHomework> query(long studentId, Integer commitCount, StudentHomeworkStatus status,
			CursorPageable<Long> cpr) {
		Params params = Params.param("studentId", studentId);
		if (status != null) {
			params.put("status", status.getValue());
		}
		if (commitCount != null && commitCount > 0) {
			params.put("commitCount", commitCount);
		}

		return studentHomeworkRepo.find("$queryHk", params).fetch(cpr);
	}

	@Override
	public List<StudentHomework> mget(Collection<Long> ids) {
		return studentHomeworkRepo.mgetList(ids);
	}

	@Override
	public Map<Long, StudentHomework> mgetMap(Collection<Long> ids) {
		return studentHomeworkRepo.mget(ids);
	}

	@Override
	public List<StudentHomework> listByHomeworkOrderByJoinAt(long homeworkId, long classId) {
		return studentHomeworkRepo
				.find("$listByHomeworkOrderByJoinAt", Params.param("homeworkId", homeworkId).put("classId", classId))
				.list();
	}

	@Override
	public List<StudentHomework> listByHomework(long homeworkId, BigDecimal rightRate, BigDecimal leftRate,
			Integer timeLimit, StudentHomeworkStatus status, String orderby, String name) {
		Params params = Params.param();
		params.put("homeworkId", homeworkId);
		if (rightRate != null) {
			params.put("rightRate", rightRate);
		}
		if (leftRate != null) {
			params.put("leftRate", leftRate);
		}
		if (timeLimit != null) {
			params.put("timeLimit", timeLimit);
		}
		if (status != null) {
			params.put("status", status);
		}
		if (name != null) {
			params.put("name", name + "%");
		}
		if (orderby == null) {
			params.put("def", "def");
		} else {
			if ("RATE".equals(orderby)) {
				params.put("rate", "rate");
			} else if ("TIME".equals(orderby)) {
				params.put("timeby", "timeby");
			}
		}

		return studentHomeworkRepo.find("$findStudentHomeworkByFilter", params).list();
	}

	@Override
	public long countCommit(long homeworkId) {
		return studentHomeworkRepo.find("$countCommit", Params.param("homeworkId", homeworkId)).count();
	}

	@Override
	public List<StudentHomework> findSubmitedStuHomeworks(long homeworkId, long classId) {
		return studentHomeworkRepo
				.find("$findSubmitedStuHomeworks", Params.param("homeworkId", homeworkId).put("classId", classId))
				.list();
	}
}
