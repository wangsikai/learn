#macro($getSchoolByCode(code))
SELECT * FROM school WHERE code = :code
#end

#macro($getSchoolByCodes(codes))
SELECT * FROM school WHERE code IN :codes
#end

#macro($findAllSchool(status))
select * from school where 1=1
#if(status)
and status=:status
#end
order by code
#end

#macro($getSchoolByDistrictCode(districtCode,status))
select * from school where district_code = :districtCode and status= :status
#end

#macro($getOpenSchool(code))
select * from school where status =1 
#if(code)
AND code like :code
#end
order by code desc
#end




#macro($keyWordExist(keyWord,id))
select * from school where status = 1 and acronym=:keyWord 
#if(id)
and id !=:id
#end
#end


#macro($getSchoolByName(schoolName))
SELECT * FROM school WHERE name = :schoolName
#end


#macro($getSchoolByDistrict(districtCode,type))
select * from school where 1=1
#if(districtCode)
and district_code = :districtCode
#end
#if(type)
and type = :type
#end
order by code
#end

#macro($getSchoolByNameLike(schoolName))
SELECT * FROM school WHERE name LIKE :schoolName LIMIT 100
#end
