#macro($findByKnowledgePoint(knowledgePointCardId,checkStatus,status))
SELECT * FROM knowledge_point_card WHERE knowpoint_code = :knowledgePointCode AND check_status = 2 AND del_status = 0
#end


##统计知识点对应的通过的卡片数量
#macro($statisByPoints(codes))
SELECT count(id) count,knowpoint_code FROM knowledge_point_card 
WHERE check_status = 2 AND del_status = 0 AND knowpoint_code IN :codes
GROUP BY knowpoint_code
#end

## 查找卡片不为空的知识点
#macro($findKnowledgePoint(codes))
SELECT DISTINCT t.knowpoint_code FROM knowledge_point_card t WHERE t.knowpoint_code IN :codes AND t.check_status = 2 AND t.del_status = 0
#end

##根据阶段查询第一个知识点卡片
#macro($getFirstKnowpointCard(phaseCode))
select * from knowledge_point_card kpc INNER JOIN knowledge_point kp on kpc.knowpoint_code= kp.code and kp.status = 0 where 
kpc.del_status = 0 and kpc.check_status=2 
#if(phaseCode)
and phase_code =:phaseCode
#end
ORDER BY kp.CODE LIMIT 1
#end

## 根据知识点列表查找所有知识点数据
#macro($findByKnowledgePoints(codes))
SELECT t.* FROM knowledge_point_card t WHERE t.knowpoint_code IN :codes AND t.check_status = 2 AND t.del_status = 0
#end

## 根据学科编码查找所有知识点数据
#macro($findBySubject(subjectCode))
SELECT t.* FROM knowledge_point_card t 
 inner join knowledge_point kp on kp.code=t.knowpoint_code and kp.subject_code=:subjectCode
 WHERE t.check_status = 2 AND t.del_status = 0
#end
