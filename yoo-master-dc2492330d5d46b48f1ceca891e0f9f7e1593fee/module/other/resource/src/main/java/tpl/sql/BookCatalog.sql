#macro($listByBookVersion(bookVersionId, isDesc))
SELECT * FROM book_catalog t WHERE t.book_version_id=:bookVersionId
 ORDER BY t.level ASC,t.sequence
#if(isDesc == true)
 DESC
#else
 ASC
#end
#end

## 查询一个书本的目录
#macro(getBookCatalog(bookVersionId))
SELECT * FROM book_catalog WHERE book_version_id = :bookVersionId ORDER BY level ASC,sequence ASC
#end

#macro($getChildrenByPId(pid))
SELECT * FROM book_catalog WHERE pid=:pid ORDER BY sequence ASC
#end

#macro($deleteByIds(ids))
DELETE FROM book_catalog WHERE id IN (:ids)
#end

## 新增目录后更新后面的sequence
#macro($updateSequence(bookVersionId,noself,sequence,updateId,nowDate))
UPDATE book_catalog SET sequence = sequence + 1,update_id = :updateId,update_at = :nowDate 
WHERE book_version_id = :bookVersionId AND sequence >= :sequence
#if(noself)
AND id!=:noself
#end
#end

## 查找同层上一个目录
#macro($findLastCatalog(sequence,bookVersionId,level,pid))
SELECT * FROM book_catalog 
WHERE 
	book_version_id = :bookVersionId 
AND level = :level
AND pid = :pid
AND sequence < :sequence 
ORDER BY sequence DESC LIMIT 0,1
#end

## 查找同层下一个目录
#macro($findNextCatalog(sequence,bookVersionId,level,pid))
SELECT * FROM book_catalog 
WHERE 
	book_version_id = :bookVersionId 
AND level = :level
AND pid = :pid
AND sequence > :sequence 
ORDER BY sequence ASC LIMIT 0,1
#end

## 获取同级的最大sequence
#macro($getMaxSequenceSameParent(pid,bookVersionId))
SELECT MAX(sequence) FROM book_catalog WHERE book_version_id = :bookVersionId AND pid = :pid
#end

## 修改目录的level
#macro($updateLevel(ids,delta))
UPDATE book_catalog SET level = level + :delta WHERE id IN :ids
#end

## 将某个目录下的所有资源移动到另一个目录下
#macro($moveResource(bookVersionId, srcCatalogId, destCatalogId))
UPDATE book_question 
SET book_catalog_id = :destCatalogId 
WHERE book_version_id = :bookVersionId AND book_catalog_id = :srcCatalogId
#end

## 将某个目录下的所有资源移动到未分组中
#macro($move2NoCatalog(bookVersionId,catalogId, catalogIds))
UPDATE book_question SET book_catalog_id = 0 WHERE book_version_id = :bookVersionId
#if(catalogId)
AND book_catalog_id = :catalogId
#end
#if(catalogIds)
AND book_catalog_id in (:catalogIds)
#end
#end


##插入书本目录
#macro($insertBookCatalog(id,bookVersionId,createAt,createId,level,name,pid,sequence,updateAt,updateId))
INSERT INTO book_catalog(id,book_version_id,create_at,create_id,level,name,pid,sequence,update_at,update_id) VALUES (:id,:bookVersionId,:createAt,:createId,:level,:name,:pid,:sequence,:updateAt,:updateId)
#end