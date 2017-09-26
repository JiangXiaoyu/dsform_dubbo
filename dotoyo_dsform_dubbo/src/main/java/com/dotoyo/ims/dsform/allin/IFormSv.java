package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Map;

/**
 * 自定义表单
 * 
 * @author xieshh
 * 
 */
public interface IFormSv {

	public FormRequestBo getFormRequestBo(FormRequestBo bo) throws Exception;

	public List<Map<String, Object>> getFormElementParam(String thisType,
			String elementId) throws Exception;

	public Map<String, Object> getFormElement(FormElementRequestBo bo)
			throws Exception;

	public List<Map<String, Object>> getFormElement(String code)
			throws Exception;

	public List<Map<String, Object>> queryFormElementByFormId(String code)
			throws Exception;

	public Map<String, Object> queryFormElementById(String id);

	/**
	 * 根据ID查询表单
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> queryFormById(String id);

	/**
	 * 更新表单
	 * 
	 * @param map
	 * @return
	 */
	public int updateForm(Map<String, String> map);

	public int updateFormElm(Map<String, String> map);

	public List<Map<String, Object>> queryFormElementByFormCode(String formCode);
	
	public List<Map<String, Object>> queryFormEleParByEleIdAndType(String elementId, String type) throws Exception;

}
