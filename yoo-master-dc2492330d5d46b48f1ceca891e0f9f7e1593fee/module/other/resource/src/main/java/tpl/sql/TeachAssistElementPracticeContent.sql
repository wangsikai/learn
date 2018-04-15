## 根据模块id查找数据
#macro($resconFindByElement(elementId))
SELECT t.* FROM teachassist_e_practicecontent t WHERE t.practice_id = :elementId ORDER BY t.sequence ASC
#end

## 根据模块id删除数据
#macro($resconDeleteByElement(elementId))
DELETE FROM teachassist_e_practicecontent WHERE practice_id = :elementId
#end

## 根据多个模块id查找数据
#macro($resconFindByElements(elementIds))
SELECT t.* FROM teachassist_e_practicecontent t WHERE t.practice_id IN :elementIds ORDER BY t.sequence ASC
#end