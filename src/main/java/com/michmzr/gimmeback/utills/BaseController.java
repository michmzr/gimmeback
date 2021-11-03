package com.michmzr.gimmeback.utills;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class BaseController {
  public ResponseEntity onItemNotFound(long id) {
    log.error("Not found item " + id);
    return ResponseEntity.noContent().build();
  }
}
