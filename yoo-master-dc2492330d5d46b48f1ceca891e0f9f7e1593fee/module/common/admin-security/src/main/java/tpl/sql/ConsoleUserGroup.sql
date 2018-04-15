#macro($getUserGroupByGroupId(groupId))
SELECT * FROM con_user_group t WHERE t.group_id = :groupId
#end

#macro($deleteByGroupAndUser(userIds, groupId))
DELETE FROM con_user_group WHERE group_id = :groupId AND user_id IN :userIds
#end