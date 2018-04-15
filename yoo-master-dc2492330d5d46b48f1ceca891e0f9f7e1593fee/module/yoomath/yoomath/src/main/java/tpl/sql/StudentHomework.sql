## 查询作业列表(页码方式)
#macro($zyQuery(studentId,courseId,classId,fromCourse,fromClass,status,statuses))
SELECT a.* FROM student_homework a 
#if(fromCourse)
	INNER join homework b ON a.homework_id = b.id
#end
#if(fromClass)
	INNER join homework b ON a.homework_id = b.id
#end
WHERE a.student_id = :studentId AND a.del_status = 0
#if(courseId)
	AND b.course_id = :courseId
#end
#if(classId)
	AND b.homework_class_id = :classId
#end
#if(fromCourse)
	AND b.homework_class_id IS NULL AND b.del_status = 0
#end
#if(fromClass)
	AND b.course_id IS NULL AND b.del_status = 0
#end
#if(status)
	AND a.status = :status
#end
#if(statuses)
	AND a.status IN (:statuses)
#end
ORDER BY a.status ASC,a.id DESC
#end

## 查询作业列表(游标方式)
#macro($zyQueryCursor(studentId,courseId,classId,fromCourse,fromClass,status,statuses))
SELECT a.* FROM student_homework a 
#if(fromCourse)
	INNER join homework b ON a.homework_id = b.id
#end
#if(fromClass)
	INNER join homework b ON a.homework_id = b.id
#end
WHERE a.student_id = :studentId AND a.del_status = 0
#if(courseId)
	AND b.course_id = :courseId
#end
#if(classId)
	AND b.homework_class_id = :classId
#end
#if(fromCourse)
	AND b.homework_class_id IS NULL AND b.del_status = 0
#end
#if(fromClass)
	AND b.course_id IS NULL AND b.del_status = 0
#end
#if(status)
	AND a.status = :status
#end
#if(statuses)
	AND a.status IN (:statuses)
#end
#if(next)
	AND id < :next
#end
ORDER BY a.id DESC
#end

## 更新学生作业计时
#macro($zyUpdateHomeworkTime(id,studentId,homeworkTime,completionRate,updateAt))
UPDATE student_homework SET  homework_time = :homeworkTime, update_at = :updateAt
#if(completionRate)
,completion_rate = :completionRate
#end
WHERE id = :id AND student_id = :studentId  AND homework_time < :homeworkTime AND status = 0
#end

## 计算作业的平均用时[统计用,其他地方勿用]
#macro($zyStaticHomeworkTime(homeworkId))
SELECT avg(homework_time) homework_time FROM student_homework 
WHERE homework_id = :homeworkId AND del_status = 0 AND submit_at IS NOT NULL AND stu_submit_at IS NOT NULL
#end

## 根据正确率排序获取一次作业的所有学生作业[统计用,其他地方勿用]
#macro($zyStaticRightRateRank(homeworkId))
SELECT * FROM student_homework WHERE homework_id = :homeworkId ORDER BY right_rate DESC
#end

## 计算某门课程每个学生所有作业的平均用时[统计用,其他地方勿用]
#macro($zyStaticAvgHomeworkTimeForStuStat(classId))
SELECT 
	avg(a.homework_time) avg_time ,avg(a.right_rate) avg_rate,a.student_id 
FROM student_homework a INNER JOIN homework b ON a.homework_id = b.id 
WHERE b.status IN (2,3) AND a.status IN (1,2) AND a.del_status = 0 AND b.del_status = 0 AND a.submit_at IS NOT NULL AND a.stu_submit_at IS NOT NULL
#if(classId)
AND b.homework_class_id = :classId
#end
GROUP BY a.student_id
#end

## 计算某门课程所有作业的平均用时[统计用,其他地方勿用]
## since 教师端v1.3.0 2017-7-5 班级整体数据不再统计已退出的学生，同时数据取自学生统计
#macro($zyStaticAvgHomeworkTimeForTeaStat(classId))
SELECT AVG(stat.homework_time) avg_time,AVG(stat.right_rate) avg_rate FROM student_homework_stat stat
INNER JOIN homework_student_class sc ON sc.class_id=stat.homework_class_id AND sc.student_id=stat.user_id AND sc.status=0
WHERE stat.homework_class_id=:classId
#end

## 计算某个学生某门课程的作业数量[统计用,其他地方勿用]
#macro($zyCountHomework(studentId,classId))
SELECT count(a.id) FROM student_homework a INNER JOIN homework b ON a.homework_id = b.id
WHERE a.student_id = :studentId AND a.del_status = 0 AND b.del_status = 0
#if(classId)
AND b.homework_class_id = :classId
#end
#end

## 计算某个学生某门课程的TODO作业数量[统计用,其他地方勿用]
#macro($zyCountToDoHomework(studentId,classId))
select count(a.id) from student_homework a INNER JOIN homework b ON a.homework_id = b.id
WHERE a.student_id = :studentId AND a.del_status = 0 AND b.del_status = 0
#if(classId)
and b.homework_class_id = :classId
#end
and a.status = 0
#end

## 计算某个学生某门课程的超时未做的作业数量[统计用,其他地方勿用]
#macro($zyCountOverdueHomework(studentId,classId))
select count(a.id) from student_homework a INNER JOIN homework b ON a.homework_id = b.id
WHERE a.student_id = :studentId AND a.del_status = 0 AND b.del_status = 0
#if(classId)
and b.homework_class_id = :classId
#end
and a.stu_submit_at IS NULL
and a.status > 0
#end

## 计算某个学生某些状态的作业数量
#macro($zyCountHomeworks(studentId,status,statuses))
SELECT 
	count(id) 
FROM student_homework WHERE student_id = :studentId AND del_status = 0
#if(status)
	AND status = :status
#end
#if(statuses)
	AND status IN (:statuses)
#end
#end

##获取最近limit次作业的统计(个人数据和全班数据)
#macro($zyStatisticLatestHomework(classId,studentId,limit))
SELECT 
	t1.right_rate stu_right_rate,
	t2.right_rate class_right_rate,
	t2.name homework_name,
	t1.id student_homework_id
FROM student_homework t1 INNER JOIN homework t2 ON t1.homework_id = t2.id 
WHERE t2.status = 3 AND t1.del_status = 0 AND t2.del_status = 0
AND t2.homework_class_id = :classId 
AND t1.student_id = :studentId 
AND t1.right_rate IS NOT NULL
ORDER BY t1.id DESC LIMIT :limit
#end

##所有作业数量
#macro($getAllHkCount())
SELECT count(*) FROM student_homework 
#end

## 更新学生作业的相关统计
#macro($zyUpdateHomeworkStudentStat(hkId,studentId,rightRate,rightCount,wrongCount))
UPDATE student_homework SET right_rate = :rightRate,right_count = :rightCount,wrong_count = :wrongCount
WHERE student_id = :studentId AND homework_id = :hkId
#end

## 统计已经提交但是没有批改完的学生作业数量
#macro($zyStatSubmitNotCorrected(hkId))
SELECT count(*) FROM student_homework WHERE status = 1 AND right_rate IS NULL AND homework_id = :hkId AND stu_submit_at  IS NOT NULL AND submit_at IS NOT NULL
#end

## 查询未提交的学生
#macro($zyListNotCommitStudent(hkId))
SELECT student_id FROM student_homework WHERE status = 0 AND homework_id = :hkId
#end

##单份作业Top limit个学生
#macro($findStudentHomeworkTop(homeworkId,limit))
SELECT * FROM student_homework WHERE homework_id = :homeworkId
ORDER BY right_rate DESC
#if(limit)
	limit :limit
#end
#end

##单份作业某一排名的学生
#macro($findByRank(homeworkId,rank))
SELECT * FROM student_homework WHERE homework_id = :homeworkId and rank =:rank
#end

##查询学生作业列表，包括寒假作业
#macro($queryUnionHolidayStuHk(studentId,classId,fromCourse,fromClass,status,statuses))
SELECT hk.id,hk.type FROM 
(
	SELECT a.id,1 AS TYPE,a.create_at,a.student_id,a.status,a.del_status FROM student_homework a
	#if(fromCourse)
		INNER join homework b ON a.homework_id = b.id
	#end
	#if(fromClass)
		INNER join homework b ON a.homework_id = b.id
	#end
	WHERE a.student_id = :studentId
	#if(classId)
		AND b.homework_class_id = :classId
	#end
	#if(fromCourse)
		AND b.homework_class_id IS NULL AND b.del_status = 0
	#end
	#if(fromClass)
		AND b.course_id IS NULL AND b.del_status = 0
	#end
	UNION ALL
 	SELECT id,2 AS TYPE,create_at,student_id,status,del_status FROM holiday_stu_homework WHERE student_id = :studentId
 )
 AS hk WHERE hk.del_status = 0
	#if(status)
		AND hk.status = :status
	#end
	#if(statuses)
		AND hk.status IN (:statuses)
	#end
	ORDER BY hk.status ASC,hk.create_at DESC
#end

##获取对应年月的学生作业（在该月布置和下发的作业）
#macro(listByTime(beginTime,endTime,studentId,clazzId))
SELECT sh.* FROM student_homework sh INNER JOIN homework h ON sh.homework_id = h.id
AND h.homework_class_id =:clazzId
WHERE sh.student_id=:studentId AND sh.create_at>:beginTime AND sh.create_at<:endTime AND sh.issue_at>:beginTime AND sh.issue_at<:endTime
#end

##获取学生时间范围内的作业情况
#macro($getStuStatRankByClass(beginTime,endTime,clazzId,stuIds))
SELECT sh.student_id,AVG(sh.right_rate) AS rightRate FROM student_homework  sh INNER JOIN homework  hk ON sh.homework_id=hk.id  AND hk.homework_class_id =:clazzId
WHERE sh.create_at>:beginTime AND sh.create_at<:endTime AND sh.issue_at>:beginTime AND sh.issue_at<:endTime  AND sh.student_id IN (:stuIds) 
GROUP BY sh.student_id ORDER BY AVG(sh.right_rate) DESC
#end

##查询某个学生指定状态下的所有作业的数量
#macro($countAllHomeworks(studentId,status,statuses))
SELECT count(hk.id) FROM 
(
	SELECT a.id FROM student_homework a WHERE a.del_status = 0 AND a.student_id = :studentId
	#if(status)
		AND a.status = :status 
	#end
	#if(statuses)
		AND a.status IN(:statuses) 
	#end
	UNION ALL
 	SELECT b.id FROM holiday_stu_homework b WHERE b.del_status = 0 AND b.student_id = :studentId
 	#if(status)
		AND b.status = :status 
	#end
	#if(statuses)
		AND b.status IN(:statuses) 
	#end
) AS hk 
#end

##查询某个学生指定状态下的所有作业的数量，不包括假期作业
#macro($countAllHomeworksNoHoliday(studentId,status,statuses))
SELECT count(hk.id) FROM 
(
	SELECT a.id FROM student_homework a WHERE a.del_status = 0 AND a.student_id = :studentId
	#if(status)
		AND a.status = :status 
	#end
	#if(statuses)
		AND a.status IN(:statuses) 
	#end
) AS hk 
#end


##查询学生作业列表，包括寒假作业(游标方式id)
#macro($queryUnionHolidayStuHkByCursor(studentId,status,statuses))
SELECT hk.id,hk.type FROM 
(
	SELECT a.id , 1 AS TYPE,a.create_at,a.status FROM student_homework a WHERE a.del_status = 0 AND a.student_id = :studentId
	#if(status)
		AND a.status = :status 
	#end
	#if(statuses)
		AND a.status IN(:statuses) 
	#end
	UNION ALL
 	SELECT b.id , 2 AS TYPE,b.create_at,b.status FROM holiday_stu_homework b WHERE b.del_status = 0 AND b.student_id = :studentId
 	#if(status)
		AND b.status = :status 
	#end
	#if(statuses)
		AND b.status IN(:statuses) 
	#end
)AS hk WHERE
#if(next)
	hk.id < :next
#end
ORDER BY hk.id DESC
#end

##查询学生作业列表，包括寒假作业(游标方式startTime)
#macro($queryUnionHolidayStuHkByStartTimeCursor(studentId,clazzId,status,statuses,startTime,isMobileIndex))
SELECT hk.id,hk.type,hk.start_time FROM 
(
	SELECT a.id , 1 AS TYPE,a.create_at,a.status,c.start_time FROM student_homework a INNER JOIN homework c ON a.homework_id = c.id WHERE a.del_status = 0 AND a.student_id = :studentId
	#if(clazzId)
		AND c.homework_class_id = :clazzId
	#end
	#if(isMobileIndex == 0)
		#if(status)
			AND a.status = :status 
		#end
		#if(statuses)
			AND a.status IN(:statuses) 
		#end
	#end
	#if(isMobileIndex == 1)
		#if(status)
			AND a.status = :status 
		#end
		#if(statuses)
			AND a.status IN(:statuses) 
		#end
	#end
	UNION ALL
 	SELECT b.id , 2 AS TYPE,b.create_at,b.status,d.start_time FROM holiday_stu_homework b INNER JOIN holiday_homework d ON b.holiday_homework_id = d.id WHERE b.del_status = 0 AND b.student_id = :studentId
 	#if(clazzId)
		AND d.homework_class_id = :clazzId
	#end
 	#if(status)
		AND b.status = :status 
	#end
	#if(statuses)
		AND b.status IN(:statuses) 
	#end
)AS hk WHERE 1=1
#if(startTime)
	AND hk.start_time < :startTime
#end
ORDER BY hk.start_time DESC,id ASC
#end

##查询学生作业列表，不包括寒假作业(游标方式id)
#macro($queryStuHkByCursor(studentId,status,statuses))
SELECT hk.id,hk.type FROM 
(
	SELECT a.id , 1 AS TYPE,a.create_at,a.status FROM student_homework a WHERE a.del_status = 0 AND a.student_id = :studentId
	#if(status)
		AND a.status = :status 
	#end
	#if(statuses)
		AND a.status IN(:statuses) 
	#end
)AS hk WHERE
#if(next)
	hk.id < :next
#end
ORDER BY hk.start_time DESC,id DESC
#end

##查询学生作业列表，不包括寒假作业(游标方式startTime)
#macro($queryStuHkByStartTimeCursor(studentId,clazzId,status,statuses,startTime,isMobileIndex))
SELECT hk.id,hk.type,hk.start_time FROM 
(
	SELECT a.id , 1 AS TYPE,a.create_at,a.status,c.start_time FROM student_homework a INNER JOIN homework c ON a.homework_id = c.id WHERE a.del_status = 0 AND a.student_id = :studentId
	#if(clazzId)
		AND c.homework_class_id = :clazzId
	#end
	#if(isMobileIndex == 0)
		#if(status)
			AND a.status = :status 
		#end
		#if(statuses)
			AND a.status IN(:statuses) 
		#end
	#end
	#if(isMobileIndex == 1)
		#if(status)
			AND a.status = :status 
		#end
		#if(statuses)
			AND a.status IN(:statuses) 
		#end
	#end
)AS hk WHERE 1=1
#if(startTime)
	AND hk.start_time < :startTime
#end
ORDER BY hk.start_time DESC,id DESC
#end

## 判断一份作业里面有没有简答题被答过题
#macro($zyIsQuestionAnsweringDone(studentHomeworkId))
SELECT COUNT(*) FROM student_homework_question 
WHERE student_homework_id = :studentHomeworkId 
AND (type = 5 OR type = 3) AND answer_img IS NOT NULL AND answer_img > 0
#end

## 查找未批改的学生作业数量
#macro($zyCountNotCorrect(hkId))
SELECT count(*) FROM student_homework t
WHERE t.homework_id = :hkId
AND t.correct_status = 3 AND t.status = 1
#end

##查询学生作业列表，包括寒假作业未下发或者已经下发了但是学生还没有查看过
#macro($queryUnionHolidayStuHkNew(studentId,classId,fromCourse,fromClass,status,statuses))
SELECT hk.id,hk.type,hk.status FROM
(
	SELECT a.id,1 AS TYPE,a.create_at,a.student_id,a.status,a.viewed,a.del_status FROM student_homework a
	#if(fromCourse)
		INNER join homework b ON a.homework_id = b.id
	#end
	#if(fromClass)
		INNER join homework b ON a.homework_id = b.id
	#end
	#if(classId)
		AND b.homework_class_id = :classId
	#end
	#if(fromCourse)
		AND b.homework_class_id IS NULL AND b.del_status = 0
	#end
	#if(fromClass)
		AND b.course_id IS NULL AND b.del_status = 0
	#end
	WHERE  a.student_id=:studentId
	UNION ALL
 	SELECT id,2 AS TYPE,create_at,student_id,status,viewed,del_status FROM holiday_stu_homework  WHERE  student_id=:studentId
 )
 AS hk WHERE  (hk.status = 0 or (hk.status = 2 AND hk.viewed = 0)) and hk.del_status = 0
	#if(status)
		AND hk.status = :status
	#end
	#if(statuses)
		AND hk.status IN (:statuses)
	#end
	ORDER BY hk.status ASC,hk.type DESC,hk.create_at DESC
	LIMIT 20
#end

## 更新已经下发学生作业的查看状态
#macro($updateViewStatus(id))
UPDATE student_homework SET viewed = 1 WHERE id = :id
#end

##查询作业v2.0.3(web v2.0)
#macro($queryHomeworkWeb(studentId,status,homeworkClassIds,homeworkClassId,keys,bt,et,secname,line,statusIndex))
select sh.* FROM student_homework sh
 inner join homework t on t.id = sh.homework_id AND t.del_status = 0
 inner join exercise e on e.id = t.exercise_id
 where sh.student_id=:studentId
#if(status)
	AND t.status in (:status)
#end
#if(homeworkClassIds)
	AND t.homework_class_id in (:homeworkClassIds)
#end
#if(homeworkClassId)
	AND t.homework_class_id =:homeworkClassId
#end
#if(bt)
	AND (t.deadline > :bt)
#end
#if(et)
	AND (t.start_time < :et)
#end
#if(keys)
 AND (
	EXISTS (
		select 1 from homework_metaknow hm
		inner join meta_knowpoint mk on mk.code=hm.meta_code and REPLACE(mk.name,' ','') like :keys
		where hm.homework_id=t.id
	)
	or REPLACE(t.name,' ','') like :keys
 )
#end
#if(secname)
	AND
	EXISTS (
		select 1 from section st where st.code = e.section_code and st.name like :secname
	)
#end
#if(line == true)
	AND
	NOT EXISTS (
		select 1 from section st where st.code = e.section_code
	)
#end
#if(statusIndex)
	#if(statusIndex == 1)
		AND sh.status=2
	#end
	#if(statusIndex == 2)
		AND sh.status=0
	#end
	#if(statusIndex == 3)
		AND sh.status=1 AND sh.student_corrected = 0 AND t.status=1
	#end
	#if(statusIndex == 4)
		AND sh.status=1 AND (sh.student_corrected = 1 OR t.status=1)
	#end
#end

 ORDER BY sh.status ASC, sh.create_at DESC
#end

##获取最早发布的作业时间
#macro($getFirstStartAt(studentId, homeworkClassIds))
SELECT h1.start_time as ct FROM homework h1
 inner join student_homework sh on sh.homework_id=h1.id and sh.student_id=:studentId
	WHERE h1.del_status = 0
#if(homeworkClassIds)
	AND h1.homework_class_id in (:homeworkClassIds)
#end
	ORDER BY h1.start_time ASC LIMIT 1
#end

##查询当前学生有没有待处理的作业
#macro($countToDoHk(studentId))
SELECT SUM(count1) FROM
(
	SELECT COUNT(1) count1 FROM holiday_stu_homework WHERE student_id = :studentId AND STATUS = 0
	UNION ALL
	SELECT COUNT(1) count1 FROM student_homework WHERE student_id = :studentId AND STATUS = 0
) t
#end

##删除所有学生作业
#macro($deleteAllStuHomeworkByClazz(homeworkids))
UPDATE student_homework  SET del_status = 2 where homework_id IN (:homeworkids)
and del_status != 1
#end

##查询作业(小优快批)
#macro($queryHomeworkWeb2(studentId,homeworkClassIds,homeworkClassId,keys,bt,et,secname,line,status))
select sh.* FROM student_homework sh
 inner join homework t on t.id = sh.homework_id AND t.del_status = 0
 inner join exercise e on e.id = t.exercise_id
 where sh.student_id=:studentId
#if(homeworkClassIds)
	AND t.homework_class_id in (:homeworkClassIds)
#end
#if(homeworkClassId)
	AND t.homework_class_id =:homeworkClassId
#end
#if(bt)
	AND (t.deadline > :bt)
#end
#if(et)
	AND (t.start_time < :et)
#end
#if(keys)
 AND (
	EXISTS (
		select 1 from homework_metaknow hm
		inner join meta_knowpoint mk on mk.code=hm.meta_code and REPLACE(mk.name,' ','') like :keys
		where hm.homework_id=t.id
	)
	or REPLACE(t.name,' ','') like :keys
 )
#end
#if(secname)
	AND
	EXISTS (
		select 1 from section st where st.code = e.section_code and st.name like :secname
	)
#end
#if(line == true)
	AND
	NOT EXISTS (
		select 1 from section st where st.code = e.section_code
	)
#end
#if(status)
	AND sh.status=:status
#end

 ORDER BY sh.status ASC, sh.create_at DESC
#end