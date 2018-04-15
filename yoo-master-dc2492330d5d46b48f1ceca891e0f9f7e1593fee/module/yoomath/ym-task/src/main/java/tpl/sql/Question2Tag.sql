#macro($taskListByQuestions(questionIds))
select t.* from question_2_tag t 
inner join question_tag qt on qt.code=t.tag_code and qt.status=0
where t.question_id in (:questionIds)
order by qt.sequence ASC
#end

#macro($taskDeleteSystemQuestions())
DELETE q2t FROM question_2_tag q2t INNER JOIN question_tag qt ON qt.code=q2t.tag_code AND qt.type=0
#end

## TASK-删除习题标签
#macro($deleteByQuestionAndTag(questionIds, tagCode))
DELETE FROM question_2_tag where tag_code=:tagCode and question_id in (:questionIds)
#end

## TASK-热门题需要删除的部分
#macro($findHotQuestionsForDEL(minPulishCount))
SELECT q2t.question_id FROM question_2_tag q2t
LEFT JOIN 
( 
 SELECT COUNT(hq.id),hq.question_id FROM homework_question hq 
 GROUP BY hq.question_id HAVING COUNT(hq.id)>=:minPulishCount
) t ON t.question_id=q2t.question_id
WHERE q2t.tag_code=1 AND t.question_id IS NULL
ORDER BY q2t.question_id ASC
#end

## TASK-热门题需要增加的部分
#macro($findHotQuestionsForADD(minPulishCount))
SELECT t.question_id FROM question_2_tag q2t
RIGHT JOIN 
( 
 SELECT COUNT(hq.id),hq.question_id FROM homework_question hq 
 GROUP BY hq.question_id HAVING COUNT(hq.id)>=:minPulishCount
) t ON t.question_id=q2t.question_id AND q2t.tag_code=1
WHERE q2t.question_id IS NULL
ORDER BY t.question_id ASC
#end

## TASK-易错题需要删除的部分
#macro($findFallQuestionsForDEL(maxRightRate, minDoNum))
SELECT q2t.question_id FROM question_2_tag q2t
LEFT JOIN 
( 
 SELECT AVG(right_rate),t.question_id FROM teacher_fallible_question t
 GROUP BY t.question_id HAVING AVG(right_rate)<=:maxRightRate AND SUM(t.do_num)>=:minDoNum
) t ON t.question_id=q2t.question_id
WHERE q2t.tag_code=2 AND t.question_id IS NULL
ORDER BY q2t.question_id ASC 
#end

## TASK-易错题需要增加的部分
#macro($findFallQuestionsForADD(maxRightRate, minDoNum))
SELECT t.question_id FROM question_2_tag q2t
RIGHT JOIN 
( 
 SELECT AVG(right_rate),t.question_id FROM teacher_fallible_question t
 GROUP BY t.question_id HAVING AVG(right_rate)<=:maxRightRate AND SUM(t.do_num)>=:minDoNum
) t ON t.question_id=q2t.question_id AND q2t.tag_code=2
WHERE q2t.question_id IS NULL
ORDER BY t.question_id ASC
#end

## TASK-好题需要删除的部分
#macro($findGoodQuestionsForDEL(minCollectCount))
SELECT q2t.question_id FROM question_2_tag q2t
LEFT JOIN 
( 
 SELECT COUNT(t.id), t.question_id FROM question_collection t 
 GROUP BY t.question_id HAVING COUNT(t.id)>=:minCollectCount
) t ON t.question_id=q2t.question_id
WHERE q2t.tag_code=3 AND t.question_id IS NULL
ORDER BY q2t.question_id ASC 
#end

## TASK-好题需要增加的部分
#macro($findGoodQuestionsForADD(minCollectCount))
SELECT t.question_id FROM question_2_tag q2t
RIGHT JOIN 
( 
 SELECT COUNT(t.id), t.question_id FROM question_collection t 
 GROUP BY t.question_id HAVING COUNT(t.id)>=:minCollectCount
) t ON t.question_id=q2t.question_id AND q2t.tag_code=3
WHERE q2t.question_id IS NULL
ORDER BY t.question_id ASC
#end