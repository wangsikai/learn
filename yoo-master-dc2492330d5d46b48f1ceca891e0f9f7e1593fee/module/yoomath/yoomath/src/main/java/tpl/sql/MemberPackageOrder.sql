#macro($getWXLastNotpayOrder(userId, memberPackageID))
select t.* from member_package_order t
 where t.type=0 and t.status=0 and t.payment_platform_code=1
 and t.user_id=:userId and t.member_package_id=:memberPackageID
 order by t.id DESC limit 1
#end
