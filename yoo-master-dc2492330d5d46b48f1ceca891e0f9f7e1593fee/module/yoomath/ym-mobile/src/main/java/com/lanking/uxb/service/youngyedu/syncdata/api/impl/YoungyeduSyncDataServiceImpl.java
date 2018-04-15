package com.lanking.uxb.service.youngyedu.syncdata.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.ClazzFrom;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.youngyedu.syncdata.api.YoungyeduSyncDataService;
import com.lanking.uxb.service.youngyedu.syncdata.form.YoungyeduClass;
import com.lanking.uxb.service.youngyedu.syncdata.form.YoungyeduUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
@Service
@Transactional(readOnly = true)
public class YoungyeduSyncDataServiceImpl implements YoungyeduSyncDataService {

	@Autowired
	@Qualifier("AccountRepo")
	private Repo<Account, Long> accountRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	private Repo<Teacher, Long> teacherRepo;

	@Autowired
	@Qualifier("StudentRepo")
	private Repo<Student, Long> studentRepo;

	@Autowired
	@Qualifier("UserRepo")
	private Repo<User, Long> userRepo;

	@Autowired
	private CredentialService credentialService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkStudentClazzService studentClazzService;
	@Autowired
	private ZyHomeworkStatisticService homeworkStatisticService;

	@Override
	@Transactional
	public void sync(List<YoungyeduUser> users, int code) {
		for (YoungyeduUser u : users) {
			User user = createUser(u, code);
			createClass(u.getClazz(), user);
		}
	}

	@Override
	@Transactional
	public User createUser(YoungyeduUser user, int code) {
		if (user == null) {
			return null;
		}

		Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, CredentialType.YOUNGY_EDU,
				user.getUserId());

		// 表明此用户之前并没有与系统进行关联,现进行关联
		if (credential == null) {
			RegisterForm form = new RegisterForm();
			form.setRealName(user.getRealName());
			// 临时处理登录名
			form.setName("yy" + user.getName());
			form.setNickname(user.getName());
			form.setCredentialType(CredentialType.YOUNGY_EDU);
			if (user.getUserType() == 1) {
				form.setType(UserType.TEACHER);
			} else {
				form.setType(UserType.STUDENT);
			}
			if (user.getPhase() == 2) {
				form.setSubjectCode(202);
			} else {
				form.setSubjectCode(302);
			}

			form.setPhaseCode(user.getPhase());
			form.setThirdName(user.getName());
			form.setSource(Product.YOOMATH);
			form.setChannelCode(code);

			Account account = saveAccount(form);
			saveUser(account, form);

			credential = new Credential();
			credential.setAccountId(account.getId());
			credential.setCreateAt(new Date());
			credential.setUid(user.getUserId());
			credential.setName(form.getThirdName());
			credential.setProduct(Product.YOOMATH);
			credential.setType(CredentialType.YOUNGY_EDU);
			credential.setUpdateAt(new Date());

			User saveUser = accountService.getUserByAccountId(account.getId());
			credential.setUserId(saveUser.getId());

			credentialService.save(credential);

			return saveUser;
		}

		return accountService.getUserByAccountId(credential.getAccountId());
	}

	@Override
	@Transactional
	public void createClass(YoungyeduClass clazz, User user) {
		List<String> codes = new ArrayList<String>(1);
		codes.add(clazz.getId());
		Map<String, List<HomeworkClazz>> clazzMap = homeworkClassService.findTeaUsedByFromCode(ClazzFrom.YOUNGYEDU,
				codes);

		// 是教师
		if (user.getUserType() == UserType.TEACHER) {
			if (CollectionUtils.isEmpty(clazzMap.get(clazz.getId()))) {
				homeworkClassService.createByThird(clazz.getName(), user.getId(), ClazzFrom.YOUNGYEDU, clazz.getId());
			}
		} else {
			// 是学生，则要判断这个学生所在班级是否已经在系统中生成了，如果已经生成了则加入这个班级
			if (CollectionUtils.isNotEmpty(clazzMap.get(clazz.getId()))) {
				HomeworkClazz c = clazzMap.get(clazz.getId()).get(0);

				// 学生若之前加入过此班级
				if (!studentClazzService.isJoin(c.getId(), user.getId())) {
					studentClazzService.join(c.getId(), user.getId());

					// 学生加入班级，需要重新计算班级整体统计
					homeworkStatisticService.updateTeacherHomeworkStatByClass(c.getId());
				}
			}
		}
	}

	@Transactional
	private Account saveAccount(RegisterForm form) {
		// 用户名已经存在
		Account findAccount = accountService.getAccount(GetType.NAME, form.getName());

		if (findAccount != null) {
			throw new AccountException(AccountException.ACCOUNT_NAME_EXIST);
		}

		Account account = new Account();
		account.setStatus(Status.ENABLED);
		account.setName(form.getName());
		account.setPasswordStatus(PasswordStatus.DISABLED);
		account.setPassword(Codecs.md5Hex(String.valueOf(form.getPassword())));
		account.setRegisterAt(new Date());

		return accountRepo.save(account);
	}

	@Transactional
	private void saveUser(Account account, RegisterForm form) {

		User user = new User();
		user.setAccountId(account.getId());
		user.setUserType(form.getType());
		user.setName(form.getRealName());
		user.setNickname(form.getNickname());
		if (form.getChannelCode() != null) {
			user.setUserChannelCode(form.getChannelCode());
		}

		if (form.getType() == UserType.STUDENT) {
			Student student = new Student();
			student.setNickname(user.getNickname());
			student.setName(user.getName());
			student.setSex(form.getSex());
			student.setCreateAt(new Date());
			student.setUpdateAt(student.getUpdateAt());
			studentRepo.save(student);
			user.setId(student.getId());
			userRepo.save(user);
		} else if (form.getType() == UserType.TEACHER) {
			Teacher teacher = new Teacher();
			teacher.setName(user.getName());
			teacher.setNickname(user.getNickname());
			teacher.setSex(form.getSex());
			teacher.setCreateAt(new Date());
			teacher.setUpdateAt(teacher.getCreateAt());
			teacher.setPhaseCode(form.getPhaseCode());
			teacher.setSubjectCode(form.getSubjectCode());
			teacherRepo.save(teacher);
			user.setId(teacher.getId());
			userRepo.save(user);
		}

	}

}
