package com.jim.quickjournal.model;

import java.util.Date;

public interface Journal {

  int getId();
  String getTitle();
  String getBody();
  Date getUpdatedOn();

}

