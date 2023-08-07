package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.laptop.shopping.dto.UserDto;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    @Query(value = "select ju.* from jhi_user ju join jhi_user_authority jua on ju.id  = jua.user_id\n" +
        "join jhi_authority ja on jua.authority_name = ja.name\n" +
        "where jua.authority_name = ?1 ", nativeQuery = true)
    List<UserDto> getListUserByRole(String roleName);
}
