## 查询某班级某学生最近一次的统计数据
#macro($ymFindLatestData(classId,studentId))
SELECT t.* FROM diagno_class_student t WHERE t.user_id = :studentIdn AND t.class_id = :classId ORDER BY day0 DESC limit 1
#end

## 查询班级各项正确率前10名
#macro($ymFindTopnStudent(classId))
SELECT t.user_id FROM
 (
    SELECT m.user_id FROM diagno_class_student m WHERE m.class_id = :classId AND m.status = 0 AND m.day0 = 0
 ) t LIMIT 10
#end

## 根据不同的范围查询数据
#macro($ymQuery(day0,classId,userId,statAt))
SELECT t.* FROM diagno_class_student t WHERE t.class_id = :classId AND t.user_id = :userId AND t.day0 = :day0 AND t.statistic_at = :statAt
#end

## 批量更新数据
#macro($ymUpdate(classId,yesterday))
UPDATE diagno_class_student SET status = 1 WHERE class_id = :classId AND statistic_at <= :yesterday AND status = 0
#end

## 批量查询学生的统计数据
#macro($ymQueryByStudents(day0,classId,userIds,statAt))
SELECT t.* FROM diagno_class_student t WHERE t.class_id = :classId AND t.user_id IN :userIds AND t.day0 = :day0 AND t.statistic_at = :statAt
#end

## 批量更新数据
#macro($ymDelete(day0,classId,statisticAt))
DELETE FROM diagno_class_student where class_id=:classId and day0=:day0 and statistic_at=:statisticAt
#end
