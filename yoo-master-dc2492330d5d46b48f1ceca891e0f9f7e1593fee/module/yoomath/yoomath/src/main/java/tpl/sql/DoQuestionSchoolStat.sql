## 更加条件查询topN
#macro($zyListTopN(schoolId,day0,phaseCode))
SELECT * FROM do_question_school_stat 
WHERE school_id = :schoolId AND phase_code = :phaseCode AND status = 0 AND day0 = :day0
ORDER BY do_count DESC,right_rate DESC
LIMIT :topN
#end

## 学校排行翻页
#macro($queryQuesBySchool(schoolId,day0,phaseCode))
SELECT * FROM do_question_school_stat 
WHERE school_id = :schoolId AND phase_code = :phaseCode AND status = 0 AND day0 = :day0
ORDER BY do_count DESC,right_rate DESC
#end