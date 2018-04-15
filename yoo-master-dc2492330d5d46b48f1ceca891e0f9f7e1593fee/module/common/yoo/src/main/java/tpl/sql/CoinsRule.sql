#macro($getByAction(action))
SELECT * FROM coins_rule WHERE action = :action
#end

#macro($getAll())
SELECT * FROM coins_rule
#end