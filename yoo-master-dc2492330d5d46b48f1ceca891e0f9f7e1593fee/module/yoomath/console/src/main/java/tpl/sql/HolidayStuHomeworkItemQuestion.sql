## 查询学生未批改的题目
#macro($zycFindHolidayStuQuestion(notInIds,size))
SELECT t.* FROM holiday_stu_homework_item_question t
INNER JOIN question q ON q.id = t.question_id
INNER JOIN holiday_homework hk ON t.holiday_homework_id = hk.id
WHERE t.auto_correct = 1 AND t.manual_correct = 0
AND t.auto_result IN (0, 2, 3) AND q.type IN (3, 5, 6)
AND hk.del_status = 0 AND hk.status IN (1, 2)
#if(notInIds)
AND t.id NOT IN :notInIds
#end
ORDER BY t.id ASC
LIMIT :size
#end

## 查询假期作业学生未批改题目数量
#macro($zycCountNotCorrectQuestions())
SELECT count(*) FROM holiday_stu_homework_item_question t
INNER JOIN question q ON q.id = t.question_id
INNER JOIN holiday_homework hk ON t.holiday_homework_id = hk.id
WHERE t.auto_correct = 1 AND t.manual_correct = 0
AND t.auto_result IN (0, 2, 3) AND q.type IN (3, 5, 6)
AND hk.del_status = 0 AND hk.status IN (1, 2)
#end

## 更新假期作业批改确认状态
#macro($zycUpdateStatus(ids,status))
UPDATE holiday_stu_homework_item_question SET confirm_status = :status WHERE id IN :ids
#end

## 查询已经批改过待确认的题目
#macro($zycFindConfirm())
SELECT t.* FROM holiday_stu_homework_item_question t
INNER JOIN holiday_homework hk ON t.holiday_homework_id = hk.id
INNER JOIN holiday_stu_homework_item sht ON t.holiday_stu_homework_item_id = sht.id
WHERE sht.status = 1 AND sht.right_rate IS NULL AND hk.del_status = 0 AND hk.status IN (1, 2) AND t.manual_correct = 1
AND t.confirm_status = 1
#end

## 更新批改学生假期专项题目数据
#macro($zycUpdateResult(id,result,correctAt))
UPDATE holiday_stu_homework_item_question SET result = :result, correct_at = :correctAt, manual_correct = 1, confirm_status = 1 WHERE id = :id
#end

## 通过ID查询已经批改过待确认的题目
#macro($zycFindConfirmById(questionCode))
SELECT t.* FROM holiday_stu_homework_item_question t
INNER JOIN holiday_homework hk ON t.holiday_homework_id = hk.id
INNER JOIN holiday_stu_homework_item sht ON t.holiday_stu_homework_item_id = sht.id
INNER JOIN question q ON t.question_id = q.id
WHERE sht.status = 1 AND sht.right_rate IS NULL AND hk.del_status = 0 AND hk.status IN (1, 2) AND t.manual_correct = 1
AND t.confirm_status = 1
AND q.code = :questionCode
#end