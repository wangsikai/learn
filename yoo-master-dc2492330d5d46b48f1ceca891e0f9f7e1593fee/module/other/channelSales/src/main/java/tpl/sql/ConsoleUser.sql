## 查询创建会员兑换卡管理员
#macro($csGetCreateUsers())
select c.* from member_package_card m INNER JOIN con_user c on m.create_id=c.id GROUP BY  m.create_id
#end


