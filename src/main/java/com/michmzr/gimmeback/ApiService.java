package com.michmzr.gimmeback;

import com.michmzr.gimmeback.security.SpringSecurityService;
import com.michmzr.gimmeback.user.User;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiService {
  protected final SpringSecurityService springSecurityService;

  public ApiService(SpringSecurityService springSecurityService) {
    this.springSecurityService = springSecurityService;
  }

  protected User getCurrentUser() {
    return springSecurityService.getCurrentUser();
  }
}
