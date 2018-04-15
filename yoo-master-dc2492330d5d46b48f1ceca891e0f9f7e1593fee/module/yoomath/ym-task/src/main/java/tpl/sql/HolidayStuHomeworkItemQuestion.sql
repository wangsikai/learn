##查询学生作业对应的寒假作业题目
#macro($findHolidayQuestions(holidayStudentHomeworkIds))
SELECT * FROM holiday_stu_homework_item_question WHERE holiday_stu_homework_id in :holidayStudentHomeworkIds
#end
