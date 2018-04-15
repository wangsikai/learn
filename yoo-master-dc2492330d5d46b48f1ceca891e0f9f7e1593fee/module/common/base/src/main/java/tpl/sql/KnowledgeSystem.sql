##根据科目查询对应的知识体系
#macro($findAllSystem())
SELECT * FROM knowledge_system WHERE status = 0
#end

##根据科目查询对应的知识体系
#macro($findAllBySubject(subjectCode))
SELECT * FROM knowledge_system WHERE subject_code = :subjectCode and status = 0
#end

##根据code查询对应的子集
#macro($findChildByPcode(code))
SELECT * FROM knowledge_system WHERE pcode = :code and status = 0
#end

## 根据学科及层级查询数据
#macro($findBySubjectAndLevel(level,subjectCode))
SELECT * FROM knowledge_system WHERE level = :level AND subject_code = :subjectCode AND status = 0
#end