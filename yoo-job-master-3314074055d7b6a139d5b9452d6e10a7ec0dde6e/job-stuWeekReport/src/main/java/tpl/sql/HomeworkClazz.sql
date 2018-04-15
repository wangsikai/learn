#macro($queryClassIdsByWeek(modVal))
SELECT id FROM homework_class WHERE id < :next AND status = 0 and mod(id,3) =:modVal
ORDER BY id DESC
#end
