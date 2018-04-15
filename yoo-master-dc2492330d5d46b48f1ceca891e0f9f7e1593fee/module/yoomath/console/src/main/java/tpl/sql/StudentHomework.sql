##根据homeworkid查找studenthomework
#macro($zycFindByHomework(hkId))
SELECT * FROM student_homework t WHERE t.homework_id = :hkId
#end

##获取学生时间范围内的作业情况
#macro($zycGetStuStatRankByClass(beginTime,endTime,clazzId,stuIds))
SELECT sh.student_id,AVG(sh.right_rate) AS rightRate FROM student_homework  sh INNER JOIN homework  hk ON sh.homework_id=hk.id  AND hk.homework_class_id =:clazzId
WHERE sh.create_at>:beginTime AND sh.create_at<:endTime AND sh.issue_at>:beginTime AND sh.issue_at<:endTime  AND sh.student_id IN (:stuIds) 
GROUP BY sh.student_id ORDER BY AVG(sh.right_rate) DESC
#end

##获取对应年月的学生作业（在该月布置并下发的作业）
#macro(zycListByTime(beginTime,endTime,studentId,clazzId))
SELECT sh.* FROM student_homework sh INNER JOIN homework h ON sh.homework_id = h.id
AND h.homework_class_id =:clazzId
WHERE sh.student_id=:studentId AND sh.create_at>:beginTime AND sh.create_at<:endTime AND sh.issue_at>:beginTime AND sh.issue_at<:endTime
#end

## 临时跑学生周报告要用，后续删除
##获取学生一段时间内作业列表,只查询已下发
#macro($findByUserId(userId,startDate,endDate))
	select a.* from student_homework a 
	inner join homework b on a.homework_id = b.id
	where a.issue_at >= :startDate and a.issue_at < :endDate and a.student_id = :userId
	and a.issue_at is not null and a.right_rate is not null
	and a.del_status = 0
	order by b.start_time
#end


##获取学生指定时间的统计
#macro($getStatByUser(userId,startDate,endDate))
	select round(avg(completion_rate)) completion_rate,round(avg(right_rate)) right_rate
	from student_homework
	where student_id = :userId and issue_at >= :startDate and issue_at < :endDate
	and del_status = 0
#end


#macro($stuClassWeekReportList(classId,startDate,endDate))
	SELECT t.right_rate,t.student_id FROM
	(
		SELECT ROUND(AVG(a.right_rate)) right_rate,a.student_id
		FROM student_homework a
		INNER JOIN homework b ON a.homework_id = b.id
		WHERE b.homework_class_id = :classId
		and b.right_rate is not null
		and a.issue_at >= :startDate and a.issue_at < :endDate
		and a.del_status = 0
		GROUP BY a.student_id
	) t
	ORDER BY t.right_rate DESC
#end

#macro($getSectionAnalysis(studentId,startDate,endDate,textbookCode))
	SELECT d.section_code,b.result FROM student_homework a
	INNER JOIN student_homework_question b ON a.id = b.student_homework_id
	AND a.student_id = :studentId AND a.STATUS = 1 AND a.right_rate IS NOT NULL
	AND a.issue_at >= :startDate AND a.issue_at < :endDate AND a.del_status = 0
	INNER JOIN question_knowledge c ON b.question_id = c.question_id
	INNER JOIN knowledge_section d ON d.knowledge_code = c.knowledge_code AND d.section_code LIKE :textbookCode
	INNER JOIN section e ON d.section_code = e.code AND e.name != '本章综合与测试'
	ORDER BY LEFT(d.section_code,12) ASC
#end

