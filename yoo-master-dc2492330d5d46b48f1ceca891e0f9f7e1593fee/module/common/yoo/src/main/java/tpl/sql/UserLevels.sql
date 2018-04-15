##获取用户等级信息
#macro($getUserLevel(level,product))
SELECT * FROM user_levels 
WHERE level = :level AND product = :product
#end

##根据等级获取信息
#macro($getUserLevels(startLevel,endLevel,product))
SELECT * FROM user_levels 
WHERE product = :product
AND level > :startLevel
AND level <= :endLevel
#end

##根据成长值获取对应等级
#macro($getLevelByGrowth(growth))
SELECT t.level FROM user_levels t WHERE t.max_growth_value>=:growth AND t.min_growth_value<=:growth
#end
