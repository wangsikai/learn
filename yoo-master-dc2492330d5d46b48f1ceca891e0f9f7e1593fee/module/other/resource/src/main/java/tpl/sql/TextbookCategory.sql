#macro($findLastestCode())
SELECT * FROM textbook_category t where t.status = 0 ORDER BY t.code DESC LIMIT 1
#end