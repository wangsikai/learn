#macro($find(product,version,type,userId))
SELECT * FROM user_parameter 
WHERE product = :product 
#if(version)
AND version = :version
#end
AND type = :type 
AND user_id = :userId LIMIT 1
#end
