#macro($findAllDuty())
SELECT * FROM duty WHERE status = 0 ORDER BY sequence ASC,code ASC
#end