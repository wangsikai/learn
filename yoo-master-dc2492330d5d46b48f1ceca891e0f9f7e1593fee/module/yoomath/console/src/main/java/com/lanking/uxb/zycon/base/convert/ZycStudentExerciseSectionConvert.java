package com.lanking.uxb.zycon.base.convert;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseSection;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.zycon.base.api.ZycStudentExerciseSectionService;
import com.lanking.uxb.zycon.base.value.CStudentExerciseSection;

/**
 * Section -> CStudentExerciseSection
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月8日 下午3:27:38
 */
@Component
public class ZycStudentExerciseSectionConvert extends Converter<CStudentExerciseSection, VSection, Long> {
	private static final int MIN_DO_COUNT = 20;
	@Autowired
	private ZycStudentExerciseSectionService studentExerciseSectionService;

	@Override
	protected Long getId(VSection section) {
		return section.getCode();
	}

	@Override
	protected CStudentExerciseSection convert(VSection section) {
		CStudentExerciseSection v = new CStudentExerciseSection();
		v.setSection(section);
		return v;
	}

	private void internalAssemblyTree(List<CStudentExerciseSection> dest, CStudentExerciseSection v) {
		if (v.getSection().getLevel() == 1) {
			dest.add(v);
		} else {
			for (CStudentExerciseSection f : dest) {
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
	 *            {@link CStudentExerciseSection}
	 */
	private void calculateComplete(CStudentExerciseSection v) {
		List<CStudentExerciseSection> children = v.getChildren();
		if (children.size() > 0) {
			double totalComplete = 0d;
			long totalDoCount = 0L;
			long totalWrongCount = 0L;
			for (CStudentExerciseSection c : children) {
				calculateComplete(c);
				totalComplete += c.getLastMonthcomplete();
				totalDoCount += c.getLastdoCount() == null ? 0 : c.getLastdoCount();
				totalWrongCount += c.getLastWrongCount() == null ? 0 : c.getLastWrongCount();
			}
			v.setDoCount(totalDoCount);
			v.setWrongCount(totalWrongCount);
			v.setComplete(totalComplete / children.size());
			v.setCompleteTitle(Math.round(v.getComplete()) + "%");
		}
	}

	/**
	 * 批量计算正确率
	 *
	 * @param vs
	 *            {@link CStudentExerciseSection}
	 */
	private void calculateComplete(List<CStudentExerciseSection> vs) {
		for (CStudentExerciseSection v : vs) {
			calculateComplete(v);
		}
	}

	/**
	 * 根据studentId进行组装
	 *
	 * @param vs
	 *            {@link CStudentExerciseSection}
	 * @param studentId
	 *            学生id
	 */
	public void statisticsBeforeAssembleTree(List<CStudentExerciseSection> vs, long studentId, Long lastMonth) {
		List<Long> sectionCodes = Lists.newArrayList();
		for (CStudentExerciseSection v : vs) {
			sectionCodes.add(v.getSection().getCode());
		}
		Map<Long, StudentExerciseSection> map = studentExerciseSectionService.mgetBySection(studentId, sectionCodes,
				lastMonth);
		for (CStudentExerciseSection v : vs) {
			StudentExerciseSection s = map.get(v.getSection().getCode());
			if (s != null) {
				v.setDoCount(s.getDoCount());
				v.setWrongCount(s.getWrongCount());
				v.setLastdoCount(s.getLastMonthDoCount());
				v.setLastWrongCount(s.getLastMonthWrongCount());
				v.setId(s.getId());
				// 做过的题数量>20
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
				v.setLastdoCount(0L);
				v.setLastWrongCount(0L);
				v.setLastMonthcomplete(0);
			}
		}
	}

	/**
	 * 根据班级id得到掌握情况，是计算这个班级下的所有学生 并且这个章节下的所有子级掌握情况平均值
	 *
	 * @param vs
	 *            {@link CStudentExerciseSection}
	 * @param classId
	 *            班级id
	 * @return
	 */
	public List<CStudentExerciseSection> statisticsByClassId(List<CStudentExerciseSection> vs, long classId,
			Long lastMonth) {
		for (CStudentExerciseSection v : vs) {
			List<StudentExerciseSection> ret = studentExerciseSectionService.findByClassIdAndSectionCode(classId, v
					.getSection().getCode(), lastMonth);
			long totalComplete = 0;
			for (StudentExerciseSection ses : ret) {
				long complete = 0;
				if (lastMonth == null) {
					if (ses.getLastMonthDoCount() > MIN_DO_COUNT) {
						complete = Math.round(((ses.getLastMonthDoCount() - ses.getLastMonthWrongCount()) * 100)
								/ ses.getLastMonthDoCount());
					} else {
						complete = Math.round(((ses.getLastMonthDoCount() - ses.getLastMonthWrongCount()) * 100)
								/ MIN_DO_COUNT);
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
		return vs;
	}

	/**
	 * 组装树，并计算本级所有掌握的情况
	 *
	 * @param vs
	 *            VStudentExerciseSection列表
	 * @return {@link CStudentExerciseSection}
	 */
	public List<CStudentExerciseSection> assembleTree(List<CStudentExerciseSection> vs) {
		List<CStudentExerciseSection> dest = Lists.newArrayList();
		for (CStudentExerciseSection v : vs) {
			internalAssemblyTree(dest, v);
		}
		calculateComplete(dest);
		return dest;
	}
}
