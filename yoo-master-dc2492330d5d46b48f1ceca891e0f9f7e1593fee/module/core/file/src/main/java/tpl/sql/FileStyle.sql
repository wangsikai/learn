#macro($findFileStyle(long spaceId,int mode,int width,int height,int quality))
SELECT * FROM file_style 
WHERE status = 0
AND space_id =:spaceId AND mode=:mode AND width =:width AND height =:height AND quality=:quality
#end

#macro($findFileStyleByName(spaceId,name))
SELECT * FROM file_style 
WHERE status = 0
#if(spaceId)
AND space_id =:spaceId
#end
AND name =:name
#end