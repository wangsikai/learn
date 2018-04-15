#macro($get(code))
SELECT * FROM lottery_activity WHERE code = :code
#end
