package com.platform.domain.repository;

import com.platform.domain.entity.Client;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;


/**
 * Base repository interface, override if needed.
 *
 * @param <E>  Entity
 * @param <ID> ID
 * @author Teodor Maeli
 */
@NoRepositoryBean
public interface BaseClientRepository<E extends Client, ID> extends JpaRepository<E, ID> {

    @Query("SELECT e FROM #{#entityName} e "
        + "LEFT JOIN FETCH e.roles r "
        + "WHERE e.username = :username")
    Optional<E> findByUserName(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("DELETE #{#entityName} e "
        + "WHERE e.username = :username")
    int deleteByUserName(String username);

    @Transactional
    @Modifying
    @Query("UPDATE #{#entityName} e "
        + "SET e.password = :password "
        + "WHERE e.username = :username")
    int updatePasswordByUsername(@Param("username") String username,
                                 @Param("password") String password);

    @Transactional
    @Modifying
    @Query("UPDATE #{#entityName} e "
        + "SET e.enabled = :enabled "
        + "WHERE e.username = :username")
    int disableOrEnableByUsername(@Param("username") String username,
                                 @Param("enabled") Boolean enabled);

}
