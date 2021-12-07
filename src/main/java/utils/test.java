package utils;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class test {

  public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

//    ArrayList<Object> objects = Lists.newArrayList();
//    CollectionUtils.isNotEmpty(objects);
    Employee ee1 = new Employee("A",21,"it");
    Employee ee2 = new Employee("B",22,"it2");
    User user = new User();
//    BeanUtils.copyProperties(user,ee1);
//    System.out.println(user);
      BeanUtils.copyProperty(ee1,"name","123");
    System.out.println("ee1 = " + ee1);



  }


}
