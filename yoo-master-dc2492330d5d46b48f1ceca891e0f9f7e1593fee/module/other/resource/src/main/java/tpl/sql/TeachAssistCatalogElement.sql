##教辅目录资源

##获得教辅目录资源个数
#macro($getElementCount(catalogIds))
select count(id) from teachassist_catalog_element where teachassist_catalog_id in (:catalogIds)
#end

##根据目录Id删除资源从属关系
#macro($deleteByCatalogIds(catalogIds))
delete from teachassist_catalog_element where teachassist_catalog_id in (:catalogIds)
#end

##移动模块
#macro($moveElements(oldCatalog, newCatalog, newNum))
update teachassist_catalog_element set teachassist_catalog_id = :newCatalog, sequence = sequence + :newNum
 where teachassist_catalog_id = :oldCatalog
#end

## 根据目录id查找数据
#macro($findByCatalog(catalogId))
SELECT t.* FROM teachassist_catalog_element t WHERE t.teachassist_catalog_id = :catalogId ORDER BY t.sequence ASC
#end

## 更新排序
#macro($updateSequence(elementId,catalogId,sequence))
UPDATE teachassist_catalog_element SET sequence = :sequence WHERE teachassist_catalog_id = :catalogId AND element_id = :elementId
#end

## 根据模块id删除数据
#macro($deleteByElement(elementId))
DELETE FROM teachassist_catalog_element WHERE element_id = :elementId
#end