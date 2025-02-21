package cloud.daodao.license.server.service;

import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.model.PageData;
import cloud.daodao.license.common.model.PageParam;
import cloud.daodao.license.common.server.error.ServerError;
import cloud.daodao.license.common.server.model.IdParam;
import cloud.daodao.license.common.server.model.product.ProductData;
import cloud.daodao.license.common.server.model.product.ProductSearch;
import cloud.daodao.license.common.server.model.product.ProductUpsert;
import cloud.daodao.license.server.constant.CacheConstant;
import cloud.daodao.license.server.helper.DataHelper;
import cloud.daodao.license.server.model.Product;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ProductService {

    @Resource
    private ProductRepository productRepository;

    @Resource
    private DataHelper dataHelper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public PageData<ProductData> productSearch(@Valid @NotNull PageParam<ProductSearch> pageParam) {
        Integer page = pageParam.getPage();
        Integer size = pageParam.getSize();
        ProductSearch param = pageParam.getParam();
        List<PageParam.Match> matches = pageParam.getMatches();
        List<PageParam.Order> orders = pageParam.getOrders();

        ExampleMatcher matcher = dataHelper.matcher(matches);
        Sort sort = dataHelper.sort(orders);
        Product entity = new Product();
        BeanUtils.copyProperties(param, entity);
        Example<Product> example = Example.of(entity, matcher);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Product> dataPage = productRepository.findAll(example, pageable);
        Long total = dataPage.getTotalElements();
        List<ProductData> list = dataPage.getContent().stream().map(model -> {
            ProductData data = new ProductData();
            BeanUtils.copyProperties(model, data);
            return data;
        }).collect(Collectors.toList());

        return new PageData<>(page, size, total, list);
    }

    public ProductData productSelect(@Valid @NotNull IdParam param) {
        Long id = param.getId();
        String key = CacheConstant.PRODUCT + id;
        String value = stringRedisTemplate.opsForValue().get(key);

        if (null == value) {
            Optional<Product> optional = productRepository.findById(id);
            if (optional.isEmpty()) {
                throw new AppException(ServerError.PRODUCT_NOT_EXIST, String.valueOf(id));
            }
            value = optional.get().toJson();
            stringRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(10L));
        }

        Product entity = new Product().ofJson(value);
        ProductData data = new ProductData();
        BeanUtils.copyProperties(entity, data);
        return data;
    }

    public ProductData productUpsert(@Valid @NotNull ProductUpsert param) {
        Long id = param.getId();
        String code = param.getCode();
        String name = param.getName();
        Product entity;

        if (null == id) {
            productRepository.findByCode(code).ifPresent(model -> {
                throw new AppException(ServerError.PRODUCT_CODE_ALREADY_EXIST, code);
            });
            productRepository.findByName(name).ifPresent(model -> {
                throw new AppException(ServerError.PRODUCT_NAME_ALREADY_EXIST, name);
            });
            entity = new Product();
        } else {
            entity = productRepository.findById(id)
                    .orElseThrow(() -> new AppException(ServerError.PRODUCT_NOT_EXIST, String.valueOf(id)));
            if (!entity.getCode().equals(code)) {
                productRepository.findByCode(code).ifPresent(model -> {
                    throw new AppException(ServerError.PRODUCT_CODE_ALREADY_EXIST, code);
                });
            }
            if (!entity.getName().equals(name)) {
                productRepository.findByName(name).ifPresent(model -> {
                    throw new AppException(ServerError.PRODUCT_NAME_ALREADY_EXIST, name);
                });
            }
        }

        BeanUtils.copyProperties(param, entity);
        entity = productRepository.saveAndFlush(entity);

        String key = CacheConstant.PRODUCT + entity.getId();
        stringRedisTemplate.delete(key);

        ProductData data = new ProductData();
        BeanUtils.copyProperties(entity, data);
        return data;
    }

}
