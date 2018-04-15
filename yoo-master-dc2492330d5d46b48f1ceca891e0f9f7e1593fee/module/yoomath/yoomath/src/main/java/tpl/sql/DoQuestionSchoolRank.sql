## 查询学校维度统计
#macro($listDoQuestionSchoolRank(schoolId,topN,startDate,endDate))
SELECT * FROM do_question_school_rank 
WHERE school_id = :schoolId 
AND start_date = :startDate 
AND end_date = :endDate
ORDER BY rank ASC
LIMIT :topN
#end

## 查询个人统计
#macro($findStudentInSchoolRank(schoolId,userId,startDate,endDate))
SELECT * FROM do_question_school_rank 
WHERE school_id = :schoolId 
AND start_date = :startDate 
AND end_date = :endDate
AND user_id = :userId
#end

## 更新点赞数
#macro($updateSchoolPraiseCount(rankId,userId))
UPDATE do_question_school_rank 
SET praise_count = praise_count + 1
WHERE id = :rankId
#end

## 取消点赞
#macro($cancelSchoolPraiseCount(rankId,userId))
UPDATE do_question_school_rank 
SET praise_count = praise_count - 1
WHERE id = :rankId
#end