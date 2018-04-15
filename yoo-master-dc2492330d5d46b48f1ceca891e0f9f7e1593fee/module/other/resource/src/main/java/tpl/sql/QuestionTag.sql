#macro($listAll(status, questionTagType))
SELECT * FROM question_tag where 1=1
#if(status)
	and status=:status
#end
#if(questionTagType)
	and type=:questionTagType
#end
order by sequence ASC
#end