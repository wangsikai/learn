#macro($hwProxyResponse(id,response,responseAt))
UPDATE hw_record SET response = :response,response_at = :responseAt WHERE id = :id
#end