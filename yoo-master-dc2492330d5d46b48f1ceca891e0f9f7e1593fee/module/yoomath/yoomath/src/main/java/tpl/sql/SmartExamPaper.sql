##智能出卷:获取最新的试卷
#macro($getPaper(userId,textBookCode,smartDifficulty,status))
select * from smart_exam_paper where user_id = :userId and textbook_code = :textBookCode and smart_difficulty = :smartDifficulty and status =:status and paper_id = 0
#end

##换一换时候，更新试卷状态
#macro($updatePaperStatus(userId,textBookCode,smartDifficultys,status))
update smart_exam_paper 
set status = :status
where user_id = :userId and textbook_code = :textBookCode and smart_difficulty in (:smartDifficultys) and paper_id = 0  
#end

##智能出卷:往期练习回顾
#macro($queryHistoryPaperList(userId,textBookCode,status))
select * from smart_exam_paper 
where user_id = :userId and textbook_code = :textBookCode and status =:status and paper_id = 0
AND id < :next
ORDER BY commit_at DESC
#end


##智能出卷:往期练习回顾个数
#macro($getHistoryPaperCount(userId,textBookCode,status))
select count(id) from smart_exam_paper 
where user_id = :userId and textbook_code = :textBookCode and status =:status and paper_id = 0
#end

##智能出卷:往期练习回顾
#macro($getHistoryPaperAvg(userId,textBookCode,status))
select AVG(right_rate) from smart_exam_paper where user_id = :userId and textbook_code = :textBookCode and status =:status and paper_id = 0
#end

