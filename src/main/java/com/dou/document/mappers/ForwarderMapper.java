package com.dou.document.mappers;

import com.dou.adm.configuration.TargetDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.dou.adm.shared.TargetDSName.DS_FINNONE;

@Component
public class ForwarderMapper {

    @Autowired
    private TATTrackingMapper mapper;

    @TargetDataSource(DS_FINNONE)
      public List getDataF1(Map param) {
        return mapper.getDataF1(param);
    }
}
