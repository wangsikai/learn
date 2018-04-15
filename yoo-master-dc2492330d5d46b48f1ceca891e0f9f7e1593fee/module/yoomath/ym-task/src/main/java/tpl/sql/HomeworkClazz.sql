#macro($taskQueryClass())
SELECT id,teacher_id FROM homework_class WHERE status = 0 AND lock_status = 0 AND id < :next ORDER BY id DESC
#end

## 游标查询班级列表(有效,布置过作业并且下发过的班级)
#macro($ymFindEnableClass())
SELECT t.* FROM homework_class t WHERE id < :next AND t.status = 0 AND t.id IN
(SELECT DISTINCT h.homework_class_id FROM homework h WHERE h.status = 3)
ORDER BY t.id DESC
#end

## 游标查询班级列表(当天或某个时间段,下发过作业的班级列表)
#macro($curDayIssuedClass(startTime,endTime))
SELECT t.id FROM homework_class t WHERE id < :next AND t.status = 0 AND t.id IN
(SELECT h.homework_class_id 
	FROM homework h WHERE h.issue_at IS NOT NULL
		#if(startTime)
			AND h.issue_at > :startTime
		#end
		#if(endTime)
			AND h.issue_at < :endTime
		#end
GROUP BY h.homework_class_id)
ORDER BY t.id DESC
#end

## 查询
#macro($taskZyQuery(teacherId,status,size))
SELECT * FROM homework_class WHERE teacher_id = :teacherId
#if(status)
 AND status = :status
#end
#if(status==0)
ORDER BY id DESC
#end
#if(status==1)
ORDER BY update_at DESC
#end
#if(size)
 limit :size
#end
#end

## 游标班级
#macro($taskGetAllByPage())
SELECT t.* FROM homework_class t, teacher b
WHERE t.teacher_id = b.id AND t.id < :next and  t.status = 0 
AND b.textbook_code IS NOT NULL AND b.textbook_category_code IS NOT NULL
ORDER BY t.id DESC
#end

## 根据一个班级找到下面所有学生所在的所有班级关系
#macro($findAllByOneClassStudent(homeworkClassId))
SELECT hsc2.class_id,hsc2.student_id FROM homework_class hc
 INNER JOIN homework_student_class hsc ON hsc.class_id=hc.id AND hsc.status=0
 INNER JOIN homework_student_class hsc2 ON hsc2.student_id=hsc.student_id AND hsc2.status=0 AND hsc2.class_id IN
 (SELECT DISTINCT h.homework_class_id FROM homework h WHERE h.status = 3)
 WHERE hc.status=0 AND hc.id=:homeworkClassId
#end