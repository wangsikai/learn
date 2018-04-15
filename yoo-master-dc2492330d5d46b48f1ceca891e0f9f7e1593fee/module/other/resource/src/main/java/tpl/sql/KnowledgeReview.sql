## 获取所有复习知识点
#macro($findAll(phaseCode, subjectCode, status))
select t.* from knowledge_review t where t.phase_code=:phaseCode and t.subject_code=:subjectCode
#if(status)
 and t.status=:status
#end
#end