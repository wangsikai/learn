#macro($resconGetUser(id))
select t.name, m.school_name as schoolName, t.id from user t INNER JOIN teacher m on t.id = m.id where t.id = :id
#end

#macro($resconGetUsers(ids))
select t.name, m.school_name as schoolName, t.id from user t INNER JOIN teacher m on t.id = m.id where t.id in :ids
#end