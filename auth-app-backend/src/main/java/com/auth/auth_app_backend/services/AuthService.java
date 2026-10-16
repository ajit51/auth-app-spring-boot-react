package com.auth.auth_app_backend.services;

import com.auth.auth_app_backend.dtos.UserDto;

public interface AuthService {
  UserDto registerUser(UserDto userDto);

  //login user
}
