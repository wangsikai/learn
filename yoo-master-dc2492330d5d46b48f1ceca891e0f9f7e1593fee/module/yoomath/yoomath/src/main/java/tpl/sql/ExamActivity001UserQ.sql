## 获取用户所有的礼包数量
#macro($ymCountGift(userId,code))
SELECT count(*) FROM exam_activity_001_user_q WHERE user_id=:userId and activity_code =:code
#end

## 获取用户所有的礼包
#macro($ymGetGifts(userId,code))
SELECT * FROM exam_activity_001_user_q WHERE user_id=:userId and activity_code =:code
#end

## 获取用户当天的礼包
#macro($ymTodayGift(userId,code,dateBegin,dateEnd))
SELECT * FROM exam_activity_001_user_q WHERE user_id=:userId and activity_code =:code and create_at between :dateBegin and :dateEnd
#end