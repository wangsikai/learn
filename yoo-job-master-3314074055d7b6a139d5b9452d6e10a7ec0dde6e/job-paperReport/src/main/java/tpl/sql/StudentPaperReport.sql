##获取班级章节统计
#macro($getSectionMapByClassId(classId,startDate,endDate,textbookCode))
	select COALESCE(SUM(a.right_count),0) right_count,COALESCE(SUM(a.wrong_count),0) wrong_count,LEFT(c.section_code,10) section_code 
   from homework_question a
   inner join question_knowledge b on a.question_id = b.question_id
   inner join knowledge_section c on b.knowledge_code = c.knowledge_code AND c.section_code like :textbookCode
   INNER JOIN section d ON c.section_code = d.code AND d.name != '本章综合与测试'
   inner join homework h on a.homework_id = h.id
   where h.homework_class_id = :classId and h.status = 3 AND h.del_status = 0
   	and h.issue_at >= :startDate
    and h.issue_at < :endDate
   GROUP BY left(c.section_code,10)
#end

##获取班级知识点统计
#macro($getKpMapByClassId(classId,startDate,endDate,textbookCode))
	SELECT COUNT(a.id) q_count,SUM(a.right_count) right_count,SUM(a.wrong_count) wrong_count,SUM(a.half_wrong_count) half_wrong_count,
	b.knowledge_code,AVG(qq.difficulty) difficulty
   FROM homework_question a
   INNER JOIN question_knowledge b ON a.question_id = b.question_id
   INNER JOIN knowledge_section c ON b.knowledge_code = c.knowledge_code AND c.section_code LIKE :textbookCode
   INNER JOIN section d ON c.section_code = d.code AND d.name != '本章综合与测试'
   INNER JOIN homework h ON a.homework_id = h.id
   INNER JOIN question qq ON qq.id = a.question_id
   WHERE h.homework_class_id = :classId AND h.status = 3 AND h.del_status = 0
   	AND h.issue_at >= :startDate
    AND h.issue_at < :endDate
   AND (a.right_count  > 0 OR a.wrong_count  > 0)
   GROUP BY b.knowledge_code
#end


##获取学生章节统计
#macro($getSectionMapByStuId(studentId,startDate,endDate,textbookCode))
	SELECT LEFT(d.section_code,10) section_code,b.result,b.question_id,d.knowledge_code FROM student_homework a
	INNER JOIN student_homework_question b ON a.id = b.student_homework_id
	AND a.student_id = :studentId AND a.STATUS = 2 AND a.right_rate IS NOT NULL
	AND a.issue_at >= :startDate AND a.issue_at < :endDate AND a.del_status = 0
	INNER JOIN question_knowledge c ON b.question_id = c.question_id
	INNER JOIN knowledge_section d ON d.knowledge_code = c.knowledge_code AND d.section_code LIKE :textbookCode
	INNER JOIN section e ON d.section_code = e.code AND e.name != '本章综合与测试'
	ORDER BY LEFT(d.section_code,10) ASC
#end


##获取学生完成率和正确率相关统计
#macro($getStuStat(classId,startDate,endDate))
	  SELECT t.completion_rate ,t.right_rate,t.student_id FROM
		(
			SELECT ROUND(AVG(a.completion_rate)) completion_rate,ROUND(AVG(a.right_rate)) right_rate,a.student_id
			FROM student_homework a
			INNER JOIN homework b ON a.homework_id = b.id
			WHERE b.homework_class_id = :classId  AND a.status = 2 AND a.issue_at >= :startDate AND a.issue_at < :endDate 
			AND a.del_status = 0 and a.right_rate is not null
			GROUP BY a.student_id
		) t
	 	 
	ORDER BY t.right_rate DESC,t.completion_rate DESC
#end

##获取班级完成率和正确率相关统计
#macro($getClazzStat(classId,startDate,endDate))
	 select ROUND(AVG(a.completion_rate)) completion_rate,ROUND(AVG(a.right_rate)) right_rate
	from student_homework a 
	inner join homework b on a.homework_id = b.id
	where a.del_status = 0 and a.status = 2 and a.issue_at >= :startDate and a.issue_at < :endDate
	 and b.homework_class_id = :classId
#end


##获取学生小章节统计
#macro($getSmallSectionMapByStuId(studentId,startDate,endDate,textbookCode))
	SELECT LEFT(d.section_code,12) section_code,b.result,b.question_id FROM student_homework a
	INNER JOIN student_homework_question b ON a.id = b.student_homework_id
	AND a.student_id = :studentId AND a.STATUS = 2 AND a.right_rate IS NOT NULL
	AND a.issue_at >= :startDate AND a.issue_at < :endDate AND a.del_status = 0
	INNER JOIN question_knowledge c ON b.question_id = c.question_id
	INNER JOIN knowledge_section d ON d.knowledge_code = c.knowledge_code AND d.section_code LIKE :textbookCode
	INNER JOIN section e ON d.section_code = e.code AND e.name != '本章综合与测试'
	ORDER BY LEFT(d.section_code,12) ASC
#end

##获取学生报告数据
#macro($findByRecord(recordId))
select t.* from student_paper_report t where t.record_id=:recordId
#end


##删除班级时间段内的数据
#macro($deleteReport(classId,startDate,endDate))
	 delete from student_paper_report 
	 where class_id = :classId and start_date = :startDate and end_date = :endDate
#end


##获取班级作业情况列表
#macro($findClazzHkList(classId,startDate,endDate))
	select round(avg(right_rate)) right_rate from homework
	where homework_class_id = :classId and del_status = 0 and status = 3 
	and issue_at >= :startDate and issue_at < :endDate and right_rate is not null
#end

##获取班级作业情况列表2(取布置次数、题目数)
#macro($findClazzHkMap2(classId,startDate,endDate))
	select sum(question_count) question_count,count(id) count0 from homework
	where homework_class_id = :classId and del_status = 0 
	and start_time >= :startDate and start_time < :endDate 
#end

##获取学生完成率
#macro($findStuHkMap(classId,studentId,startDate,endDate))
	 select ROUND(AVG(a.completion_rate)) completion_rate,round(avg(a.right_rate)) right_rate
	from student_homework a 
	inner join homework b on a.homework_id = b.id
	where a.del_status = 0 and a.status = 2 and b.issue_at >= :startDate 
	and b.issue_at < :endDate and a.right_rate is not null and a.stu_submit_at is not null
	 and b.homework_class_id = :classId and a.student_id = :studentId
#end

##获取学生完成率
#macro($findStuHkMap2(classId,studentId,startDate,endDate))
	 select count(a.id) count0,sum(a.right_count + a.wrong_count) question_count
	from student_homework a 
	inner join homework b on a.homework_id = b.id
	where a.del_status = 0 and b.start_time >= :startDate and a.stu_submit_at is not null
	and b.start_time < :endDate
	 and b.homework_class_id = :classId and a.student_id = :studentId
#end

##获取学生作业列表
#macro($findLastHkList(classId,studentId,startDate,endDate))
select * from (
	select a.right_rate class_rate,b.right_rate stu_rate,a.id,a.name,a.start_time from homework a 
	inner join student_homework b on a.id = b.homework_id
	and b.student_id = :studentId and a.homework_class_id = :classId
	and a.del_status = 0 and b.status = 2 and b.issue_at >= :startDate 
	and b.issue_at < :endDate and a.right_rate is not null and b.right_rate is not null 
	order by a.start_time desc
	limit 0,10
) t order by t.start_time asc
	
#end

##获取班级自主练习数
#macro($clazzSelfCount(classId,startDate,endDate))
select count(1) from student_question_answer a inner join homework_student_class b
	on a.student_id = b.student_id and b.status = 0 and b.class_id = :classId
	and a.create_at >= :startDate and a.create_at < :endDate
#end

##获取学生自主练习数
#macro($stuSelfCount(studentId,startDate,endDate))
select count(1) from student_question_answer where student_id = :studentId
and create_at >= :startDate and create_at < :endDate
#end