## 根据模块id查找数据
#macro($resconFindByElement(id))
SELECT t.* FROM teachassist_e_knoledgespec_kp t WHERE t.knowledge_spec_id = :id ORDER BY t.sequence ASC
#end

## 根据模块id删除数据
#macro($resconDeleteByElement(elementId))
DELETE FROM teachassist_e_knoledgespec_kp WHERE knowledge_spec_id = :elementId
#end

## 根据多个模块id查找数据
#macro($resconFindByElements(elementIds))
SELECT t.* FROM teachassist_e_knoledgespec_kp t WHERE t.knowledge_spec_id IN :elementIds
#end