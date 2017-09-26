package com.dotoyo.ims.dsform.allin;

import java.util.Map;

/**
 * 
 * @author xieshh
 * 
 */
public interface IFrameForm {

	public Map<String, Object> getFormData(String formCode,
			Map<String, Object> result) throws Exception;
}
