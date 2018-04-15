#macro($indexFindMetaKnowKnow(knowPointCode))
SELECT * FROM metaknow_know WHERE know_point_code = :knowPointCode
#end