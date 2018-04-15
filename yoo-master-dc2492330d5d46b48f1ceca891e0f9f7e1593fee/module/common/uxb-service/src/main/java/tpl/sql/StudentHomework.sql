#macro($findStudentHomework(studentId,homeworkId))
SELECT * FROM student_homework WHERE homework_id = :homeworkId
#if(studentId)
AND student_id = :studentId
#end
#end

##按学生加入时间排序
#macro($listByHomeworkOrderByJoinAt(homeworkId,classId))
SELECT a.* FROM student_homework a INNER JOIN 
homework_student_class b ON a.student_id = b.student_id
AND a.homework_id = :homeworkId 
AND b.class_id = :classId
ORDER BY a.status,a.correct_status DESC
#end

## 上交所有未上交的学生作业
#macro($commitStudentHomework(homeworkId,commitAt))
UPDATE student_homework 
SET submit_at = :commitAt,status = 1 
WHERE homework_id = :homeworkId AND status = 0
#end

## 下发作业
#macro($issue(homeworkId,issueAt))
UPDATE student_homework SET status = 2,issue_at = :issueAt WHERE homework_id = :homeworkId
#end

## 查询学生作业
#macro($query(courseId,studentId,status,keyword))
SELECT * FROM student_homework a INNER JOIN homework b ON a.homework_id = b.id
WHERE student_id = :studentId and b.course_id = :courseId
#if(keyword)
AND b.name LIKE :keyword
#end
#if(status)
AND a.status = :status
#end
AND a.id < :next ORDER BY a.id DESC 
#end

## 查询学生作业
#macro($queryHk(studentId,status,commitCount))
SELECT * FROM student_homework a INNER JOIN homework b ON b.id = a.homework_id
WHERE student_id = :studentId
#if(status)
AND a.status = :status
#end
#if(commitCount)
AND b.commit_count >= :commitCount
#end
AND a.id < :next ORDER BY a.id DESC
#end

## 查询学生作业
#macro($queryByStudent(studentId, homeworkIds))
SELECT * FROM student_homework WHERE student_id = :studentId
#if(homeworkIds)
AND homework_id in (:homeworkIds)
#end
#end


## 计算作业没有提交的人数
#macro($countNotCommit(homeworkId))
SELECT count(*) FROM student_homework WHERE homework_id = :homeworkId AND status = 0
#end


## 查询自己没有提交的作业的学生id集合
#macro($findNotSubmitStus(homeworkId))
SELECT student_id FROM student_homework WHERE homework_id = :homeworkId AND status != 0 AND stu_submit_at IS null
#end

#macro($findStudentHomeworkByFilter(homeworkId, rightRate, leftRate, timeLimit, status, name, def, rate, timeby))
SELECT sh.* FROM student_homework sh 
INNER JOIN student s on sh.student_id = s.id
WHERE sh.homework_id = :homeworkId
#if(rightRate)
AND sh.right_rate < :rightRate
#end
#if(leftRate)
AND sh.right_rate >= :leftRate
#end
#if(timeLimit)
AND sh.homeworkTime > :timeLimit
#end
#if(status)
AND sh.status = :status
#end
#if(name)
AND s.name like :name
#end
#if(rate)
order by sh.right_rate desc
#end
#if(timeby)
order by sh.homework_time asc
#end
#end


#macro($countCommit(homeworkId))
SELECT COUNT(id) FROM student_homework WHERE homework_id = :homeworkId AND stu_submit_at IS NOT NULL
#end


##更新学生作业正确率
#macro($zyRateUpdate(id,rightRate))
UPDATE student_homework SET right_rate =:rightRate WHERE id =:id AND status = 1
#end

##查询出已经提交了的学生作业集合
#macro($findSubmitedStuHomeworks(homeworkId,classId))
SELECT a.* FROM student_homework a INNER JOIN 
homework_student_class b ON a.student_id = b.student_id
AND a.homework_id = :homeworkId 
AND a.status = 1
AND b.class_id = :classId
ORDER BY b.join_at DESC
#end

##更新学生作业表状态
#macro($updateStuHomework(homeworkId))
UPDATE student_homework  SET del_status = 1 where homework_id = :homeworkId
#end

## 更新作业正确率
## @since 小悠快批，2018-3-7
#macro($updateStudentHomeworkRightRate(studentHomeworkId, rightCount, wrongCount, halfWrongCount, rightRate, rightRateCorrect, correctCompleteTime))
UPDATE student_homework set right_count=:rightCount,wrong_count=:wrongCount,half_wrong_count=:halfWrongCount
#if(rightRate)
,right_rate=:rightRate
#end
#if(rightRateCorrect)
,right_rate_correct=:rightRateCorrect
#end
#if(correctCompleteTime)
,issue_at=:correctCompleteTime
#end
where id=:studentHomeworkId
#end

## 更新作业订正后的订正正确率
## @since 小悠快批，2018-3-12
#macro($updateStudentHomeworkRightRateCorrect(studentHomeworkId, rightRateCorrect))
UPDATE student_homework set right_rate_correct=:rightRateCorrect where id=:studentHomeworkId
#end

## 更新作业状态
## @since 小悠快批，2018-3-12
#macro($setStudentHomeworkCorrectStatus(studentHomeworkId, correctStatus))
UPDATE student_homework set correct_status=:correctStatus where id=:studentHomeworkId
#end

## 获取因异常未处理完成的作业
#macro($queryNotCompleteStudentHomework(bt, et))
SELECT t.* FROM student_homework t
INNER JOIN homework h ON h.id=t.homework_id AND (h.status=1 OR h.status=2)
WHERE t.status=1 AND (t.correct_status IS NULL OR t.correct_status=0 OR t.correct_status=1) AND t.stu_submit_at IS NOT NULL
AND t.stu_submit_at > :et AND t.stu_submit_at <= :bt
#if(next)
 AND t.id < :next
#end
ORDER BY t.id DESC
#end