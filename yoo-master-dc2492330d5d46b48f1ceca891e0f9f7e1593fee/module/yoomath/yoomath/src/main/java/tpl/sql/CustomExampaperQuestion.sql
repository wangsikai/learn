## 根据组卷id查询所有题目
#macro($findByPaper(paperId))
SELECT t.* FROM custom_exampaper_question t
inner join custom_exampaper_topic tp on tp.id=t.custom_exampaper_topic_id
WHERE t.custom_exampaper_id = :paperId and t.question_type in (1,3,5)
ORDER BY tp.sequence ASC,t.sequence ASC
#end

#macro($deletCustomExamQuestions(examPaperId))
DELETE ceq.* FROM custom_exampaper_question ceq WHERE ceq.custom_exampaper_id =:examPaperId
#end

#macro($deletCustomExamTopic(examPaperId))
DELETE cet.* FROM custom_exampaper_topic cet WHERE cet.exam_paper_id =:examPaperId
#end


