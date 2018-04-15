## 包含相似题的题目数
#macro($similarCounts(vendorId))
SELECT COUNT(q.id) FROM question_similar q WHERE q.status=0 and q.change_flag = 1 and q.vendor_id=:vendorId
#end

## 重复题数
#macro($sameCounts(vendorId))
SELECT COUNT(q.id) FROM question q WHERE q.del_status=0 AND q.same_show_id IS NOT NULL and q.vendor_id=:vendorId
#end

#macro($getByBaseId(baseQuestionId))
SELECT * FROM question_similar where status = 0 and change_flag = 1 and base_question_id=:baseQuestionId
#end

## 查询转换前的旧数据
#macro($queryOldDatas())
SELECT * FROM question_similar where status = 0 and change_flag = 0 order by id ASC
#end

## 查询新的转换后的数据
#macro($queryNewDatasByQuestions(questionIds))
SELECT * FROM question_similar where status = 0 and change_flag = 1 and base_question_id in (:questionIds)
#end

## 判断包含题型的个数
#macro($getQuestionTypesCount(questionIds, types))
SELECT count(id) FROM question where id in (:questionIds) and type in (:types)
#end