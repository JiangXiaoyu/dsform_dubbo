
package com.dotoyo.dsform.dao.inter;

import java.util.List;
import java.util.Map;

import com.dotoyo.ims.dsform.allin.FormElmParModel;

/**
 * 表单模块数据链路层
 * 
 * @author xieshh
 * 
 */
public interface IFormModelDao  extends IDao{
	/**
	 * 取分页记录
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryByPage(Map<String,Object> condition) throws Exception;

	/**
	 * 取所有符合条件的记录数量
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public long queryRowCount(Map<String,Object> condition) throws Exception;
	/**
	 * 根据报表编码查询报表信息
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryFormByCode(String code) throws Exception;

	/**
	 * 根据报表编号查询元素列表
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryFormElementByReportId(
			String reportId) throws Exception;

	/**
	 * 根据元素编号查询参数列表
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryFormElementParamByElementId(
			String elementId) throws Exception;
	
	public List<Map<String, Object>> queryFormElementParam(Map<String,Object> condition) throws Exception;

	public Map<String, Object> queryFormElement(Map<String,Object> condition)throws Exception;

	public List<Map<String, Object>> queryFormElementByFormId(String str);

    public  Map<String, Object> queryFormElementById(String id);
    
    /**
     * 获取表单结点的详细详情
     * @param map
     * @return
     */
	public List<FormElmParModel> queryFormElePar(Map<String, String> map);

	public List<Map<String, Object>> queryFormElementByFormCode(String formCode);

	public List<Map<String, Object>> queryFormEleParByEleIdAndType(String elementId, String type);
}
