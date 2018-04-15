## 根据学生作业题目id和学生id查找申述记录
#macro($findQuestionAppeal(sHkQId))
SELECT * FROM question_appeal WHERE biz_id = :sHkQId
#end

## 查询学生作业习题最近的一条申诉记录
#macro($getLastAppeal(sHkQId))
SELECT * FROM question_appeal where biz_id = :sHkQId and appeal_type=1
order by create_at DESC limit 1
#end