#macro($selectTags(name))
SELECT * FROM tag WHERE status = 0
#if(name)
AND name = :name
#end
#end