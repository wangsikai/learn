##分页获取假期活动作业
#macro($taskGetAllByPage())
SELECT id FROM holiday_activity_01_homework WHERE id < :next and homework_id is not null ORDER BY id DESC
#end

##批量获取假期活动作业
#macro($mgetHolidayActivity01Homework(ids))
SELECT * FROM holiday_activity_01_homework WHERE id in (:ids)
#end

##分页获取假期活动作业(homework_id is null)
#macro($taskGetByHomeworkIdIsNull(code))
SELECT * FROM holiday_activity_01_homework WHERE id < :next and homework_id is null and activity_code=:code ORDER BY id DESC
#end

##获取班级用户提交率
#macro($userSubmitRate(studentId,classId,code))
SELECT ROUND(SUM(t.commit)*100/SUM(t.total),0) FROM(
	SELECT 0 AS total,COUNT(1) AS COMMIT FROM student_homework a 
	INNER JOIN holiday_activity_01_homework b ON a.homework_id = b.homework_id AND a.student_id = :studentId AND b.class_id = :classId
	WHERE stu_submit_at IS NOT NULL and b.homework_id is not null and b.activity_code = :code 
	UNION
	SELECT COUNT(1) AS total,0 AS COMMIT  FROM student_homework a 
	INNER JOIN holiday_activity_01_homework b ON a.homework_id = b.homework_id AND a.student_id = :studentId AND b.class_id = :classId
	where b.homework_id is not null and b.activity_code = :code
) t
#end

##获取班级提交率
#macro($classSubmitRate(teacherId,classId,code))
SELECT ROUND(AVG(submit_rate),0) FROM holiday_activity_01_homework WHERE class_id = :classId and user_id = :teacherId and activity_code = :code
#end

##更新homeworkId
#macro($taskUpdateHolidayActivity01Homework(id,homeworkId))
update holiday_activity_01_homework set homework_id =:homeworkId where id =:id
#end


