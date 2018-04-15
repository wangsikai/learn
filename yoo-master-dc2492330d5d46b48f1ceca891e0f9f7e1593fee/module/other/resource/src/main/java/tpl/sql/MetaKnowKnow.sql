#macro($deleteByKnowpointCodeAndMetaCode(metaCode, knowpointCode))
DELETE FROM metaknow_know WHERE meta_code = :metaCode AND know_point_code = :knowpointCode
#end