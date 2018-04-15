## 查找试卷
#macro($ymFindExams(category,grade,type))
SELECT * FROM exam_activity_001_question WHERE textbook_category_code=:category and grade =:grade and type=:type
#end