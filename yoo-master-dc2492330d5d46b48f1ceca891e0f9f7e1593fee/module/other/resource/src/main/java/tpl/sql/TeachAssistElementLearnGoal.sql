## 根据目录id获得数据
#macro($resconFindByCatalog(catalogId))
SELECT t.* FROM teachassist_e_learngoal t 
 INNER JOIN teachassist_catalog_element tce on tce.element_id=t.id AND tce.teachassist_catalog_id=:catalogId
 ORDER BY t.sequence ASC
#end