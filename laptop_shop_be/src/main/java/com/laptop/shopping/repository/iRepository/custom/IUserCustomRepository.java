package com.laptop.shopping.repository.iRepository.custom;

import com.laptop.shopping.dto.SearchUser;
import com.laptop.shopping.dto.UserDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserCustomRepository {
    List<UserDto> getListUserByRole(String roleName);
    List<UserDto> searchListCustomer(SearchUser searchUser, Integer page, Integer pageSize);
    List<UserDto> totalListCustomer(SearchUser searchUser);
}
