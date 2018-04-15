## 更加条件查询topN
#macro($zyListTopN(classId,day0,topN))
SELECT * FROM do_question_class_stat 
WHERE class_id = :classId AND status = 0 AND day0 = :day0
ORDER BY rank ASC
LIMIT :topN
#end

## 查找学生班级里面的统计
#macro($zyFindStudentInClassStat(day0,classId,userId))
SELECT * FROM do_question_class_stat WHERE status = 0 AND day0 = :day0 AND class_id = :classId AND user_id = :userId
#end

## 批量查询学生班级对应的练习统计
#macro($mgetDoQuestionClassStat(day0,classIds,userId))
SELECT * FROM do_question_class_stat WHERE status = 0 AND day0 = :day0 AND class_id in :classIds AND user_id = :userId
#end