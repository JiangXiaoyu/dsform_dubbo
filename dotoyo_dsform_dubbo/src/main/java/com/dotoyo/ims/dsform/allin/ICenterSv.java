package com.dotoyo.ims.dsform.allin;

public interface ICenterSv {

	public String addCenter(CenterModel model) throws Exception;

	public CenterModel getCenterById(String id) throws Exception;

	public CenterModel getCenterByPk(CenterModel pk) throws Exception;

	public void deleteCenterByPk(CenterModel model) throws Exception;

	public void saveCenter(CenterModel model) throws Exception;

	public void updateCenter(CenterModel model) throws Exception;
}
