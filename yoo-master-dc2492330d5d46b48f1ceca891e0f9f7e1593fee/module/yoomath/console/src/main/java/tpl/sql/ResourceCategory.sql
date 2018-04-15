##查询全部的资源类别
#macro($findAllCategory())
SELECT * FROM resource_category WHERE status = 0 ORDER BY sequence ASC, code ASC
#end