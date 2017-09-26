
package com.dotoyo.dsform.dao.inter;

import java.util.List;
import java.util.Map;

/**
 * 琛ㄥ崟妯″潡鏁版嵁閾捐矾灞�
 *
 * @author xieshh
 *
 */
public interface IFormDao  extends IDao{
    /**
     * 分页查询
     * @param condition
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> queryByPage(Map<String,Object> condition) throws Exception;

    /**
     * 根据条件查询记录数
     * @param condition
     * @return
     * @throws Exception
     */
    public long queryRowCount(Map<String,Object> condition) throws Exception;
    /**
     * 鍙栧崟琛�
     *
     * @param condition
     * @return
     * @throws Exception
     */
    public Map<String, Object> getOnlyRow(Map<String, Object> condition)
            throws Exception;

    /**
     * query form element with report code
     * @param condition
     * @return
     */
    public List<Map<String,Object>> queryFormElementsByPage(Map<String,Object> condition);

    /**
     * query form with id and status
     * @param id
     * @return
     * @throws Exception
     */
    public Map<String, Object> getFormById(String id);

    /**
     * 更新表单
     * @param map
     * @return
     */
    public int updateForm(Map<String, String> map);
    public int updateFormElm(Map<String, String> map);
}
