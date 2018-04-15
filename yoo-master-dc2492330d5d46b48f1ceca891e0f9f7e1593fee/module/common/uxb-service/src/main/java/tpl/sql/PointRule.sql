#macro($getByAction(action))
SELECT * FROM point_rule WHERE action = :action
#end

#macro($getAll())
SELECT * FROM point_rule
#end