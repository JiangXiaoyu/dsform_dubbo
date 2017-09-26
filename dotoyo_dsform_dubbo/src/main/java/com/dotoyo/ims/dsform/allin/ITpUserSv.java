package com.dotoyo.ims.dsform.allin;

import java.util.Map;
import java.util.List;

public interface ITpUserSv {
	public TpAccountModel getAccountByPk(TpAccountModel pk) throws Exception;

	public TpUserModel loginUser(String userCode, String userPasswd,
			Map<String, Object> param) throws Exception;

	public void deleteAccountByPk(TpAccountModel model) throws Exception;

	public void saveAccount(TpAccountModel model) throws Exception;

	public void updateAccount(TpAccountModel model) throws Exception;
    
	public List<Map> selectAMList(Map condition) throws Exception;
	
	public TpUserModel getUserByPk(TpUserModel pk) throws Exception;

	public void deleteUserByPk(TpUserModel model) throws Exception;

	public void saveUser(TpUserModel model) throws Exception;

	public void updateUser(TpUserModel model) throws Exception;

	public TpPostModel getPostByPk(TpPostModel pk) throws Exception;

	public void deletePostByPk(TpPostModel model) throws Exception;

	public void savePost(TpPostModel model) throws Exception;

	public void updatePost(TpPostModel model) throws Exception;

	//
	public TpRoleModel getRoleByPk(TpRoleModel pk) throws Exception;

	public void deleteRoleByPk(TpRoleModel model) throws Exception;

	public void saveRole(TpRoleModel model) throws Exception;

	public void updateRole(TpRoleModel model) throws Exception;

	public TpMenuModel getMenuByPk(TpMenuModel pk) throws Exception;

	public void deleteMenuByPk(TpMenuModel model) throws Exception;

	public void saveMenu(TpMenuModel model) throws Exception;

	public void updateMenu(TpMenuModel model) throws Exception;

	public TpBusiModel getBusiByPk(TpBusiModel pk) throws Exception;

	public void deleteBusiByPk(TpBusiModel model) throws Exception;

	public void saveBusi(TpBusiModel model) throws Exception;

	public void updateBusi(TpBusiModel model) throws Exception;

	public TpRoleMenuModel getRoleMenuByPk(TpRoleMenuModel pk) throws Exception;

	public void deleteRoleMenuByPk(TpRoleMenuModel model) throws Exception;

	public void saveRoleMenu(TpRoleMenuModel model) throws Exception;

	public void updateRoleMenu(TpRoleMenuModel model) throws Exception;

	public void roleAuthSave(String ids,String roleId) throws Exception;
	
	public TpRoleBusiModel getRoleBusiByPk(TpRoleBusiModel pk) throws Exception;

	public void deleteRoleBusiByPk(TpRoleBusiModel model) throws Exception;

	public void saveRoleBusi(TpRoleBusiModel model) throws Exception;

	public void updateRoleBusi(TpRoleBusiModel model) throws Exception;

	public TpAppModel getAppByPk(TpAppModel pk) throws Exception;

	public void deleteAppByPk(TpAppModel model) throws Exception;

	public void saveApp(TpAppModel model) throws Exception;

	public void updateApp(TpAppModel model) throws Exception;
	////////////////jim///////////////////////////////////////////
	public List<TpMenuModel> getAll()throws Exception;
	
	public List<TpMenuModel> selectModelList(TpMenuModel record)throws Exception;
	
	public int deleteMenu(TpMenuModel model)throws Exception;
}
