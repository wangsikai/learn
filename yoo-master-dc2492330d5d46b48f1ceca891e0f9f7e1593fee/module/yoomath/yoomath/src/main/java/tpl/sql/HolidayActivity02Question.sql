## 根据pkrecordId查询pk问题
#macro($getQuestion(code,pkRecordId))
SELECT * FROM holiday_activity_02_question WHERE activity_code = :code and pk_record_id = :pkRecordId
#end


