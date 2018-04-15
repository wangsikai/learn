##根据商品获取书本
#macro($getQuestionBySections(sectionCodes,version))
SELECT  a.section_code code,b.level,a.name,b.pcode,
		a.input_count total,a.pass_count pass,a.nopass_count nopass,
		a.nocheck_count editing,a.onepass_count onepass
FROM section_question_stat a 
INNER JOIN section b ON a.section_code = b.code
WHERE section_code IN :sectionCodes AND version = :version
#end