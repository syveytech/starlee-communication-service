package com.retail.starlee.data.dao;

import java.sql.Connection;
import java.util.List;

import com.retail.starlee.models.UserDetail;

public interface ICommunicationInfoDao {
	public List<UserDetail> getRecordsToSendSms(Connection con);
}
