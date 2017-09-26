package com.dotoyo.dsform.service.inter;

import com.dotoyo.dsform.model.AnnexModel;

public interface IAnnexSv {

	public String addAnnex(AnnexModel model) throws Exception;

	public AnnexModel getAnnexById(String id) throws Exception;
}
