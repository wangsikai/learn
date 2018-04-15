## 分页查询学生掌握情况等
#macro($query(day0,orderBy,classId))
SELECT t.* FROM diagno_class_student t WHERE t.class_id = :classId AND t.day0 = :day0 AND t.status = 0
#if(orderBy == 1 || orderBy == 2 || orderBy == 3)
AND t.right_rate IS NOT NULL
#end
#if(orderBy == 0)
ORDER BY t.right_rate DESC
#end
#if(orderBy == 1)
and t.float_rank >= 0
ORDER BY t.float_rank DESC
#end
#if(orderBy == 2)
and t.float_rank <= 0
ORDER BY t.float_rank ASC
#end
#if(orderBy == 3)
ORDER BY t.homework_count DESC
#end
#end