##查询作业项题目ID集合
#macro($queryQuestions(holidayHomeworkItemId))
SELECT question_id FROM holiday_homework_item_question WHERE holiday_homework_item_id =:holidayHomeworkItemId
#end

##查询作业项题目集合
#macro($getHomeworkItemQuestion(holidayHomeworkItemId))
SELECT * FROM holiday_homework_item_question WHERE holiday_homework_item_id =:holidayHomeworkItemId
#end

##查询作业项习题
#macro($findQuestionByItem(questionId,questionIds,holidayHomeworkItemId))
SELECT * FROM holiday_homework_item_question WHERE 1=1
#if(question_id)
and question_id =:questionId
#end
#if(questionIds)
and question_id  in (:questionIds)
#end
and holiday_homework_item_id=:holidayHomeworkItemId
#end

##作业某题做错的学生列表
#macro($listWrongStu(homeworkItemId,questionId))
SELECT sh.student_id,stha.* FROM holiday_stu_homework_item sh INNER JOIN holiday_stu_homework_item_question shq ON shq.holiday_stu_homework_item_id
 =sh.id  INNER JOIN holiday_stu_homework_item_answer stha ON stha.holiday_stu_homework_item_qid =shq.id WHERE sh.holiday_homework_item_id=:homeworkItemId
AND shq.question_id =:questionId AND shq.result=2 
#end

## 查询假期作业专项中的题目
#macro($findByQuestionAndItem(questionId,itemId))
SELECT t.* FROM holiday_homework_item_question t WHERE t.holiday_homework_item_id = :itemId
AND t.question_id = :questionId
#end

## 获取正确率集合
#macro($getRateStat(holidayHomeworkItemId))
SELECT right_rate FROM holiday_homework_item_question where holiday_homework_item_id =:holidayHomeworkItemId
#end

## 批量获得专项下面的题目
#macro($mgetByItemIds(itemIds))
SELECT t.* FROM holiday_homework_item_question t WHERE t.holiday_homework_item_id IN :itemIds
#end
