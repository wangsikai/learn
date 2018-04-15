## 根据学生作业ID和习题ID获取非订正的作业习题
#macro($findByStudentHomeworkIdAndQuestionId(studentHomeworkId,questionId,questionIds))
SELECT * FROM student_homework_question 
WHERE student_homework_id = :studentHomeworkId 
AND new_correct = 0 AND correct = 0
#if(questionId)
AND question_id = :questionId
#end
#if(questionIds)
AND question_id IN :questionIds
#end
#end

## 更新语音批注
#macro($zyUpdateVoice(shqId,voiceTime,fileKey))
UPDATE student_homework_question SET voice_time=:voiceTime, voice_file_key=:fileKey WHERE id=:shqId
#end

## 根据学生作业ID获取需要订正的题目数量
#macro($findNeedCorrectQuestionCount(studentHomeworkId))
SELECT count(*) FROM student_homework_question 
WHERE student_homework_id = :studentHomeworkId 
AND (new_correct = 1 or correct = 1)
AND is_revised = 0
#end

## 根据学生作业ID获取总的订正题目数量
#macro($findCorrectQuestionCount(studentHomeworkId))
SELECT count(*) FROM student_homework_question 
WHERE student_homework_id = :studentHomeworkId 
AND (new_correct = 1 or correct = 1)
#end

## 根据学生作业ID获取已订正的题目数量
#macro($findCorrectedQuestionCount(studentHomeworkId))
SELECT count(*) FROM student_homework_question 
WHERE student_homework_id = :studentHomeworkId 
AND (new_correct = 1 or correct = 1)
AND is_revised = 1
#end

## 根据学生作业ID获取当前已订正还没有批改的订正题数量
#macro($findNoCorrectQuestionCount(studentHomeworkId))
SELECT count(*) FROM student_homework_question 
WHERE student_homework_id = :studentHomeworkId 
AND (new_correct = 1 or correct = 1)
AND is_revised = 1
AND result IN (0,3)
#end

## 更新学生作业题目订正题订正状态
#macro($updateReviseStatus(id))
UPDATE student_homework_question SET is_revised = 1
WHERE id = :id
#end

## 手动批改学生作业题目
#macro($zyCorrect(id,result,questionRate,correctAt,correctType))
UPDATE student_homework_question SET result = :result,manual_correct=1
#if(questionRate)
, right_rate =:questionRate
#end
#if(correctAt)
, correct_at =:correctAt
#end
#if(correctType)
, correct_type =:correctType
#end
WHERE id = :id
#end

## 更新轨迹
#macro($zyUpdateNotation(id,srcId,generateId,notationImg,notationPoints,rightRate,result,correctAt,correctType))
UPDATE student_homework_question 
SET 
#if(notationImg)
notation_answer_img = :generateId,
answer_notation = :notationImg, 
answer_notation_points = null,
notation_web_img = :srcId,
notation_mobile_img = null
  #if(rightRate)
    ,right_rate = :rightRate, result = :result
  #end
  #if(correctAt)
    ,correct_at = :correctAt,manual_correct=1
  #end
  #if(correctType)
    ,correct_type =:correctType
  #end
#end
#if(notationPoints)
notation_answer_img = :generateId,
answer_notation = null, 
answer_notation_points = :notationPoints,
notation_web_img = null, 
notation_mobile_img = :srcId
  #if(rightRate)
    ,right_rate = :rightRate, result = :result
  #end
  #if(correctAt)
    ,correct_at = :correctAt,manual_correct=1
  #end
  #if(correctType)
    ,correct_type =:correctType
  #end
#end
WHERE id = :id
#end

## 更新轨迹,多图时候
#macro($zyUpdateMultiNotation(id,oriSrcId,srcId,generateId,notationImg,notationPoints))
UPDATE student_homework_answer_image 
SET
##web
#if(notationImg)
notation_answer_img = :generateId,
answer_notation = :notationImg, 
answer_notation_points = null,
notation_web_img = :srcId, 
notation_mobile_img = null
WHERE student_homework_question_id = :id and (answer_img = :oriSrcId OR notation_answer_img = :oriSrcId OR notation_web_img = :oriSrcId)
#end
##mobile
#if(notationPoints)
notation_answer_img = :generateId,
answer_notation = null, 
answer_notation_points = :notationPoints,
notation_web_img = null, 
notation_mobile_img = :srcId
WHERE student_homework_question_id = :id and (answer_img = :oriSrcId OR notation_answer_img = :oriSrcId OR notation_mobile_img = :oriSrcId)
#end
#end

## 更新结果及正确率
#macro($zyUpdateResultAndRightRate(id,result,rightRate,notationImageId,notation,correctAt,correctType))
UPDATE student_homework_question
SET
result = :result
#if(rightRate)
,right_rate = :rightRate
#end
#if(notationImageId)
,notation_answer_img = :notationImageId
#end
#if(notation)
,answer_notation = :notation
#end
#if(correctAt)
, correct_at =:correctAt,manual_correct=1
#end
#if(correctType)
,correct_type =:correctType
#end
WHERE id = :id
#end

## 根据学生作业ID和习题ID获取作业习题
#macro($queryStuQuestions(studentHomeworkId,questionId,questionIds,newCorrect))
SELECT * FROM student_homework_question 
WHERE student_homework_id = :studentHomeworkId 
#if(newCorrect)
AND new_correct = 1
#end
#if(questionId)
AND question_id = :questionId
#end
#if(questionIds)
AND question_id IN :questionIds
#end
#end

## 根据学生作业ID和习题ID获取非订正的作业习题
#macro($findByNewCorrect(studentHomeworkIds,questionId,questionIds,newCorrect))
SELECT * FROM student_homework_question 
WHERE student_homework_id IN (:studentHomeworkIds) 
#if(questionId)
AND question_id = :questionId
#end
#if(questionIds)
AND question_id IN :questionIds
#end
#if(newCorrect)
AND new_correct = :newCorrect
#end
#end

## 获取新订正题
## @since 小悠快批，2018-3-12
#macro($getNewCorrectQuestion(studentHomeworkId, questionId))
select * from student_homework_question where student_homework_id=:studentHomeworkId and question_id = :questionId and new_correct=1
#end

## 根据学生作业ID获取当前已订正还没有批改的订正题数量
#macro($queryCorrectAnswerTeacherCorrectCount(studentHomeworkId))
SELECT count(*) as count FROM student_homework_question 
WHERE student_homework_id = :studentHomeworkId
AND (new_correct = 1 or correct = 1)
AND result IN (0,3)
and correct_type = 3
#end

## 更新学生作业习题的待确认状态
#macro($setHomeworkQuestionConfirmStatus(studentHomeworkQuestionId, confirmStatus))
update student_homework_question set confirm_status=:confirmStatus where id=:studentHomeworkQuestionId
#end