## 查询
#macro($zyQuery(classId,studentId,status))
SELECT * FROM homework_student_class WHERE class_id = :classId
#if(studentId)
 AND student_id = :studentId
#end
#if(status)
 AND status = :status
#end
#end

## 查找学生班级
#macro($zyFindByStudentId(studentId,hasTeacher))
SELECT t1.* 
FROM 
	homework_student_class t1 INNER JOIN homework_class t2 ON t1.class_id = t2.id
WHERE 
	t1.student_id = :studentId 
AND t1.status = 0 
AND t2.status = 0
#if(hasTeacher)
	AND t2.teacher_id > 0
#end
ORDER BY t1.join_at DESC
#end

## 班级里面的有效学生
#macro($zyListClassStudents(classId))
SELECT student_id FROM homework_student_class WHERE class_id = :classId AND status = 0 ORDER BY join_at DESC 
#end

## 判断学生是否在这个班级内
#macro($zyIsJoin(classId,studentId))
SELECT count(*) FROM homework_student_class 
WHERE class_id = :classId AND student_id = :studentId AND status = 0 
#end

## 分页查询班级学生
#macro($zyPageClassStudents(classId, joinorder))
SELECT * FROM homework_student_class WHERE class_id = :classId AND status = 0 
#if(joinorder == 'asc')
 ORDER BY join_at ASC
#elseif
 ORDER BY join_at DESC
#end

#end

## 分页查询班级学生
#macro($zyPageClassStudentsByGroup(classId, groupId))
SELECT t.* FROM homework_student_class t
 inner join homework_class_group_student gs on gs.student_id=t.student_id and gs.class_id=t.class_id
 and gs.group_id=:groupId
 WHERE t.class_id = :classId AND t.status = 0 ORDER BY t.join_at DESC
#end

## 查询一个班级的所有学生
#macro($zyListStudents(classId,limit))
SELECT * FROM homework_student_class WHERE class_id = :classId AND status = 0 ORDER BY join_at DESC LIMIT :limit
#end

## 计算学生班级数量
#macro($zyCountStudentClazz(classId,status))
SELECT count(*) FROM homework_student_class WHERE student_id = :studentId
#if(status)
AND status = :status
#end
#end

## 备注学生
#macro($zyMarkStudent(classId,studentId,name))
UPDATE homework_student_class SET mark = :name 
WHERE student_id = :studentId AND class_id = :classId AND status = 0
#end

## 查询多个学生
#macro($zyFindStudentsByIds(studentIds))
SELECT * FROM homework_student_class t WHERE t.student_id IN :studentIds
#end

## 通过班级查找微信绑定的学生
#macro($findWXStudentByClass(type, homeworkClassIds))
SELECT DISTINCT(t.uid) AS openid,u.name AS NAME FROM credential t
 INNER JOIN USER u ON u.account_id=t.account_id
 INNER JOIN homework_student_class c ON c.student_id=u.id AND c.class_id in (:homeworkClassIds)
 WHERE t.type=:type
#end

## 通过班级分组查找微信绑定的学生
#macro($findWXStudentByClassGroup(type,groupIds))
  SELECT DISTINCT(t.uid) AS openid,u.name AS NAME FROM credential t
   INNER JOIN USER u ON u.account_id=t.account_id
   INNER JOIN homework_class_group_student c ON c.student_id=u.id AND c.group_id in (:groupIds)
   WHERE t.type=:type
#end

## 通过作业查找微信绑定的学生
#macro($findWXStudentByHomework(type, homeworkId))
SELECT DISTINCT(t.uid) AS openid,u.name AS NAME FROM credential t
 INNER JOIN USER u ON u.account_id=t.account_id
 INNER JOIN student_homework h ON h.student_id=u.id AND h.homework_id=:homeworkId
 WHERE t.type=:type
#end

## 根据学生id列表以及班级id查找数据
#macro($zyFindByStudentsAndClass(studentIds,classId))
SELECT t.* FROM homework_student_class t WHERE t.student_id IN :studentIds AND t.class_id = :classId AND t.status = 0
#end