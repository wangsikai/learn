##更新学生专项题目解题截图
#macro($saveHolidaySolvingImg(stuItemId,solvingImg))
UPDATE holiday_stu_homework_item_question SET solving_img = :solvingImg  WHERE id = :stuItemId
#end

##通过题目ID和学生假期专项题目ID获取专项题目对象
#macro($findStuItemQuestion(questionId,questionIds,stuItemId))
select * from holiday_stu_homework_item_question where holiday_stu_homework_item_id =:stuItemId
	#if(questionId)
	and question_id = :questionId
	#end
	#if(questionIds)
	and question_id in (:questionIds)
	#end
#end

## 根据学生专项作业查找此专项下的题目
#macro($queryByItem(itemId))
SELECT t.* FROM holiday_stu_homework_item_question t
WHERE t.holiday_stu_homework_item_id = :itemId
#end

##更新学生专项题目解题截图
#macro($saveHolidayAnswerImg(stuItemId,answerImg))
UPDATE holiday_stu_homework_item_question SET answer_img = :answerImg WHERE id = :stuItemId
#end
