package com.plantdata.kgcloud.service;

import com.plantdata.kgcloud.bean.ApiReturn;

public interface CallService {
    ApiReturn call(String id, String input);
}
