package cloud.daodao.license.server.service;

import cloud.daodao.license.common.annotation.Mask;
import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.helper.HttpHelper;
import cloud.daodao.license.common.helper.SecurityHelper;
import cloud.daodao.license.common.model.PageData;
import cloud.daodao.license.common.model.PageParam;
import cloud.daodao.license.common.server.error.ServerError;
import cloud.daodao.license.common.server.model.IdParam;
import cloud.daodao.license.common.server.model.license.LicenseData;
import cloud.daodao.license.common.server.model.license.LicenseParam;
import cloud.daodao.license.common.server.model.license.LicenseSearch;
import cloud.daodao.license.common.server.model.license.LicenseUpsert;
import cloud.daodao.license.common.util.security.MaskUtil;
import cloud.daodao.license.server.constant.CacheConstant;
import cloud.daodao.license.server.helper.DataHelper;
import cloud.daodao.license.server.model.Customer;
import cloud.daodao.license.server.model.License;
import cloud.daodao.license.server.model.Product;
import cloud.daodao.license.server.repository.CustomerRepository;
import cloud.daodao.license.server.repository.LicenseRepository;
import cloud.daodao.license.server.repository.ProductRepository;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class LicenseService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private LicenseRepository licenseRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private DataHelper dataHelper;

    @Resource
    private SecurityHelper securityHelper;

    @Resource
    private HttpHelper httpHelper;

    public PageData<LicenseData> licenseSearch(@Valid @NotNull PageParam<LicenseSearch> pageParam) {
        Integer page = pageParam.getPage();
        Integer size = pageParam.getSize();
        LicenseSearch param = pageParam.getParam();
        List<PageParam.Match> matches = pageParam.getMatches();
        List<PageParam.Order> orders = pageParam.getOrders();

        securityHelper.encode(param);

        ExampleMatcher matcher = dataHelper.matcher(matches);
        Sort sort = dataHelper.sort(orders);
        License entity = new License();
        BeanUtils.copyProperties(param, entity);
        Example<License> example = Example.of(entity, matcher);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<License> dataPage = licenseRepository.findAll(example, pageable);
        Long total = dataPage.getTotalElements();
        List<LicenseData> list = dataPage.getContent().stream().map(model -> {
            LicenseData data = new LicenseData();
            BeanUtils.copyProperties(model, data);

            securityHelper.decode(data);

            return data;
        }).collect(Collectors.toList());

        return new PageData<>(page, size, total, list);
    }

    public LicenseData licenseSelect(@Valid @NotNull IdParam param) {
        Long id = param.getId();
        String key = CacheConstant.LICENSE + id;
        String value = stringRedisTemplate.opsForValue().get(key);

        if (null == value) {
            Optional<License> optional = licenseRepository.findById(id);
            if (optional.isEmpty()) {
                throw new AppException(ServerError.LICENSE_NOT_EXIST, String.valueOf(id));
            }
            value = optional.get().toJson();
            stringRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(10L));
        }

        License entity = new License().ofJson(value);
        LicenseData data = new LicenseData();
        BeanUtils.copyProperties(entity, data);

        securityHelper.decode(data);

        return data;
    }

    public LicenseData licenseUpsert(@Valid @NotNull LicenseUpsert param) {

        Long id = param.getId();
        Long productId = param.getProductId();
        Long customerId = param.getCustomerId();
        LocalDate periodEnd = param.getPeriodEnd();
        Boolean longTerm = param.getLongTerm();

        if (Boolean.FALSE.equals(longTerm)) {
            if (null == periodEnd) {
                throw new AppException(ServerError.LICENSE_PERIOD_END_NULL, param.toJson());
            }
        } else if (Boolean.TRUE.equals(longTerm)) {
            if (null != periodEnd) {
                throw new AppException(ServerError.LICENSE_PERIOD_END_INVALID, param.toJson());
            }
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ServerError.PRODUCT_NOT_EXIST, String.valueOf(productId)));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ServerError.CUSTOMER_NOT_EXIST, String.valueOf(customerId)));

        License entity;

        if (null == id) {
            licenseRepository.findByProductIdAndCustomerId(productId, customerId).ifPresent(model -> {
                throw new AppException(ServerError.PRODUCT_CUSTOMER_ALREADY_EXIST, product.getName() + " : " + customer.getNameMask());
            });

            String appId = Optional.ofNullable(param.getAppId()).orElse(generateAppId());
            String license = Optional.ofNullable(param.getLicense()).orElse(generateLicense());
            LocalDate periodStart = Optional.ofNullable(param.getPeriodStart()).orElse(LocalDate.now());
            if (null == longTerm && null == periodEnd) {
                longTerm = Boolean.TRUE;
            } else if (null == longTerm) {
                longTerm = Boolean.FALSE;
            }
            param.setAppId(appId);
            param.setLicense(license);
            param.setPeriodStart(periodStart);
            param.setPeriodEnd(periodEnd);
            param.setLongTerm(longTerm);

            entity = new License();
        } else {
            entity = licenseRepository.findById(id)
                    .orElseThrow(() -> new AppException(ServerError.LICENSE_NOT_EXIST, String.valueOf(id)));
            licenseRepository.findByProductIdAndCustomerId(productId, customerId).ifPresent(model -> {
                if (!model.getId().equals(id)) {
                    throw new AppException(ServerError.PRODUCT_CUSTOMER_ALREADY_EXIST, product.getName() + " : " + customer.getNameMask());
                }
            });
        }

        securityHelper.encode(param);

        BeanUtils.copyProperties(param, entity);
        entity = licenseRepository.saveAndFlush(entity);

        String key = CacheConstant.LICENSE + entity.getId();
        stringRedisTemplate.delete(key);

        LicenseData data = new LicenseData();
        BeanUtils.copyProperties(entity, data);

        securityHelper.decode(data);

        return data;
    }

    public void licenseSending(@Valid @NotNull IdParam param) {
        Long id = param.getId();
        License entity = licenseRepository.findById(id)
                .orElseThrow(() -> new AppException(ServerError.LICENSE_NOT_EXIST, String.valueOf(id)));

        String licenseMask = entity.getLicenseMask();
        Boolean valid = entity.getValid();
        if (!valid) {
            throw new AppException(ServerError.LICENSE_INVALID, licenseMask);
        }

        Boolean longTerm = entity.getLongTerm();
        LocalDate periodEnd = entity.getPeriodEnd();
        if (Boolean.FALSE.equals(longTerm)) {
            if (LocalDate.now().isAfter(periodEnd)) {
                throw new AppException(ServerError.LICENSE_EXPIRED, licenseMask);
            }
        }

        Long productId = entity.getProductId();
        Long customerId = entity.getCustomerId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ServerError.PRODUCT_NOT_EXIST, String.valueOf(productId)));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ServerError.CUSTOMER_NOT_EXIST, String.valueOf(customerId)));

        sendMail(product, customer, entity);

        sendSms(product, customer, entity);
    }

    private void sendMail(Product product, Customer customer, License license) {
        log.info("准备向客户发送 License 邮件...");
    }

    private void sendSms(Product product, Customer customer, License license) {
        log.warn("暂未支持短信发送...");
    }

    public LicenseData licenseIntrospect(@NotNull String appId, @Valid @NotNull LicenseParam param) {

        // String appId = httpHelper.getHeader(AppConstant.X_APP_ID);
        if (null == appId || appId.isEmpty()) {
            throw new AppException(ServerError.REQUEST_HEADER_REQUIRED_X_APP_ID);
        }
        licenseRepository.findByAppId(appId)
                .orElseThrow(() -> new AppException(ServerError.LICENSE_APP_ID_NOT_EXIST, appId));

        securityHelper.encode(param);

        String license = param.getLicense();
        String licenseHash = param.getLicenseHash();

        License entity = licenseRepository.findByLicenseHash(licenseHash)
                .orElseThrow(() -> new AppException(ServerError.LICENSE_NOT_EXIST, MaskUtil.mask(Mask.Security.LICENSE, license)));

        if (!entity.getAppId().equals(appId)) {
            throw new AppException(ServerError.APP_ID_AND_LICENSE_NOT_MATCH, appId + " : " + MaskUtil.mask(Mask.Security.LICENSE, license));
        }

        Boolean valid = entity.getValid();
        if (!valid) {
            throw new AppException(ServerError.LICENSE_INVALID, MaskUtil.mask(Mask.Security.LICENSE, license));
        }

        Boolean longTerm = entity.getLongTerm();
        LocalDate periodEnd = entity.getPeriodEnd();
        if (Boolean.FALSE.equals(longTerm)) {
            if (LocalDate.now().isAfter(periodEnd)) {
                throw new AppException(ServerError.LICENSE_EXPIRED, MaskUtil.mask(Mask.Security.LICENSE, license));
            }
        }

        LicenseData data = new LicenseData();
        BeanUtils.copyProperties(entity, data);

        securityHelper.decode(data);

        return data;
    }

    private static String generateAppId() {
        return randomUUID();
    }

    private static String generateLicense() {
        return randomUUID();
    }

    private static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
