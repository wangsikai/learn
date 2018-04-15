##通过知识专项查询预置内容
#macro($getByKsCode(code))
SELECT * FROM teachassist_presetcontent WHERE knowledge_system_code =:code AND del_status = 0
#end

##通过知识专项批量查询预置内容
#macro($mgetByKsCodes(codes))
SELECT * FROM teachassist_presetcontent WHERE knowledge_system_code in :codes AND del_status = 0
#end