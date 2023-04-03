package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderHashMap = new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap = new HashMap<>();
    HashMap<String,String> orderpartnerhashmap = new HashMap<>();
    HashMap<String, List<String>> hashmap = new HashMap<>();


    public void addOrder(Order order){
        String name = order.getId();
        orderHashMap.put(name,order);
        return;
    }
    public void addpartner(String partnerid){
        if(deliveryPartnerHashMap.containsKey(partnerid)){
            DeliveryPartner partner = deliveryPartnerHashMap.get(partnerid);
            deliveryPartnerHashMap.put(partnerid,partner);
        }else{
            DeliveryPartner partner = new DeliveryPartner(partnerid);
            deliveryPartnerHashMap.put(partnerid,partner);
        }
    }
    public void addordertopartner(String orderid,String partnerid){
        orderpartnerhashmap.put(orderid,partnerid);
        if(hashmap.containsKey(partnerid)){
            List<String> orders = hashmap.get(partnerid);
                orders.add(orderid);
                hashmap.put(partnerid,orders);

        }
        else {
            List<String> orders = new ArrayList<>();
            orders.add(orderid);
            hashmap.put(partnerid,orders);
        }


    }
    public Order getorderbyid(String orderid){
        if(orderHashMap.containsKey(orderid)){
            Order order = orderHashMap.get(orderid);
            return order;
        }
        else {
            return null;
        }

    }
    public DeliveryPartner getpartnerbyid(String partnerid){
        if(deliveryPartnerHashMap.containsKey(partnerid)){
            DeliveryPartner partner = deliveryPartnerHashMap.get(partnerid);
            return partner;
        }
        else {
            return null;
        }

    }
    public Integer getcountbypartnerid(String partnerid){
        if(hashmap.containsKey(partnerid)){
           Integer cnt = hashmap.get(partnerid).size();
           return cnt;
        }
        return 0;
    }
    public List<String> getordersbypartnerid(String partnerid){
        if(hashmap.containsKey(partnerid)){
            return hashmap.get(partnerid);
        }
        else {
            return new ArrayList<>();
        }
    }
    public List<String> getallorders(){
        List<String> ans = new ArrayList<>();
        for (String s : orderHashMap.keySet()){
            ans.add(s);
        }
        return ans;

    }
    public Integer getcount(){
        return orderHashMap.size()-hashmap.size();
    }
    public Integer countofordersleftaftergiventime(String time,String partnerid){
        int cnt = 0;
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(3));
        int giventime = hour*60+min;

        if(hashmap.containsKey(partnerid)){
            List<String> t = hashmap.get(partnerid);
            for( String orderid : t){
                if (orderHashMap.containsKey(orderid)){
                    Order order = orderHashMap.get(orderid);
                    if (order.getDeliveryTime() > giventime){
                        cnt += 1;
                    }
                }
            }
        }
        return cnt;
    }
    public void deletepartnerbyid(String partnerid){

        if(hashmap.containsKey(partnerid)){
            List<String> orders = hashmap.get(partnerid);
            for(String s : orders){
                if(orderpartnerhashmap.containsKey(orders)){
                    orderpartnerhashmap.remove(s);
                }

            }
            hashmap.remove(partnerid);
        }
        if (deliveryPartnerHashMap.containsKey(partnerid)){
            deliveryPartnerHashMap.remove(partnerid);
        }

    }
    public void deleteorderbyorderid(String orderid){
        orderHashMap.remove(orderid);

        if(orderpartnerhashmap.containsKey(orderid)){
            String partnerid = orderpartnerhashmap.get(orderid);
            List<String> orders = hashmap.get(partnerid);
            orders.remove(orderid);
            hashmap.put(partnerid,orders);

            DeliveryPartner partner = deliveryPartnerHashMap.get(partnerid);
            partner.setNumberOfOrders(orders.size());
        }
        if (orderHashMap.containsKey(orderid)){
            orderHashMap.remove(orderid);
        }

    }
    public String finddelieverytimebypartnerid(String partnerid){
        Integer time  = 0;
        if(hashmap.containsKey(partnerid)){
            List<String> orders = hashmap.get(partnerid);
            for (String s : orders){
                if(orderHashMap.containsKey(s)){
                    Order order = orderHashMap.get(s);
                    time = Math.max(time,order.getDeliveryTime());
                }
            }
        }
        Integer hour = time/60;
        Integer min = time%60;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(hour );
        stringBuilder.append(":" );
        stringBuilder.append(min);
        return stringBuilder.toString();

    }

}
