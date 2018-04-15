## 获取某一门课程下所有学生的作业统计
#macro($zyFindByCourseId(courseId))
SELECT * FROM student_homework_stat WHERE course_id = :courseId
#end

## 获取某一个班级下所有学生的作业统计
#macro($zyFindByClazzId(clazzId))
SELECT * FROM student_homework_stat WHERE homework_class_id = :clazzId
#end

## 查找一个学生作业某门课程的统计
#macro($zyFindOne(studentId,courseId,homeworkClassId))
SELECT * FROM student_homework_stat WHERE user_id = :studentId  
#if(courseId)
 AND course_id = :courseId 
#end
#if(homeworkClassId)
 AND homework_class_id = :homeworkClassId
#end
#end

## 根据正确率排序获取一门课程的所有学生作业统计[统计用,其他地方勿用]
#macro($zyStaticRightRateRank(courseId,classId))
SELECT * FROM student_homework_stat 
WHERE 1=1 
#if(courseId)
AND course_id = :courseId
#end
#if(classId)
AND homework_class_id = :classId
#end
ORDER BY right_rate DESC
#end

## 根据作业班级ID获取学生作业统计
#macro($zyGetByHomeworkClassId(studentId,homeworkClassId))
SELECT * FROM student_homework_stat WHERE homework_class_id = :homeworkClassId AND user_id = :studentId
#end

## 根据作业班级IDs获取学生作业统计
#macro($zyGetByHomeworkClassIds(studentId,homeworkClassIds))
SELECT * FROM student_homework_stat WHERE homework_class_id IN (:homeworkClassIds) AND user_id = :studentId
#end

## 根据作业班级ID和学生IDs获取学生作业统计
#macro($zyFind(studentIds,homeworkClassId))
SELECT * FROM student_homework_stat WHERE homework_class_id = :homeworkClassId AND user_id IN (:studentIds)
#end

## 批量查询学生的作业提交率
#macro($getSubmitRateByStuId(studentId,classIds))
	SELECT ROUND((homework_num-overdue_num-todo_num)*100/(homework_num)) rate,homework_class_id FROM student_homework_stat
	where user_id =:studentId and homework_class_id in :classIds
#end