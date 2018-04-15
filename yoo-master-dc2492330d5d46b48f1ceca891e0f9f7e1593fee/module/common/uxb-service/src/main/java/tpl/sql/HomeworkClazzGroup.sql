## 更新当前班级组学生数量
#macro($zyUpdateStudentCount(id,num))
UPDATE homework_class_group SET student_count = (student_count - :num) WHERE id = :id AND status = 0
#end

## 根据名称查找当前班级中的班级组
#macro($zyFindNameInClass(name,classId))
SELECT t.* FROM homework_class_group t WHERE t.name = :name AND t.class_id = :classId AND t.status = 0
#end

## 查询当前班级中有多少个班级组
#macro($zyCountGroupInClass(classId))
SELECT count(id) FROM homework_class_group t WHERE t.class_id = :classId AND t.status = 0
#end

## 获取班级所有正常分组
#macro($zyFindByClazzId(clazzId))
SELECT * FROM homework_class_group where class_id=:clazzId and status=0 order by create_at ASC
#end

## 批量获取班级所有正常分组
#macro($zyFindByClazzIds(clazzIds))
SELECT * FROM homework_class_group where class_id in :clazzIds and status=0 order by create_at ASC
#end