## 查询指定的题目
#macro($findQuestions(code,type,grade,textbookCategoryCode,tag))
SELECT * FROM imperial_exam_2_question 
WHERE activity_code = :code
AND type = :type
AND grade = :grade
AND textbook_category_code = :textbookCategoryCode
AND tag = :tag
ORDER BY sequence ASC
#end