##通过区域名称查询
#macro($getDistrictByName(name))
SELECT * FROM district WHERE name LIKE :name ORDER BY code ASC
#end