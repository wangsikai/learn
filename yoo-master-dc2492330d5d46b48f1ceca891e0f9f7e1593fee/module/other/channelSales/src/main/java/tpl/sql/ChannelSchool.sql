## 根据登录名列表查找数据
#macro($csSchoolByChannel(codes))
select t.* from channel_school t where t.channel_code in :codes and status =0
#end

## 根据学校ID查询绑定时间
#macro($csGetSchoolBindDate(schoolId))
select t.create_at from channel_school t where t.school_id=:schoolId and status=0
#end