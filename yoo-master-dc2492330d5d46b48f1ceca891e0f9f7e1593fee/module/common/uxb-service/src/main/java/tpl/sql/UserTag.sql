#macro($selectUserTags(userId))
SELECT * FROM user_tag WHERE user_id = :userId
#end

#macro($selectUserTagByTagName(userId,name))
SELECT * FROM user_tag WHERE user_id = :userId AND tag = :name
#end

#macro($delUserTagByName(userId,name))
DELETE FROM user_tag WHERE user_id = :userId AND tag = :name
#end

#macro($delUserTagById(userId,id))
DELETE FROM user_tag WHERE user_id = :userId AND id = :id
#end

#macro($selectUserTagCount(userId))
SELECT count(id) FROM user_tag WHERE user_id = :userId
#end
