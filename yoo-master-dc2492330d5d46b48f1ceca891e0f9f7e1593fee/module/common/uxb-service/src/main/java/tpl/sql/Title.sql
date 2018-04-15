#macro($findAllTitle())
SELECT * FROM title WHERE status = 0 ORDER BY sequence ASC,code ASC
#end