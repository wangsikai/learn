#macro($taskQueryUserByMod(modVal))
SELECT id FROM user WHERE id < :next AND user_type = 2 and mod(id,3) =:modVal
ORDER BY id DESC
#end
