#macro($getSpace(name))
SELECT * FROM file_space WHERE name =:name
#end

#macro($updateSpace(id,incrNum,incrSize))
UPDATE file_space SET file_count = file_count+ :incrNum,used_size = used_size + :incrSize WHERE id =:id
#end