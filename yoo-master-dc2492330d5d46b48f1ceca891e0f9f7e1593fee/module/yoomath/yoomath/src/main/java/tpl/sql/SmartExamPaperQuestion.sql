###获取试卷对应的题目
#macro($queryPaperQuestions(paperId))
select * from smart_exam_paper_question where paper_id =:paperId
#end
