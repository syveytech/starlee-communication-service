package com.retail.starlee.data.dao;

import com.retail.starlee.models.UserDetail;

public interface IServiceProvider {
	public String sendPromotionalSms(UserDetail userDetail, String smsContent) throws Exception;
}
