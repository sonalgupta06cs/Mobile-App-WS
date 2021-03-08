/**
 * 
 */
package com.appsdevelopeblog.app.ws.ui.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SonaSach
 *
 */
@Getter
@Setter
public class SearchApiResponse<T> extends ApiResponse {
	
	private static final long serialVersionUID = 4624202650271693604L;
	private long total;

}
