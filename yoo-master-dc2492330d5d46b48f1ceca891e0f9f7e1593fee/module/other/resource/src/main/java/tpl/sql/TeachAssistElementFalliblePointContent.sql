## 根据元素id查找数据
#macro($resconFindByElement(elementId))
SELECT t.* FROM teachassist_e_fallpoint_content t WHERE t.fallpoint_id = :elementId ORDER BY t.sequence ASC
#end

## 根据元素id删除数据
#macro($resconDeleteByElement(elementId))
DELETE FROM teachassist_e_fallpoint_content WHERE fallpoint_id = :elementId
#end

## 根据多个模块id查找数据
#macro($resconFindByElements(elementIds))
SELECT t.* FROM teachassist_e_fallpoint_content t WHERE t.fallpoint_id IN :elementIds ORDER BY t.sequence ASC
#end
