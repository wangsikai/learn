#macro($getByAction(action))
SELECT * FROM growth_rule WHERE action = :action
#end

#macro($getAll())
SELECT * FROM growth_rule
#end