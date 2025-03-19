package bip.bip_project.repository;

import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT new bip.bip_project.model.user.UserDto(u.id, u.username, u.role, u.password, u.email) FROM User u")
    List<UserDto> findAllUserDto();

    @Query("SELECT new bip.bip_project.model.user.UserDto(u.id, u.username, u.role, u.password, u.email) " +
            "FROM User u WHERE u.email = :email")
    UserDto findUserDtoByEmail(@Param("email") String email);

    Optional<User> findByEmail(@Param("email") String email);
}
