##通过预置内容Id查询对应的预习点
#macro($getByPresetId(teachassistPresetcontentId))
SELECT * FROM teachassist_pc_preview WHERE teachassist_presetcontent_id =:teachassistPresetcontentId AND del_status = 0
#end

## 根据知识专项代码查找对应预习点
#macro($resconFindByKnowledgeSystem(code))
SELECT t.* FROM teachassist_pc_preview t INNER JOIN teachassist_presetcontent p ON t.teachassist_presetcontent_id = p.id
WHERE t.check_status = 2 AND p.knowledge_system_code = :code AND t.del_status = 0
#end


##是否有未通过的
#macro($nopassCount(teachassistPresetcontentId))
SELECT count(id) FROM teachassist_pc_preview WHERE teachassist_presetcontent_id =:teachassistPresetcontentId 
	AND del_status = 0 AND check_status = 3
#end

