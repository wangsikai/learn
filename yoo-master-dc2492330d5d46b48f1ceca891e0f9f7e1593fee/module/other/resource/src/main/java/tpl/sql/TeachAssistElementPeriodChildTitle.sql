## 根据目录id查找数据
#macro($resconFindByCatalog(catalogId))
SELECT t.* FROM teachassist_e_periodchildtitle t 
 INNER JOIN teachassist_catalog_element tce on tce.element_id=t.id AND tce.teachassist_catalog_id=:catalogId
 ORDER BY t.sequence ASC
#end