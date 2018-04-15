## 查询今日下发作业的数据
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($ymGetNowDayIssuedHk(startAt,endAt))
SELECT t.homework_class_id, t.create_id FROM homework t WHERE t.issue_at >= startAt AND t.issue_at <= endAt 
 AND (t.status = 3 or t.all_correct_complete = 1) AND t.del_status = 0 AND t.right_rate IS NOT NULL
#end

## 查询最近30次作业数据
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($ymGetLatestHomework(classId))
SELECT t.* FROM homework t WHERE (t.status = 3 or t.all_correct_complete = 1)
 AND t.del_status = 0 AND t.homework_class_id = :classId ORDER BY t.start_time DESC limit 30
#end

## 查找一同布置下去的作业数据
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($ymFindByExercise(exerciseId))
SELECT t.* FROM homework t WHERE t.exercise_id = :exerciseId AND (t.status = 3 or t.all_correct_complete = 1) AND t.del_status = 0 ORDER BY t.right_rate DESC
#end

## 查找一同布置下去的作业数据
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($ymFindByExercises(exerciseIds))
SELECT t.* FROM homework t WHERE t.exercise_id in (:exerciseIds) AND (t.status = 3 or t.all_correct_complete = 1) AND t.del_status = 0 ORDER BY t.right_rate DESC
#end


## 查询一个班级所有已下发作业
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($ymFindAllByClass(classId))
SELECT t.* FROM homework t WHERE (t.status = 3 or t.all_correct_complete = 1) AND t.del_status = 0 AND t.homework_class_id = :classId ORDER BY t.issue_at ASC
#end


##游标查询已下发的作业ID 集合
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($findIssueHkIds(nowTime))
	SELECT id FROM homework WHERE (t.status = 3 or t.all_correct_complete = 1) AND del_status = 0 AND id < :next
	#if(nowTime)
		AND issue_at <= :nowTime
	#end
	ORDER BY id DESC
#end


##获取时间范围内布置并且下发的作业
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($taskListByTime(beginTime,endTime,createId,clazzId))
SELECT * FROM homework WHERE create_at>:beginTime AND create_at<:endTime
AND create_id=:createId AND homework_class_id=:clazzId AND (status = 3 or all_correct_complete = 1)
#end

##获取某个班级在一定时间范围内下发的第一个作业
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($taskGetFirstIssuedHomeworkInMonth(beginTime,endTime,classId))
SELECT * FROM homework 
WHERE issue_at > :beginTime AND issue_at < :endTime AND del_status = 0 AND (status = 3 or all_correct_complete = 1) ORDER BY issue_at ASC LIMIT 1
#end

##获取学生平均正确率
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($findStuAvgRightRateList(startTime,endTime,classId))
	select ROUND(avg(s.right_rate)) as avgRight, s.student_id as studentId from student_homework s
	inner join homework t on t.id = s.homework_id
	inner join homework_student_class dd on dd.student_id = s.student_id and dd.status = 0
	where t.homework_class_id = :classId
	and (t.status = 3 or t.all_correct_complete = 1)
	and t.create_at >= :startTime and t.create_at <= :endTime and s.submit_at is not null and s.stu_submit_at is not null
	group by s.student_id
	order by avgRight desc
#end

## 查询规定时间内下发作业的用户id
## @since 小优快批，2018-3-9，改为已批改完成的作业
#macro($getIssueHomeworkUserId(code,startTime,endTime,process))
select t.user_id from imperial_exam_homework t
INNER JOIN homework h ON t.homework_id = h.id AND h.commit_count > 0 
and (h.status = 3 or h.all_correct_complete = 1)
AND t.activity_code = :code
#if(startTime)
AND h.issue_at >= :startTime
#end
#if(endTime)
AND h.issue_at <= :endTime
#end
GROUP BY t.user_id
#end

## 查询规定时间内提交作业且正确率不为0的学生id
#macro(getCommitHomeworkStudentId(code,startTime,endTime))
select t.user_id from imperial_exam_homework_student t
INNER JOIN student_homework h ON t.homework_id = h.homework_id and h.student_id = t.user_id 
WHERE h.status = 2 AND h.right_rate > 0
AND t.activity_code = :code
#if(startTime)
AND h.issue_at >= :startTime
#end
#if(endTime)
AND h.issue_at <= :endTime
#end
GROUP BY t.user_id
#end

## 修改老师作业的删除状态
#macro($modifyHomeworkDelstatus(code))
update homework t inner join imperial_exam_homework h on t.id = h.homework_id set t.del_status = 0 
WHERE h.activity_code = :code
#end

## 修改学生作业的删除状态
#macro($modifyStudentHomeworkDelstatus(code))
update student_homework sh inner join imperial_exam_homework_student hs on sh.homework_id = hs.homework_id set sh.del_status = 0  
WHERE hs.user_id = sh.student_id
AND hs.activity_code = :code
#end
