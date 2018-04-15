## 根据学生id列表以及班级id查找数据
#macro($zyFindByStusAndClass(studentIds,classId))
SELECT t.* FROM homework_class_group_student t
WHERE t.student_id IN :studentIds AND t.class_id = :classId
#end

## 移除学生组中的指定学生
#macro($zyRemoveStudents(studentIds,classId))
DELETE FROM homework_class_group_student
WHERE student_id IN :studentIds AND class_id = :classId
#end

## 获取所有的学生班级分组信息
#macro($zyFindAll(clazzId))
SELECT * FROM homework_class_group_student 
where class_id=:clazzId
#end

## 获取组内学生Id集合
#macro($zyfindGroupStudents(clazzId,groupId))
SELECT student_id FROM homework_class_group_student 
where class_id=:clazzId and group_id = :groupId
#end

## 获取指定班级学生ID的学生分组
#macro($zyFindByClazzAndStudent(clazzId, studentId))
SELECT * FROM homework_class_group_student where class_id=:clazzId and student_id=:studentId
#end

## 删除分组
#macro($zyDeleteGroup(groupId))
DELETE from homework_class_group_student where group_id=:groupId
#end

## 移除分组内学生数据
#macro($zyRemoveByGroup(studentIds,groupId))
DELETE FROM homework_class_group_student
WHERE student_id IN :studentIds AND group_id = :groupId
#end