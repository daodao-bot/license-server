package cloud.daodao.license.server.service;

import cloud.daodao.license.common.error.AppException;
import cloud.daodao.license.common.helper.SecurityHelper;
import cloud.daodao.license.common.model.PageData;
import cloud.daodao.license.common.model.PageParam;
import cloud.daodao.license.common.server.error.ServerError;
import cloud.daodao.license.common.server.model.IdParam;
import cloud.daodao.license.common.server.model.customer.CustomerData;
import cloud.daodao.license.common.server.model.customer.CustomerSearch;
import cloud.daodao.license.common.server.model.customer.CustomerUpsert;
import cloud.daodao.license.server.constant.CacheConstant;
import cloud.daodao.license.server.helper.DataHelper;
import cloud.daodao.license.server.model.Customer;
import cloud.daodao.license.server.repository.CustomerRepository;
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
public class CustomerService {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private DataHelper dataHelper;

    @Resource
    private SecurityHelper securityHelper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public PageData<CustomerData> customerSearch(@Valid @NotNull PageParam<CustomerSearch> pageParam) {
        Integer page = pageParam.getPage();
        Integer size = pageParam.getSize();
        CustomerSearch param = pageParam.getParam();
        List<PageParam.Match> matches = pageParam.getMatches();
        List<PageParam.Order> orders = pageParam.getOrders();

        securityHelper.encode(param);

        ExampleMatcher matcher = dataHelper.matcher(matches);
        Sort sort = dataHelper.sort(orders);
        Customer entity = new Customer();
        BeanUtils.copyProperties(param, entity);
        Example<Customer> example = Example.of(entity, matcher);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Customer> dataPage = customerRepository.findAll(example, pageable);
        Long total = dataPage.getTotalElements();
        List<CustomerData> list = dataPage.getContent().stream().map(model -> {
            CustomerData data = new CustomerData();
            BeanUtils.copyProperties(model, data);

            securityHelper.decode(data);

            return data;
        }).collect(Collectors.toList());

        return new PageData<>(page, size, total, list);
    }

    public CustomerData customerSelect(@Valid @NotNull IdParam param) {
        Long id = param.getId();
        String key = CacheConstant.CUSTOMER + id;
        String value = stringRedisTemplate.opsForValue().get(key);

        if (null == value) {
            Optional<Customer> optional = customerRepository.findById(id);
            if (optional.isEmpty()) {
                throw new AppException(ServerError.CUSTOMER_NOT_EXIST, String.valueOf(id));
            }
            value = optional.get().toJson();
            stringRedisTemplate.opsForValue().set(key, value, Duration.ofMinutes(10L));
        }

        Customer entity = new Customer().ofJson(value);
        CustomerData data = new CustomerData();
        BeanUtils.copyProperties(entity, data);

        securityHelper.decode(data);

        return data;
    }

    public CustomerData customerUpsert(@Valid @NotNull CustomerUpsert param) {

        securityHelper.encode(param);

        Long id = param.getId();
        String emailHash = param.getEmailHash();
        String email = param.getEmail();
        String phoneHash = param.getPhoneHash();
        String phone = param.getPhone();
        Customer entity;

        if (null == id) {
            customerRepository.findByEmailHash(emailHash).ifPresent(model -> {
                throw new AppException(ServerError.CUSTOMER_EMAIL_ALREADY_EXIST, email);
            });
            customerRepository.findByPhoneHash(phoneHash).ifPresent(model -> {
                throw new AppException(ServerError.CUSTOMER_PHONE_ALREADY_EXIST, phone);
            });
            entity = new Customer();
        } else {
            entity = customerRepository.findById(id).orElseThrow(() -> new AppException(ServerError.CUSTOMER_NOT_EXIST, String.valueOf(id)));
            if (!entity.getEmailHash().equals(emailHash)) {
                customerRepository.findByEmailHash(emailHash).ifPresent(model -> {
                    throw new AppException(ServerError.CUSTOMER_EMAIL_ALREADY_EXIST, email);
                });
            }
            if (!entity.getPhoneHash().equals(phoneHash)) {
                customerRepository.findByPhoneHash(phoneHash).ifPresent(model -> {
                    throw new AppException(ServerError.CUSTOMER_PHONE_ALREADY_EXIST, phone);
                });
            }
        }

        BeanUtils.copyProperties(param, entity);
        entity = customerRepository.saveAndFlush(entity);

        String key = CacheConstant.CUSTOMER + entity.getId();
        stringRedisTemplate.delete(key);

        CustomerData data = new CustomerData();
        BeanUtils.copyProperties(entity, data);

        securityHelper.decode(data);

        return data;
    }

}
