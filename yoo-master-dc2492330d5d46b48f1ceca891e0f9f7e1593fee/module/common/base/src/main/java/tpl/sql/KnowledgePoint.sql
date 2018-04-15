##查询所有的知识点
#macro($findAllPoint())
SELECT * FROM knowledge_point WHERE status = 0
#end

##根据科目查询对应的知识体系
#macro($findAllBySubject(subjectCode))
SELECT * FROM knowledge_point WHERE subject_code = :subjectCode and status = 0
#end

##根据pcode查询对应的子知识点
#macro($findByPcode(pcode))
SELECT * FROM knowledge_point WHERE pcode = :pcode and status = 0
#end

##查询所有的知识点
#macro($findAllKnowledgepoints(noHasCodes))
SELECT * FROM knowledge_point WHERE status = 0
#if(noHasCodes)
 AND code not in (:noHasCodes)
#end
#end
