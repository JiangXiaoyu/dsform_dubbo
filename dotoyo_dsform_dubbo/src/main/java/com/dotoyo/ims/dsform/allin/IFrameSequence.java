package com.dotoyo.ims.dsform.allin;

public interface IFrameSequence extends IFrame {
	/**
	 * 取得序列号:返回综合字串
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getSequence() throws Exception;

	/**
	 * 取得序列号:返回数字字串
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getSequence4Number() throws Exception;

}
