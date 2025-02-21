package cloud.daodao.license.server.model;

import cloud.daodao.license.common.model.Serializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author DaoDao
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "license_server", name = "license")
public class License implements Serializer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "license_cipher")
    private String licenseCipher;

    @Column(name = "license_hash")
    private String licenseHash;

    @Column(name = "license_mask")
    private String licenseMask;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @Column(name = "long_term")
    private Boolean longTerm;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "valid")
    private Boolean valid;

}
