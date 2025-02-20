package cloud.daodao.license.common.controller;

import cloud.daodao.license.common.api.IndexApi;
import cloud.daodao.license.common.config.CommonConfig;
import cloud.daodao.license.common.constant.AppConstant;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaoDao
 */
@RestController
public class IndexController implements IndexApi {

    @Resource
    private CommonConfig commonConfig;

    public String index() {
        String slogan = commonConfig.getSlogan();
        return (null != slogan && !slogan.isEmpty()) ? slogan : AppConstant.SLOGAN;
    }

}
