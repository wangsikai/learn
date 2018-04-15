#macro($uptUserHonorGrade(userId,isupGrade))
UPDATE user_honor SET upgrade=:isupGrade WHERE user_id=:userId
#end
