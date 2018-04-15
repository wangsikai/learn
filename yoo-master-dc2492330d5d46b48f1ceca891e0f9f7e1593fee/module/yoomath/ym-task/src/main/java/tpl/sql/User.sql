#macro($taskQueryUser())
SELECT id FROM user WHERE id < :next ORDER BY id DESC
#end
