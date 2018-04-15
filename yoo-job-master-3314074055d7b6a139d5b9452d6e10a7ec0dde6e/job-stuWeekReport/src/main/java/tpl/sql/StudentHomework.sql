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
