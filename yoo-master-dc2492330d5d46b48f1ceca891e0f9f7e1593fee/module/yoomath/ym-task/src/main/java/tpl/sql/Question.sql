##判断是否有简答题
#macro($TaskHasQuestionAnswering(ids))
SELECT count(id) FROM question WHERE id IN (:ids) AND type = 5
#end

## 查找子题
#macro($TaskGetSubQuestions(parentId,parentIds))
SELECT * FROM question WHERE del_status=0
	#if(parentId)
	AND parent_id = :parentId
	#end
	#if(parentIds)
	AND parent_id IN (:parentIds)
	#end
	ORDER BY sequence ASC
#end

## 查找初始化需要转换标签的习题
#macro($findQuestionsForInitTag(nowdate))
select t.* from question t where t.category_types is not null and t.create_at<:nowdate order by t.id ASC
#end

## 查找带典型题的考点
#macro($findExaminationPointForInitTag())
SELECT ep.* FROM examination_point ep WHERE ep.questions IS NOT NULL order by ep.id ASC
#end

## 查找初始化需要转换标签的习题
#macro($findHotQuestionsForInitTag(minPulishCount))
SELECT question_id FROM ( 
	SELECT COUNT(hq.id),hq.question_id FROM homework_question hq 
	GROUP BY hq.question_id HAVING COUNT(hq.id)>=:minPulishCount 
) t ORDER BY question_id ASC 
#end

## 查找初始化需要转换标签的习题
#macro($findFallQuestionsForInitTag(maxRightRate,minDoNum))
SELECT tt.question_id FROM (
SELECT AVG(right_rate),t.question_id FROM teacher_fallible_question t
GROUP BY t.question_id HAVING AVG(right_rate)<=:maxRightRate AND SUM(t.do_num)>=:minDoNum
) tt ORDER BY question_id ASC
#end

## 查找初始化需要转换标签的习题
#macro($findGoodQuestionsForInitTag(minCollectCount))
SELECT tt.question_id FROM (
SELECT COUNT(t.id), t.question_id FROM question_collection t 
GROUP BY t.question_id HAVING COUNT(t.id)>=:minCollectCount
) tt 
ORDER BY question_id ASC
#end