##查询学校ID和题目ID对应的数据
#macro($getSquestion(schoolId, questionId))
SELECT * FROM school_question WHERE question_id =:questionId AND school_id = :schoolId
#end