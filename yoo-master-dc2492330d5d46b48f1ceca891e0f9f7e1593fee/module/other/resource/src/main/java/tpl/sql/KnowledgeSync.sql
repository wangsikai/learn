## 获取所有同步知识点
#macro($findAll(phaseCode, subjectCode, status))
select t.* from knowledge_sync t where t.phase_code=:phaseCode and t.subject_code=:subjectCode
#if(status)
 and t.status=:status
#end
#end