## 查询所有答案记录
#macro($ymFindExamAnswers(userId,codes,activityCode))
SELECT * FROM exam_activity_001_answer WHERE user_id =:userId AND activity_code=:activityCode AND exam_question_code IN :codes
#end


## 查询单个答案记录
#macro($ymFindExamAnswer(userId,code,activityCode))
SELECT * FROM exam_activity_001_answer WHERE exam_question_code=:code AND user_id =:userId AND activity_code=:activityCode order by create_at desc
#end