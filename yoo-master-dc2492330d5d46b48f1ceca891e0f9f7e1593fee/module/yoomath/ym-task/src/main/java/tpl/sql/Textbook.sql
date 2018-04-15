##游标查询textbook
#macro($queryTextbookList())
	SELECT * FROM textbook WHERE status = 0 AND code < :next ORDER BY code DESC
#end