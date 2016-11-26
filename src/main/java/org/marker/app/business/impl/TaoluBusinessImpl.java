package org.marker.app.business.impl;

import org.marker.app.business.TaoluBusiness;
import org.marker.app.domain.MessageResult;
import org.marker.app.service.TaoluService;
import org.marker.mushroom.service.impl.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ROOT on 2016/11/26.
 */
@Service
public class TaoluBusinessImpl implements TaoluBusiness{


    @Autowired
    TaoluService taoluService;
    @Override
    public Map<String, Object> get(int id) {
        return taoluService.get(id);
    }

    @Override
    public MessageResult pullData(int endId, int drection, String keyword) {

        List<Map<String, Object>>  data = taoluService.pullData(endId, drection, 4, keyword);

        return new MessageResult(data);
    }
}
