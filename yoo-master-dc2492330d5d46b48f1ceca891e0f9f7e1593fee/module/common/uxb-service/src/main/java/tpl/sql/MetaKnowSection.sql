#macro($findBySectionCode(sectionCode))
SELECT * FROM metaknow_section WHERE section_code = :sectionCode
#end

#macro($findBySectionCodes(sectionCodes))
SELECT * FROM metaknow_section WHERE section_code in (:sectionCodes)
#end

#macro($findAll())
SELECT * FROM metaknow_section
#end

#macro($findByMetaknowCodes(metaknowCodes))
SELECT * FROM metaknow_section 
#if(metaknowCodes)
	WHERE meta_code in :(metaknowCodes)
#end
#end

#macro($getKnowPointsCodesByCode(sectionCodeLike))
SELECT ms.* FROM metaknow_section ms INNER JOIN  section s ON s.code =ms.section_code WHERE  s.code LIKE :sectionCodeLike
#end