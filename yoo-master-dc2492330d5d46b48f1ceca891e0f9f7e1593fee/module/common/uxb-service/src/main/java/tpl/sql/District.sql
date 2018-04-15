#macro($getDistrictByLevel(level))
select * from district where level=:level
#end

#macro($getDistrictByPcode(pcode))
select * from district where pcode=:pcode
#end

#macro($getPDistrict(code))
select * from district where code=(select pcode from district where code=:code)
#end

#macro($getAll())
select * from district
#end


