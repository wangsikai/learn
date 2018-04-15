#macro($getParentCategory)
SELECT * FROM resource_category where pcode = -1 order by sequence ASC, code ASC
#end

#macro($findCategoryByParent(code))
select * from resource_category where pcode=:code order by sequence ASC, code ASC
#end

#macro($getSubCategories)
select * from resource_category where pcode!= -1 order by sequence ASC, code ASC
#end

#macro($getResParentType(code))
SELECT  * FROM resource_category WHERE CODE IN(SELECT pcode FROM resource_category WHERE CODE IN(:code))
order by sequence ASC, code ASC
#end
