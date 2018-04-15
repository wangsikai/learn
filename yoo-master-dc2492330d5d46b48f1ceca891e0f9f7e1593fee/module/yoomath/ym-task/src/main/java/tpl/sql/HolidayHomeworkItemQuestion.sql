## 查找一个假期作业所有专项题目的练习情况
#macro($ymFindHolidayHkItemQuestion(hkId))
SELECT SUM(t.right_count) AS right_count, SUM(t.wrong_count) AS wrong_count, t.question_id, q.difficulty
FROM holiday_homework_item_question t
  INNER JOIN holiday_homework_item m ON t.holiday_homework_item_id = m.id
  INNER JOIN holiday_homework h ON h.id = m.holiday_homework_id
  INNER JOIN question q ON q.id = t.question_id
WHERE h.id = :hkId
GROUP BY t.id
#end