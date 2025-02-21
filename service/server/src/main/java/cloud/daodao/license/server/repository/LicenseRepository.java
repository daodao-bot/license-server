package cloud.daodao.license.server.repository;

import cloud.daodao.license.server.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author DaoDao
 */
@Repository
public interface LicenseRepository extends JpaRepository<License, Long>, JpaSpecificationExecutor<License> {

    Optional<License> findByAppId(String appId);

}
