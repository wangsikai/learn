##通过预置内容Id查询对应的疑难点
#macro($getByPresetId(teachassistPresetcontentId))
SELECT * FROM teachassist_pc_falldiff WHERE teachassist_presetcontent_id =:teachassistPresetcontentId AND del_status = 0
#end

##是否有未通过的
#macro($nopassCount(teachassistPresetcontentId))
SELECT count(id) FROM teachassist_pc_falldiff WHERE teachassist_presetcontent_id =:teachassistPresetcontentId 
	AND del_status = 0 AND check_status = 3
#end

## 根据知识专项查找数据
#macro($findByKnowledgeCode(code))
SELECT t.* FROM teachassist_pc_falldiff t INNER JOIN teachassist_presetcontent p ON p.id = t.teachassist_presetcontent_id
WHERE t.del_status = 0 AND t.check_status = 2 AND p.knowledge_system_code = :code ORDER BY t.id DESC
#end