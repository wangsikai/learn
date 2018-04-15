## 查询试卷地区码（非重复）
#macro($findDistrict(phaseCode,subjectCode))
SELECT DISTINCT t.district_code FROM exam_paper t  WHERE t.status = 2
#if(phaseCode)
AND t.phase_code = :phaseCode
#end
#if(subjectCode)
AND t.subject_code = :subjectCode
#end
AND t.own_school_id IS NULL
#end


## 查询试卷地区码（精品试卷，组卷管理专用）
#macro($findDistrictByExamGoods(phaseCode,subjectCode))
SELECT DISTINCT t.district_code FROM exam_paper t INNER JOIN resources_goods r  ON r.resources_id=t.id and r.status=2  WHERE t.status = 2
#if(phaseCode)
AND t.phase_code = :phaseCode
#end
#if(subjectCode)
AND t.subject_code = :subjectCode
#end
AND t.own_school_id IS NULL
#end


## 查询试卷地区码（我的收藏专用）
#macro($findDistrictByFavorite(phaseCode,subjectCode,createId))
SELECT DISTINCT t.district_code FROM exam_paper t INNER JOIN resources_goods_favorite rgf  
ON rgf.resources_id=t.id 
and rgf.create_id=:createId 
and rgf.type=0  INNER JOIN  resources_goods rg ON rg.id=rgf.resources_goods_id and rg.status=2
WHERE t.status = 2
#if(phaseCode)
AND t.phase_code = :phaseCode
#end
#if(subjectCode)
AND t.subject_code = :subjectCode
#end
AND t.own_school_id IS NULL
#end


##查询点击阶段点击次数最多的试卷
#macro($findRecommendByNdayHot(subjectCode,phaseCode,limit,isPutaway,nDay))
SELECT p.* FROM exam_paper_count t INNER JOIN exam_paper p ON t.exam_paper_id = p.id
#if(phaseCode)
AND p.phase_code = :phaseCode
#end
#if(subjectCode)
AND p.subject_code = :subjectCode
#end
#if(isPutaway==true)
INNER JOIN resources_goods rg ON rg.resources_id = p.id AND rg. STATUS = 2
#end
where t.n_day=:nDay
GROUP BY t.exam_paper_id
ORDER BY sum(t.click_count) desc
#if(limit)
LIMIT :limit
#end
#end

##查询点击阶段最新上架试卷
#macro($findNewPaper(subjectCode,phaseCode,limit))
SELECT p.* FROM exam_paper p INNER JOIN resources_goods rg ON rg.resources_id = p.id AND rg. 
STATUS = 2 where 1=1
#if(phaseCode)
AND p.phase_code = :phaseCode
#end
#if(subjectCode)
AND p.subject_code = :subjectCode
#end
ORDER BY rg.update_at desc 
#if(limit)
LIMIT :limit
#end
#end


