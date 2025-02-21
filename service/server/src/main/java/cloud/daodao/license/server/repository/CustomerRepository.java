package cloud.daodao.license.server.repository;

import cloud.daodao.license.server.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author DaoDao
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByEmailHash(String emailHash);

    Optional<Customer> findByPhoneHash(String phoneHash);

}
