#macro($nda01Exist(studentId,questionId))
SELECT COUNT(id) FROM national_day_activity_01_question WHERE student_id = :studentId AND question_id = :questionId
#end