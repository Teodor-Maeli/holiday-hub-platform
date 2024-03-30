package com.platform.util;

import java.util.List;
import java.util.Set;


/**
 * Helper class for collections and arrays.
 */
public class ObjectsHelper {

  public static boolean isEmpty(Object[] args) {
    return args == null || args.length == 0;
  }

  public static boolean isEmpty(List<?> list) {
    return isEmpty(list.toArray());
  }

  public static boolean isEmpty(Set<?> set) {
    return isEmpty(set.toArray());
  }

  public static boolean isNotEmpty(List<?> list) {
    return isNotEmpty(list.toArray());
  }

  public static boolean isNotEmpty(Set<?> set) {
    return isNotEmpty(set.toArray());
  }


  public static boolean isNotEmpty(Object[] args) {
    return args != null && args.length > 0;
  }

}
