## 根据学期和班级IDs获取班级作业统计数据
#macro($zyFindByTeacherId(courseIds))
SELECT * FROM homework_stat WHERE course_id IN (:courseIds)
#end

## 查找数据统计
#macro($zyFindOne(teacherId,homeworkClassId))
SELECT * FROM homework_stat WHERE user_id = :teacherId
#if(homeworkClassId)
	AND homework_class_id = :homeworkClassId
#end
#end

## 根据作业班级ID获取作业统计
#macro($zyGetByHomeworkClassId(homeworkClassId))
SELECT * FROM homework_stat WHERE homework_class_id = :homeworkClassId
#end

## 根据作业班级IDs获取作业统计
#macro($zyGetByHomeworkClassIds(homeworkClassIds))
SELECT * FROM homework_stat WHERE homework_class_id IN (:homeworkClassIds)
#end