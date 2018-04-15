## 根据id查找练习记录
#macro($zyFindByPractise(pId))
SELECT * FROM daily_practise_question t WHERE t.practise_id = :pId ORDER BY t.question_id DESC
#end

## 查询已经做过的题目
#macro($zyFindPulledQuestionIds(userId,sectionCode))
SELECT t.question_id FROM daily_practise_question t INNER JOIN daily_practise m ON t.practise_id = m.practise_id
WHERE m.practise_id = 0 AND m.user_id = :userId AND m.section_code = :sectionCode
#end

## 更新练习题目
#macro($zyUpdateQuestion(questionId,result,done,answer,practiseId))
UPDATE daily_practise_question
SET result = :result, done = :done, answer = :answer
WHERE question_id = :questionId AND practise_id = :practiseId
#end

## 查找一个学生教材下的做过题目数
#macro($zyFindStudentQuestionCount(textbookCode,studentId))
SELECT count(*) FROM daily_practise_question t
INNER JOIN daily_practise m ON t.practise_id = m.id
WHERE
m.update_at IS NOT NULL AND m.textbook_code = :textbookCode
AND m.do_count > 0
AND t.done = 1
AND m.user_id = :studentId
#end

## 计数已做题目数量
#macro($zyCountDone(practiseId))
SELECT count(*) FROM daily_practise_question t
WHERE t.done = 1 AND t.practise_id = :practiseId
#end