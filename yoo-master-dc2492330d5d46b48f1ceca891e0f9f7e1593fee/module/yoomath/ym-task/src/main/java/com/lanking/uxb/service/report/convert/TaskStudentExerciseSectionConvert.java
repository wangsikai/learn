package com.lanking.uxb.service.report.convert;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseSection;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.report.api.TaskClassReportBaseService;
import com.lanking.uxb.service.report.value.VStudentExerciseSection;

/**
 * 学生章节练习
 * 
 * @since 2.6.0
 * @author wangsenhao
 *
 */
@Component
public class TaskStudentExerciseSectionConvert extends Converter<VStudentExerciseSection, VSection, Long> {
	private static final int MIN_DO_COUNT = 20;
	@Autowired
	private TaskClassReportBaseService studentExerciseSectionService;

	@Override
	protected Long getId(VSection section) {
		return section.getCode();
	}

	@Override
	protected VStudentExerciseSection convert(VSection section) {
		VStudentExerciseSection v = new VStudentExerciseSection();
		v.setSection(section);
		return v;
	}

	private void internalAssemblyTree(List<VStudentExerciseSection> dest, VStudentExerciseSection v) {
		if (v.getSection().getLevel() == 1) {
			dest.add(v);
		} else {
			for (VStudentExerciseSection f : dest) {
				if (f.getSection().getCode() == v.getSection().getPcode()) {
					f.getChildren().add(v);
					break;
				} else {
					internalAssemblyTree(f.getChildren(), v);
				}
			}
		}
	}

	/**
	 * 计算正确率
	 *
	 * @param v
	 *            {@link VStudentExerciseSection}
	 */
	private void calculateComplete(VStudentExerciseSection v) {
		List<VStudentExerciseSection> children = v.getChildren();
		if (children.size() > 0) {
			double totalComplete = 0d;
			long totalDoCount = 0L;
			long totalWrongCount = 0L;
			for (VStudentExerciseSection c : children) {
				calculateComplete(c);
				totalComplete += c.getComplete();
				totalDoCount += c.getDoCount();
				totalWrongCount += c.getWrongCount();
			}
			v.setDoCount(totalDoCount);
			v.setWrongCount(totalWrongCount);
			v.setComplete(totalComplete / children.size());
			v.setCompleteTitle(Math.round(v.getComplete()) + "%");
		}
		// 上个月相关
		if (children.size() > 0) {
			double totalComplete = 0d;
			long totalDoCount = 0L;
			long totalWrongCount = 0L;
			for (VStudentExerciseSection c : children) {
				calculateComplete(c);
				totalComplete += c.getLastMonthcomplete();
				totalDoCount += c.getLastdoCount() == null ? 0 : c.getLastdoCount();
				totalWrongCount += c.getLastWrongCount() == null ? 0 : c.getLastWrongCount();
			}
			v.setLastdoCount(totalDoCount);
			v.setLastWrongCount(totalWrongCount);
			v.setLastMonthcomplete(totalComplete / children.size());
		}
	}

	/**
	 * 批量计算正确率
	 *
	 * @param vs
	 *            {@link VStudentExerciseSection}
	 */
	private void calculateComplete(List<VStudentExerciseSection> vs) {
		for (VStudentExerciseSection v : vs) {
			calculateComplete(v);
		}
	}

	/**
	 * 根据班级id得到掌握情况，是计算这个班级下的所有学生 并且这个章节下的所有子级掌握情况平均值
	 *
	 * @param vs
	 *            {@link VStudentExerciseSection}
	 * @param classId
	 *            班级id
	 */
	public void statisticsByClassId(List<VStudentExerciseSection> vs, long classId, Long lastMonth) {
		for (VStudentExerciseSection v : vs) {
			List<StudentExerciseSection> ret = studentExerciseSectionService.findByClassIdAndSectionCode(classId, v
					.getSection().getCode(), lastMonth);
			long totalComplete = 0;
			for (StudentExerciseSection ses : ret) {
				long complete = 0;
				if (lastMonth == null) {
					if (ses.getDoCount() > MIN_DO_COUNT) {
						complete = ((ses.getDoCount() - ses.getWrongCount()) * 100) / ses.getDoCount();
					} else {
						complete = ((ses.getDoCount() - ses.getWrongCount()) * 100) / MIN_DO_COUNT;
					}
				} else {
					if (ses.getLastMonth() == lastMonth) {
						if (ses.getLastMonthDoCount() > MIN_DO_COUNT) {
							complete = ((ses.getLastMonthDoCount() - ses.getLastMonthWrongCount()) * 100)
									/ ses.getLastMonthDoCount();
						} else {
							complete = ((ses.getLastMonthDoCount() - ses.getLastMonthWrongCount()) * 100)
									/ MIN_DO_COUNT;
						}
					} else {
						if (ses.getCurMonthDoCount() > MIN_DO_COUNT) {
							complete = ((ses.getCurMonthDoCount() - ses.getCurMonthWrongCount()) * 100)
									/ ses.getCurMonthDoCount();
						} else {
							complete = ((ses.getCurMonthDoCount() - ses.getCurMonthWrongCount()) * 100) / MIN_DO_COUNT;
						}
					}
				}

				totalComplete += complete;
			}

			if (ret.size() > 0) {
				totalComplete = totalComplete / ret.size();
				v.setComplete(totalComplete);
				if (totalComplete != 0) {
					v.setCompleteTitle(Math.round(totalComplete) + "%");
				}
			}
		}
	}

	/**
	 * 组装树，并计算本级所有掌握的情况
	 *
	 * @param vs
	 *            VStudentExerciseSection列表
	 * @return {@link VStudentExerciseSection}
	 */
	public List<VStudentExerciseSection> assembleTree(List<VStudentExerciseSection> vs) {
		List<VStudentExerciseSection> dest = Lists.newArrayList();
		for (VStudentExerciseSection v : vs) {
			internalAssemblyTree(dest, v);
		}
		calculateComplete(dest);
		return dest;
	}

	/**
	 * 根据studentId进行组装
	 *
	 * @param vs
	 *            {@link VStudentExerciseSection}
	 * @param studentId
	 *            学生id
	 */
	public void statisticsBeforeAssembleTree(List<VStudentExerciseSection> vs, long studentId, Long lastMonth) {
		List<Long> sectionCodes = Lists.newArrayList();
		for (VStudentExerciseSection v : vs) {
			sectionCodes.add(v.getSection().getCode());
		}
		Map<Long, StudentExerciseSection> map = studentExerciseSectionService.mgetBySection(studentId, sectionCodes,
				lastMonth);
		for (VStudentExerciseSection v : vs) {
			StudentExerciseSection s = map.get(v.getSection().getCode());
			if (s != null) {
				v.setDoCount(s.getDoCount());
				v.setWrongCount(s.getWrongCount());
				v.setId(s.getId());
				long complete;
				if (v.getDoCount() > MIN_DO_COUNT) {
					complete = ((v.getDoCount() - v.getWrongCount()) * 100) / v.getDoCount();
				} else {
					complete = ((v.getDoCount() - v.getWrongCount()) * 100) / MIN_DO_COUNT;
				}
				long lastComplete;
				if (v.getLastdoCount() > MIN_DO_COUNT) {
					lastComplete = ((v.getLastdoCount() - v.getLastWrongCount()) * 100) / v.getLastdoCount();
				} else {
					lastComplete = ((v.getLastdoCount() - v.getLastWrongCount()) * 100) / MIN_DO_COUNT;
				}
				v.setLastMonthcomplete(lastComplete);
				v.setComplete(complete);
				v.setCompleteTitle(Math.round(v.getComplete()) + "%");
			} else {
				v.setDoCount(0);
				v.setWrongCount(0);
				v.setComplete(0d);
			}
		}
	}
}
