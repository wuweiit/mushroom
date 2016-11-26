package org.marker.app.service;

import java.util.List;
import java.util.Map;

/**
 * Created by ROOT on 2016/11/26.
 */
public interface TaoluService {


    Map<String,Object> get(int id);

    List<Map<String, Object>> pullData(int endId, int drection, int size, String keyword);

}
