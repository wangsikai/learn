## 更新文件状态
#macro($updateFileStatus(id,status,updateAt))
UPDATE file SET status = :status,update_at = :updateAt WHERE id = :id 
#end

## 更新文件状态
#macro($updateFilesStatus(ids,status,updateAt))
UPDATE file SET status = :status,update_at = :updateAt WHERE id IN :ids 
#end

## 通过MD5值获取某个空间里面的文件
#macro($findByMd5(spaceId,md5))
SELECT * FROM file WHERE space_id = :spaceId AND md5 = :md5 AND reference = 1 LIMIT 1
#end

## 设置文件引用
#macro($referenceFile(id))
UPDATE file SET reference = 1 WHERE id = :id 
#end