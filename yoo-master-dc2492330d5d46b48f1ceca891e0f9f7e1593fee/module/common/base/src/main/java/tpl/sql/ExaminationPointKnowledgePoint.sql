#macro($findByKnowledgePoint(knowledgePointCode))
SELECT * FROM examination_point_knowledge_point WHERE knowledge_point_code = :knowledgePointCode
#end

##根据考点删除关联
#macro($deleteByExaminationPointID(examinationPointID))
delete from examination_point_knowledge_point where examination_point_id=:examinationPointID
#end

##根据考点查询
#macro($findByExaminationPoint(examinationPointCode))
SELECT * FROM examination_point_knowledge_point WHERE examination_point_id = :examinationPointCode
#end

##根据考点查询
#macro($findByExaminationPoints(examinationPointCodes))
SELECT * FROM examination_point_knowledge_point WHERE examination_point_id in (:examinationPointCodes)
#end