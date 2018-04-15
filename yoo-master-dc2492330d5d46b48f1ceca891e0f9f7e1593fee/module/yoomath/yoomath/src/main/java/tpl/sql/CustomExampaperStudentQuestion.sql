## 根据学生组卷ID获得学生习题列表
#macro($listCustomExampaperStudentQuestions(customExampaperStudentID))
 select * from custom_exampaper_student_question where custom_exampaper_student_id=:customExampaperStudentID
#end

## 根据学生组卷ID获得组卷习题列表
#macro($listCustomExampaperQuestions(customExampaperStudentID))
 SELECT t.* FROM custom_exampaper_question t
 INNER JOIN custom_exampaper_student_question st ON st.custom_exampaper_question_id=t.id
 AND st.custom_exampaper_student_id=:customExampaperStudentID
 ORDER BY t.sequence ASC
#end