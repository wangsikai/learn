## 按照业务biz和bizId查找分享
#macro($getShareLogByBiz(biz,bizId,userId,p0))
	select count(id) from share_log where biz =:biz and biz_id=:bizId
	#if(userId)
		and user_id = :userId
	#end
	#if(p0)
		and p0 = :p0
	#end
#end