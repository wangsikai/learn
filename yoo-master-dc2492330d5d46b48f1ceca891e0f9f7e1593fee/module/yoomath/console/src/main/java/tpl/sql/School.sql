## 查询开通了校本题库的学校列表
#macro($getQuestionSchoolList(bookId))
SELECT a.* FROM school a 
INNER JOIN question_school b ON a.id = b.school_id AND b.status = 0 
WHERE NOT EXISTS
(SELECT c.school_id FROM school_book c WHERE book_id = :bookId AND c.school_id = a.id and c.status = 0)
#end

## 查询学校列表
#macro($zyQuerySchool(code,type,schoolName))
select * from school where 1=1
	#if(code)
		and district_code like :code
	#end
	#if(type)
		and type =:type
	#end
	#if(schoolName)
		and name like:schoolName
	#end
#end

## 查询当前学校是否存在
#macro($zycExistSchool(schoolId))
	select count(id) from school where id = :schoolId
#end