package com.dotoyo.dsform.test;

/**
 * @author "shihuc"
 * @date   2017年5月3日
 */
import java.util.List;

import org.json.JSONObject;

/**
 * @author chengsh05
 *
 */
public interface IBuzzElementService {
    // 查询
    public List<BuzzElement> getItemInfo(JSONObject json) throws Exception;
    
    // 保存
    public int save(BuzzElement itemInfo) throws Exception;
    
    // 更新
    public void update(BuzzElement intemInfo) throws Exception;
}