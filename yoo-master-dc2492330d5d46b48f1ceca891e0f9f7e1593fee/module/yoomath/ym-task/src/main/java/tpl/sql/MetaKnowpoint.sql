## 查找知识点数据
#macro($findKnowpoint())
SELECT * FROM meta_knowpoint 
where status=0 and code > :next  ORDER BY code ASC
#end