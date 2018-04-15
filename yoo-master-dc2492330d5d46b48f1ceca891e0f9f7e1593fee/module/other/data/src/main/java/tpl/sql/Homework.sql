#macro($getLatestHomeworkDate(classId))
## @since 小优快批，2018-3-9，改为已批改完成的作业
SELECT t.* FROM homework t WHERE t.homework_class_id = :classId AND (status = 3 or all_correct_complete = 1) ORDER BY t.id DESC limit 1
#end
