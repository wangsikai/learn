## 分页查找书本
#macro($indexQueryBookByPage())
select id from Book where status=0 order by id ASC
#end

## 查找书本实际个数
#macro($indexDataCount())
select count(id) from Book where status=0 order by id ASC
#end
