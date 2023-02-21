package com.gupern.pnav.common;

import com.alibaba.fastjson.JSONObject;

public interface CommonService {
    Object loginOrRegister(JSONObject dto);
}
