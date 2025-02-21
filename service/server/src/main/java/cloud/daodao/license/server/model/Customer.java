package cloud.daodao.license.server.model;

import cloud.daodao.license.common.model.Serializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * @author DaoDao
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(schema = "license_server", name = "customer")
public class Customer implements Serializer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_cipher")
    private String nameCipher;

    @Column(name = "name_hash")
    private String nameHash;

    @Column(name = "name_mask")
    private String nameMask;

    @Column(name = "phone_cipher")
    private String phoneCipher;

    @Column(name = "phone_hash")
    private String phoneHash;

    @Column(name = "phone_mask")
    private String phoneMask;

    @Column(name = "email_cipher")
    private String emailCipher;

    @Column(name = "email_hash")
    private String emailHash;

    @Column(name = "email_mask")
    private String emailMask;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "valid")
    private Boolean valid;

}
