## 根据章节查询其课时
#macro($zyFindBySectionCode(sectionCode))
SELECT t.* FROM daily_practise_period t WHERE t.section_code = :sectionCode
#end