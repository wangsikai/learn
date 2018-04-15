##获取当前最大的code
#macro($getMaxCode())
SELECT max(code) code FROM user_channel
#end

##分页查询
#macro($query())
SELECT * FROM user_channel ORDER BY code DESC
#end