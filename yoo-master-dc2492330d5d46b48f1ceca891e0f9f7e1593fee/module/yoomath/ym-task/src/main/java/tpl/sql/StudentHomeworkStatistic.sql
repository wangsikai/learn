## 获取表数据量
#macro($countStuHkstat())
SELECT count(user_id) FROM student_homework_statistic
#end

## 获取学生统计数据
#macro($stuHkStat(classId))
	select count(1) homework_num,ROUND(AVG(a.completion_rate)) completion_rate,a.student_id 
	from student_homework a 
	inner join homework b on a.homework_id = b.id
	where a.del_status = 0
	#if(classId)
	 and a.student_id in (select student_id from homework_student_class where class_id = :classId)
	#end
	group by a.student_id
#end

## 统计学生正确率,只计算已下发
#macro($stuRateStat(classId))
	select ROUND(AVG(a.right_rate)) right_rate,a.student_id 
	from student_homework a 
	inner join homework b on a.homework_id = b.id
	where a.del_status = 0 and a.status = 2
	#if(classId)
	 and a.student_id in (select student_id from homework_student_class where class_id = :classId)
	#end
	group by a.student_id
#end

## 获取学生统计数据
#macro($stuHkStatByStudentIds(studentIds))
	select count(1) homework_num,ROUND(AVG(a.completion_rate)) completion_rate,a.student_id 
	from student_homework a 
	inner join homework b on a.homework_id = b.id
	where a.del_status = 0
	and a.student_id in :studentIds
	group by a.student_id
#end

## 统计学生正确率,只计算已下发
#macro($rateMapByStudentIds(studentIds))
	select ROUND(AVG(a.right_rate)) right_rate,a.student_id 
	from student_homework a 
	inner join homework b on a.homework_id = b.id
	where a.del_status = 0 and a.status = 2
	 and a.student_id in :studentIds
	group by a.student_id
#end

