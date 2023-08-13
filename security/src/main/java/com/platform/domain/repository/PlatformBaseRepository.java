package com.platform.domain.repository;

import com.platform.domain.entity.PlatformClient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Base repository interface , use if you need specific methods from here.Should provide query in the subclasses.
 *
 * @param <E>  Entity
 * @param <ID> ID
 * @author Teodor Maeli
 */
public interface PlatformBaseRepository<E extends PlatformClient, ID extends Number> extends JpaRepository<E, ID> {

    Optional<E> findByUserName(String username);
}
