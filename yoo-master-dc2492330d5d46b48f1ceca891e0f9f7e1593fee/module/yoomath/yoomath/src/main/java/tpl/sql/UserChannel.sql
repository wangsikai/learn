#macro($findByCode(code))
SELECT * FROM user_channel WHERE code = :code
#end

#macro($findByName(name))
SELECT * FROM user_channel WHERE name = :name
#end