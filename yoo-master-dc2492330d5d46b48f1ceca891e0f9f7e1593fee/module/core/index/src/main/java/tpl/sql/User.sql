#macro($indexQueryByPage())
SELECT * FROM user WHERE (status = 0 OR status = 3)
#end

#macro($indexmget(ids))
SELECT * FROM user WHERE (status = 0 OR status = 3) AND id IN :ids
#end