package com.nivorbit.fraudservice;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FraudCheckHistoryRepository {

  private List<FraudCheckHistory> fraudCheckHistories = new ArrayList<>();
  public void save(FraudCheckHistory fraudCheckHistory) {
    fraudCheckHistories.add(fraudCheckHistory);
  }
}
