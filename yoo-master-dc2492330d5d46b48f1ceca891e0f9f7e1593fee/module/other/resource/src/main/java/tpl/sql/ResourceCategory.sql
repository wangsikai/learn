#macro($findLastestCode(pcode))
SELECT * FROM resource_category t WHERE t.status = 0 AND
 t.pcode = :pcode ORDER BY t.code DESC
 LIMIT 1
#end

##查询全部的资源类别
#macro($findAll())
SELECT * FROM resource_category WHERE status = 0 ORDER BY sequence ASC, code ASC
#end