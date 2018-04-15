##分页获取teacher
#macro($taskGetAllByPage())
SELECT * FROM teacher WHERE id < :next ORDER BY id DESC
#end


