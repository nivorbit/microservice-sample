package com.nivorbit.customerservice;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
  private UUID id;
  private String firstName;
  private String lastName;
  private Integer age;
}
