##查询学生最近24小时下发的作业（第一次除外）
##需求变更 2017.2.24跟UE沟通，改为只取普通作业的
#macro($queryLatestList(clazzId,startTime,endTime))
SELECT 1 AS hkType,c.start_time,a.student_id,f.question_id,f.result
	FROM student_homework a 
	INNER JOIN homework c ON a.homework_id = c.id
	INNER JOIN student_homework_question f ON a.id = f.student_homework_id
	AND c.status = 3 AND c.homework_class_id =:clazzId
	#if(startTime)
		AND c.issue_at > :startTime
	#end
	#if(endTime)
		AND c.issue_at < :endTime
	#end
	AND a.stu_submit_at IS NOT NULL and f.result is not null
#end


##查询学生最新的30次作业(普通作业)
#macro($queryCommonHkList(studentId,classId))
SELECT a.right_rate,a.rank,b.start_time,a.id,a.homework_id FROM student_homework a 
	INNER JOIN homework b ON a.homework_id = b.id 
	AND a.student_id = :studentId AND b.homework_class_id = :classId
	AND b.status = 3 AND a.stu_submit_at IS NOT NULL and a.rank is not null
	ORDER BY b.start_time DESC LIMIT 0,30
#end


##查询学生最新的30次作业(普通作业/寒假作业)
#macro($queryHolidayHkList(studentId,classId))
select * from (
	SELECT a.right_rate,a.rank,b.start_time,a.id,a.homework_id,1 AS hkType FROM student_homework a 
	INNER JOIN homework b ON a.homework_id = b.id 
	AND a.student_id = :studentId AND b.homework_class_id = :classId
	AND b.status = 3 AND a.stu_submit_at IS NOT NULL and a.rank is not null
	UNION ALL 
	SELECT c.right_rate,c.rank,d.start_time,c.id,c.holiday_homework_id homework_id,2 AS hkType  FROM holiday_stu_homework c 
	INNER JOIN holiday_homework d ON c.holiday_homework_id = d.id 
	AND c.status = 2 AND c.student_id = :studentId AND d.homework_class_id =:classId  AND c.right_rate IS NOT NULL
) t
ORDER BY t.start_time DESC LIMIT 0,30
#end

## 查询学生作业
#macro($ymQueryStuHks(hkId))
SELECT t.* FROM student_homework t WHERE t.homework_id = :hkId ORDER t.rank ASC
#end

## 根据时间进行学生排名
#macro($ymRankStudent(classId,startAt,endAt))
SELECT avg(t.right_rate) as right_rate, t.student_id,  sum(t.right_count) as right_count, sum(t.wrong_count) AS wrong_count, sum(t.half_wrong_count) AS half_wrong_count  FROM student_homework t
INNER JOIN homework h ON h.id = t.homework_id
WHERE h.homework_class_id = :classId
AND h.status = 3
AND t.student_id in (SELECT m.student_id FROM homework_student_class m WHERE m.class_id = :classId AND m.status = 0)
#if(endAt)
 AND h.start_time <= :endAt
#end
#if(startAt)
 AND h.start_time > :startAt
#end
GROUP BY t.student_id ORDER BY right_rate DESc
#end

## 查找班级学生一段时间的全部数据
## 2017-7-12 只取还在本班级的学生作业
#macro($ymFindAll(classId, now))
SELECT t.right_rate, h.start_time, t.student_id FROM student_homework t
INNER JOIN homework h ON h.id = t.homework_id
INNER JOIN homework_student_class hsc on hsc.student_id=t.student_id and hsc.class_id=h.homework_class_id and hsc.status=0
WHERE h.homework_class_id = :classId
AND h.status = 3
#if(now)
 AND h.start_time <= :now
#end
ORDER BY h.start_time ASC
#end

##查询学生作业情况,只查普通作业
#macro($findStuHkList(studentId,startTime,endTime))
SELECT f.question_id,f.result,f.id
	FROM student_homework a 
	INNER JOIN homework c ON a.homework_id = c.id
	INNER JOIN student_homework_question f ON a.id = f.student_homework_id
	AND c.status = 3 AND a.student_id = :studentId
	#if(startTime)
		AND c.issue_at > :startTime
	#end
	#if(endTime)
		AND c.issue_at < :endTime
	#end
	AND a.stu_submit_at IS NOT NULL and f.result is not null
	AND f.id < :next ORDER BY f.id DESC
#end

##查询通过作业id,查询学生做题情况
#macro($findStuHkListByHomework(hkId))
SELECT f.question_id,f.result,a.student_id
	FROM student_homework a 
	INNER JOIN student_homework_question f ON a.id = f.student_homework_id
	and a.homework_id = :hkId and a.stu_submit_at IS NOT NULL and f.result is not null
	order by a.student_id desc
#end

##判断学生在某班级是否有过已下发的作业（教学诊断使用）
#macro($ymCountStudentHomeworkForDiagnostic(classId, studentId, curDate))
SELECT COUNT(t.id) FROM student_homework t
 INNER JOIN homework h ON h.id=t.homework_id AND h.homework_class_id=:classId
 #if(curDate)
 AND h.issue_at<:curDate AND h.issue_at>'2017-08-30 09:00:00'
 #end
 AND h.status=3 AND h.del_status=0 AND t.del_status=0
 WHERE t.student_id=:studentId AND t.submit_at IS NOT NULL AND t.stu_submit_at IS NOT NULL
#end

##查询学生的科举大典作业
#macro($findStuHkImperialHomework(code,type,tag))
SELECT a.*
	FROM student_homework a 
	INNER JOIN imperial_exam_homework_student f ON a.homework_id = f.homework_id AND a.student_id = f.user_id 
	#if(code)
		AND f.activity_code = :code
	#end
	#if(type)
		AND f.type = :type
	#end
	#if(tag)
		AND f.tag = :tag
	#end
#end


