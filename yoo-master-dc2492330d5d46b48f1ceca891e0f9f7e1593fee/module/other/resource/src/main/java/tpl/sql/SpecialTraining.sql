##查询专项训练列表
#macro($list(code))
SELECT * FROM special_training where knowpoint_code = :code and status != 5 order by create_at desc
#end

##查询专项训练列表总数
#macro($total(code))
SELECT count(id) FROM special_training where knowpoint_code like :code and status != 5
#end

##通过小专题code查询专项训练统计
#macro($getStat(code,flag))
SELECT count(id) count,status FROM special_training 
where 1=1 
and knowpoint_code like :code
group by status
#end

##通过科目查找对应的专项训练统计
#macro($getStatBySubject(subjectCode))
SELECT count(id) count,a.status FROM special_training a
inner join knowledge_system b on a.knowpoint_code = b.code
where subject_code = :subjectCode group by a.status
#end

##通过科目查找对应的专项训练总数
#macro($getTotalBySubject(subjectCode))
SELECT count(a.id) FROM special_training a
inner join knowledge_system b on a.knowpoint_code = b.code
where subject_code = :subjectCode
#end

## 通过知识专项查找训练列表（已通过）
#macro($findByCode(code))
SELECT t.* FROM special_training t WHERE t.knowpoint_code = :code AND t.status = 2 ORDER BY create_at DESC
#end