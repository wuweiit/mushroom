package org.marker.app.business;

import org.marker.app.domain.MessageResult;

import java.util.List;
import java.util.Map;

/**
 * Created by ROOT on 2016/11/26.
 */
public interface TaoluBusiness {
    Map<String,Object> get(int id);


    /**
     * 拉动数据
     * @param endId
     * @param drection
     * @param keyword
     * @return
     */
    MessageResult pullData(int endId, int drection, String keyword);
}
