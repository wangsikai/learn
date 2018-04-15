## 简单的习题搜索(只搜大题)
#macro($searchQuestion(keyword,difficulty,maxDifficulty,minDifficulty,typeCodes,qTypeCodes))
SELECT a.* FROM question a INNER JOIN question_type b ON a.type_code = b.code WHERE a.sub_flag = 0
#if(keyword)
AND a.content LIKE :keyword
#end
#if(difficulty)
AND a.difficulty = :difficulty
#end
#if(maxDifficulty)
AND a.difficulty <= :maxDifficulty
#end
#if(minDifficulty)
AND a.difficulty >= :minDifficulty
#end
#if(typeCodes)
AND a.type_code IN :typeCodes
#end
#if(qTypeCodes)
AND b.code IN :qTypeCodes
#end
AND a.id < :next ORDER BY a.id DESC
#end


#macro($setQuestionStatus(productId,status,updateId))
update question set status = :status , update_id=:updateId where id = :productId 
#end


## 获取类别最大CODE
#macro($getMaxCode(prefix))
SELECT MAX(t.code) FROM question t WHERE t.code LIKE :prefix
#end

## 分页找大题（wordML缓存使用）
#macro($buildWordMLAllQuery(minId))
SELECT * FROM question WHERE del_status = 0 and sub_flag = 0 and status = 2
 #if(minId)
 AND id>:minId
 #end
 order by id ASC
#end

## 分页找大题（wordML缓存使用）
#macro($buildWordMLQuery(minId))
SELECT t.* FROM question t 
 LEFT JOIN `question_wordml` qw ON qw.id=t.id
 WHERE t.del_status = 0 AND t.sub_flag = 0 AND t.status = 2 AND qw.id IS NULL
 #if(minId)
 AND t.id>:minId
 #end
 order by t.id ASC
#end

## 分页找大题（下载PIC使用）
#macro($picQueryByPage(minId))
SELECT id FROM question WHERE del_status = 0 AND sub_flag = 0 AND status = 2 AND id>:minId order by id ASC
#end

## 通过code查询题目
#macro($findByCodes(codes))
select * from question where code in :codes
#end