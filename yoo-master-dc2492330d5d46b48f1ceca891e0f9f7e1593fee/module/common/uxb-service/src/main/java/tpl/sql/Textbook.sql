## 获取所有教材
#macro($findAll())
SELECT * FROM textbook WHERE status = 0 ORDER BY sequence ASC,code ASC
#end

## 按照阶段学科获取所有教材
#macro($find(phaseCode,subjectCode,categoryCode,categoryCodes,yoomathStatus))
SELECT * FROM textbook WHERE status = 0 AND phase_code = :phaseCode
#if(yoomathStatus)
AND yoomath_status = 0
#end
#if(subjectCode)
AND subject_code = :subjectCode
#end
#if(categoryCode)
AND category_code = :categoryCode
#end
#if(categoryCodes)
AND category_code IN (:categoryCodes)
#end
ORDER BY sequence ASC,code ASC
#end