## 根据性别获取tag
#macro($findList(sex))
SELECT * FROM teachersday_activity_01_tag WHERE sex = :sex or sex = 2 order by code
#end
