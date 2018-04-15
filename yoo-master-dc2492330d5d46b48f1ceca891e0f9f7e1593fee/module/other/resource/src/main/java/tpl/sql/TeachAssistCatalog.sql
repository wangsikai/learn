##教辅目录

##按顺序获得教辅目录列表
#macro($listTeachAssistCatalogCatalog(teachAssistVersionId))
select * from teachassist_catalog where teachassist_version_id=:teachAssistVersionId order by level ASC,sequence ASC
#end

## 查找同层指定顺序的目录
#macro($findSpecifyCatalog(sequence,teachAssistVersionId,level,pid))
SELECT * FROM teachassist_catalog 
WHERE 
	teachassist_version_id = :teachAssistVersionId 
AND level = :level
AND pid = :pid
AND sequence =:sequence
LIMIT 1
#end

##获得子目录集合
#macro($getChildrenByPId(pid))
select * from teachassist_catalog where pid=:pid order by sequence ASC
#end

##获得子目录个数
#macro($getChildrenCountByPId(pid))
select count(id) from teachassist_catalog where pid=:pid
#end

##更新下移目录的子目录层级+1
#macro($updateDownChildrensLevel(pid))
UPDATE teachassist_catalog a,
(
 SELECT t2.id AS id FROM teachassist_catalog t1
 INNER JOIN teachassist_catalog t2 ON t2.pid=t1.id
 WHERE t1.pid=:pid
 UNION
 SELECT t.id AS id FROM teachassist_catalog t
 WHERE t.pid=:pid
) b SET a.LEVEL = a.LEVEL + 1 WHERE a.id=b.id
#end

##移动更新资源从属
#macro($moveResource(teachassistVersionId, srcCatalogId, destCatalogId))
update teachassist_catalog_element set teachassist_catalog_id=:destCatalogId
 where teachassist_catalog_id=:srcCatalogId
#end

##获得所有子菜单ID集合
#macro($allChildren(pid, pidLevel))
#if(pidLevel == 1)
 SELECT t2.id FROM teachassist_catalog t2 WHERE t2.pid=:pid
 UNION
 SELECT t3.id FROM teachassist_catalog t2
 INNER JOIN teachassist_catalog t3 ON t3.pid=t2.id
 WHERE t2.pid=:pid
#end
#if(pidLevel == 2)
 SELECT t3.id FROM teachassist_catalog t2
 INNER JOIN teachassist_catalog t3 ON t3.pid=t2.id
 WHERE t2.pid=:pid
#end
#end

##删除目录
#macro($deleteByIds(catalogIds))
delete from teachassist_catalog where id in (:catalogIds)
#end

## 新增、删除目录后更新后面的sequence
#macro($updateSequence(teachassistVersionId,sequence,updateId, incr, nowDate))
UPDATE teachassist_catalog SET sequence = sequence + :incr,update_id = :updateId,update_at = :nowDate 
	WHERE teachassist_version_id = :teachassistVersionId AND sequence > :sequence
#end

##获取最底层的所有目录集合
#macro($findLowestList(teachAssistVersionId))
SELECT a.* FROM teachassist_catalog a WHERE a.id NOT IN 
(SELECT b.pid FROM teachassist_catalog b) 
AND a.teachassist_version_id =:teachAssistVersionId
ORDER BY a.pid ASC,a.sequence ASC
#end

##教辅校验
#macro($updateCheckStatus(id,status))
UPDATE teachassist_catalog SET check_status =:status WHERE id =:id
#end