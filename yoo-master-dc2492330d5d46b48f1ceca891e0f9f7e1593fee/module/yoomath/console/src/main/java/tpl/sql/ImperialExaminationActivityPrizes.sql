##通过活动code查询prizes
#macro($findPrizesByCode(code))
select * from imperial_exam_activity_prizes t  
where t.activity_code=:code
#end