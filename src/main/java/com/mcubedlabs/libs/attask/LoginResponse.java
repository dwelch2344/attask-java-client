package com.mcubedlabs.libs.attask;

import lombok.Getter;
import lombok.ToString;

/**
 * Model object encapsulating the response from logging in to an AtTask instance.
 * @author dwelch2344
 *
 */
@Getter @ToString
class LoginResponse {
	private String sessionID, userID, locale;
}
