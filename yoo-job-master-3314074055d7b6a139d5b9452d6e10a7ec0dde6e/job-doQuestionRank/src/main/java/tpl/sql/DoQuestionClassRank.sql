## 查询班级维度统计
#macro($listDoQuestionClassStat(classId,topN,startDate,endDate))
SELECT * FROM do_question_class_rank 
WHERE class_id = :classId 
AND start_date = :startDate 
AND end_date = :endDate
ORDER BY rank ASC
LIMIT :topN
#end

## 查询指定学生统计信息
#macro($findStudentInClassRank(classId,userId,startDate,endDate))
SELECT * FROM do_question_class_rank 
WHERE class_id = :classId 
AND start_date = :startDate 
AND end_date = :endDate
AND user_id = :userId
#end

## 更新点赞数
#macro($updateClassPraiseCount(rankId,userId))
UPDATE do_question_class_rank 
SET praise_count = praise_count + 1
WHERE id = :rankId
#end

## 查询classIds
#macro($findClassIds(startDate,endDate,startindex,size))
SELECT DISTINCT class_id FROM do_question_class_rank 
WHERE start_date = :startDate 
AND end_date = :endDate
LIMIT :startindex, :size
#end

## 查询指定学生统计信息
#macro($findStudentInClassRanks(classIds,startDate,endDate))
SELECT * FROM do_question_class_rank 
WHERE class_id IN (:classIds) 
AND start_date = :startDate 
AND end_date = :endDate
#end

## 游标查询所有数据
#macro($findAllRankPraiseByCursor(startDate,endDate))
SELECT * FROM do_question_class_rank 
WHERE start_date = :startDate 
AND end_date = :endDate
#if(next)
	AND id < :next
#end
ORDER BY id DESC
#end