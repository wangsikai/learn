package com.lanking.uxb.security.api;

import java.io.Serializable;
import java.util.Collection;

/**
 * 身份. <br>
 * User's Authentication.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2014年12月5日
 */
public interface Authentication extends Serializable {

	/**
	 * @return the authorities granted to the principal.
	 */
	Collection<String> getAuthorities();

	/**
	 * @return the role's relative URIs.
	 */
	Collection<String> getRelativeURIs();

	/**
	 * @return the user's name or other principal.
	 */
	Object getPrincipal();

	/**
	 * @return the user's password or other credentials.
	 */
	Object getCredentials();
}
