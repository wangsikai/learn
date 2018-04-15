## 组卷记录查询
#macro($queryCustomExampapers(createId,status,key))
SELECT t.* FROM `custom_exampaper` t WHERE t.create_id = :createId AND t.status != 3
 #if(key)
  AND t.name LIKE :key
 #end 
 #if(status == 0)
  AND t.status = 0
 #else(status == 1)
  AND t.status = 1
 #else(status == 2)
  AND t.status = 2
  AND EXISTS ( SELECT tc.id FROM `custom_exampaper_class` tc
   INNER JOIN `homework_class` hc ON hc.`id`=tc.`class_id` AND hc.`status` = 0
   WHERE tc.`custom_exampaper_id`=t.id)
 #else(status == 9)
  AND (t.status = 0 OR t.status = 1 OR (t.status = 2 AND 
   EXISTS ( SELECT tc.id FROM `custom_exampaper_class` tc
   INNER JOIN `homework_class` hc ON hc.`id`=tc.`class_id` AND hc.`status` = 0
   WHERE tc.`custom_exampaper_id`=t.id)
  ))
 #end
 ORDER BY t.status ASC,t.update_at DESC
#end

## 组卷记录查询
#macro($queryCustomExampapersByCursor(createId))
SELECT t.* FROM custom_exampaper t WHERE t.create_id = :createId
AND 
(
	(
		status = 1 AND type = 0
	)
	OR
	(
		(
			(status = 1 AND type = 1) OR status = 2
		) AND EXISTS (
			SELECT 
				tc.id 
			FROM custom_exampaper_class tc INNER JOIN homework_class hc ON hc.id=tc.class_id AND hc.status = 0
			WHERE tc.custom_exampaper_id = t.id
		) 
	)
)
AND t.update_at < :next
ORDER BY t.update_at DESC
#end

## 查找所有组卷记录个数
#macro($countAllCustomExampapers(createId))
SELECT count(t.id) FROM custom_exampaper t WHERE t.create_id =:createId AND t.status != 3
#end