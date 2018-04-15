## 查询指定学生统计信息
#macro($findStudentInSchoolRank(schoolId,userId,startDate,endDate))
SELECT * FROM do_question_school_rank 
WHERE school_id = :schoolId 
AND start_date = :startDate 
AND end_date = :endDate
AND user_id = :userId
#end

## 查询schoolIds
#macro($findSchoolIds(startDate,endDate,startindex,size))
SELECT DISTINCT school_id FROM do_question_school_rank 
WHERE start_date = :startDate 
AND end_date = :endDate
LIMIT :startindex, :size
#end

## 游标查询校级榜数据
#macro($findSchoolRankPraiseBySchoolId(startDate,endDate, schoolId))
SELECT * FROM do_question_school_rank 
WHERE start_date = :startDate 
AND end_date = :endDate
#if(schoolId)
	AND school_id = :schoolId
#end
#if(next)
	AND id < :next
#end
ORDER BY id DESC
#end