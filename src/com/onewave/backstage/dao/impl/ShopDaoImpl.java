package com.onewave.backstage.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.onewave.backstage.dao.ShopDao;
import com.onewave.backstage.model.Operator;
import com.onewave.backstage.model.Shop;
import com.onewave.common.dao.impl.BaseDaoImpl;

@Repository("shopDao")
public class ShopDaoImpl extends BaseDaoImpl<Shop, String> implements ShopDao {


}
