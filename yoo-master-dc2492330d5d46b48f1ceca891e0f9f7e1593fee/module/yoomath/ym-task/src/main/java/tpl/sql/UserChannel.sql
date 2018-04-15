##获取当前渠道下的用户数量
#macro($taskGetStaticChannelUserCount(code))
SELECT COUNT(id) cou,user_type FROM user WHERE user_channel_code = :code and status=0 GROUP BY user_type 
#end

##获取当前渠道下签约学校的数量
#macro($taskGetStaticChannelSchoolCount(code))
select COUNT(1) from channel_school where channel_code =:code and status =0
#end

##获取当前渠道下会员用户数量
#macro($taskGetStaticChannelVipCount(code,nowDate))
select  count(1) cou,u.user_type,um.member_type from user_member um INNER JOIN user u on um.user_id=u.id where u.user_channel_code =:code and u.status=0 and um.end_at>:nowDate and um.start_at<:nowDate 
GROUP BY user_type,member_type
#end

##获取所有渠道
#macro($taskAllUserChannel())
select * from user_channel
#end