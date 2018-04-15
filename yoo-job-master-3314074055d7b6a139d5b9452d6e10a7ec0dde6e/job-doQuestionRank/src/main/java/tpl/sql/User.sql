#macro($taskQueryUser())
SELECT id FROM user WHERE id < :next AND user_type = 2 ORDER BY id DESC
#end
