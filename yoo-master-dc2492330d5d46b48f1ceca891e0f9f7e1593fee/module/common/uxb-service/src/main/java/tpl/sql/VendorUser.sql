#macro($getVendorUser(vendorId,userTypes,status))
SELECT * FROM vendor_user WHERE vendor_id =:vendorId
#if(userTypes)
 AND type in (:userTypes)
#end
#if(status)
 AND status = :status
#end
 order by name ASC
#end


#macro($getVendorUserCount(vendorId,status))
SELECT count(*) FROM vendor_user WHERE vendor_id =:vendorId
#if(status)
AND status = :status 
#end
#end