#macro($findAllTextbookCategory())
SELECT * FROM textbook_category WHERE status = 0 ORDER BY sequence ASC,code ASC
#end