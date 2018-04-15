## 查询新知识点为空的homework_id
#macro($findKpIsNullList())
	SELECT id FROM homework WHERE knowledge_points IS NULL or knowledge_points = ''
#end

##通过作业id查询对应的题目新知识点 
#macro($findNewKps(homeworkId))
	SELECT DISTINCT b.knowledge_code FROM homework_question a
	INNER JOIN question_knowledge b ON a.question_id = b.question_id
 	WHERE homework_id = :homeworkId
#end

##查询新知识点里存的旧知识点的数据 
#macro($findKpIsWrongList())
	SELECT id FROM homework WHERE knowledge_points IS NOT NULL AND knowledge_points!=''
AND knowledge_points NOT IN (SELECT CODE FROM knowledge_point)
#end
