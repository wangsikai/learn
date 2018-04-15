## 根据用户ID和日期查找目标统计记录
#macro($findByUserId(userId,date0))
SELECT * FROM do_question_goal_count WHERE user_id = :userId AND date0 = :date0
#end

## 答题数量的更新
#macro($incrCount(userId,date0,delta))
UPDATE do_question_goal_count SET goal = goal + :delta WHERE user_id = :userId AND date0 = :date0
#end