## 查询班级榜指定用户的点赞信息
#macro($getUserSchoolPraise(userId))
SELECT * FROM do_question_school_rank_praise 
WHERE user_id = :userId
ORDER BY id ASC
#end

## 根据rankId和userId取count
#macro($countByRankId(rankId,userId))
SELECT COUNT(*) FROM do_question_school_rank_praise 
WHERE user_id = :userId
AND rank_id = :rankId
#end

## 根据rankId和userId取RankPraise
#macro($getRankPraiseByRankId(rankId,userId))
SELECT * FROM do_question_school_rank_praise 
WHERE user_id = :userId
AND rank_id = :rankId
#end

## 游标查询数据
#macro($querySchoolRankPraiseByCursor(rankId))
SELECT * FROM do_question_school_rank_praise 
WHERE rank_id = :rankId
#if(next)
	AND id < :next
#end
ORDER BY id DESC
#end