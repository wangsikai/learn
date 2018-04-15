## 获取channelSchool对象
#macro($getCSchool(schoolId,channelCode))
	select * from channel_school 
	where 1 = 1 
	#if(schoolId)
		and school_id = :schoolId 
	#end
	#if(channelCode)
		and channel_code = :channelCode
	#end
#end
