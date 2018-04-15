#macro($findLastestCode(knowpointCode))
SELECT * FROM meta_knowpoint t WHERE
t.code LIKE :knowpointCode ORDER BY t.code DESC LIMIT 1
#end

#macro($mgetListByKnowpointCodes(knowpointCodes))
SELECT t.* FROM meta_knowpoint t INNER JOIN metaknow_know m
on t.code = m.meta_code WHERE m.know_point_code in :knowpointCodes
ORDER BY t.code ASC
#end

##将待启用的转为已启用
#macro($resconTurnOn())
UPDATE meta_knowpoint SET status = 0 WHERE status = 1
#end