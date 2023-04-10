package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderMap = new HashMap<>();
    HashMap<String,DeliveryPartner> partnerMap = new HashMap<>();
    HashMap<String,String> orderToPartnerMap = new HashMap<>();
    HashMap<String, List<String>> partnerToOrderMap= new HashMap<>();


    public void addOrder(Order order){
        String name = order.getId();

        if(name != null)orderMap.put(name,order);
        return;
    }
    public void addpartner(String partnerid){
            DeliveryPartner partner = new DeliveryPartner(partnerid);
            partnerMap.put(partner.getId(),partner);

    }
    public void addordertopartner(String orderid,String partnerid){
        List<String> list = partnerToOrderMap.getOrDefault(partnerid, new ArrayList<>());
        list.add(orderid);
        partnerToOrderMap.put(partnerid, list);
        orderToPartnerMap.put(orderid, partnerid);
        DeliveryPartner partner = partnerMap.get(partnerid);
        partner.setNumberOfOrders(list.size());

    }
    public Order getorderbyid(String orderid){
        for (String id : orderMap.keySet()){
            if (id.equals(orderid)){
                return orderMap.get(id);
            }
        }
        return null;

    }
    public DeliveryPartner getpartnerbyid(String partnerid){
       for (String id : partnerMap.keySet()){
           if (id.equals(partnerid)){
               return partnerMap.get(id);
           }
       }
       return null;

    }
    public int getcountbypartnerid(String partnerid){
        if(partnerToOrderMap.containsKey(partnerid)){
           int cnt = partnerToOrderMap.get(partnerid).size();
           return cnt;
        }
        else {
            return 0;
        }
    }
    public List<String> getordersbypartnerid(String partnerid){
        List<String> orderlist = new ArrayList<>();
        if(partnerToOrderMap.containsKey(partnerid)) {
            orderlist = partnerToOrderMap.get(partnerid);
        }
        return orderlist;
    }
    public List<String> getallorders(){
        List<String> ans = new ArrayList<>();
        for (String s : orderMap.keySet()){
            ans.add(s);
        }
        return ans;

    }
    public int getcount(){
        return orderMap.size() - orderToPartnerMap.size();
    }
    public Integer countofordersleftaftergiventime(String time,String partnerid){
        int cnt = 0;
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(3));
        int giventime = hour*60+min;

        if(partnerToOrderMap.containsKey(partnerid)){
            List<String> t = partnerToOrderMap.get(partnerid);
            for( String orderid : t){
                if (orderMap.containsKey(orderid)){
                    Order order = orderMap.get(orderid);
                    if (order.getDeliveryTime() > giventime){
                        cnt ++;
                    }
                }
            }
        }
        return cnt;
    }
    public void deletepartnerbyid(String partnerid){

        if(partnerToOrderMap.containsKey(partnerid)){
            List<String> orders = partnerToOrderMap.get(partnerid);
            for(String s : orders){
                if(orderToPartnerMap.containsKey(s)){
                    orderToPartnerMap.remove(s);
                }

            }
            partnerToOrderMap.remove(partnerid);
        }
        if (partnerMap.containsKey(partnerid)){
            partnerMap.remove(partnerid);
        }

    }
    public void deleteorderbyorderid(String orderid){
        orderMap.remove(orderid);

        if(orderToPartnerMap.containsKey(orderid)){
            String partnerid = orderToPartnerMap.get(orderid);
            List<String> orders = partnerToOrderMap.get(partnerid);
            orders.remove(orderid);
            partnerToOrderMap.put(partnerid,orders);

            DeliveryPartner partner = partnerMap.get(partnerid);
            partner.setNumberOfOrders(orders.size());
        }
        if (orderMap.containsKey(orderid)){
            orderMap.remove(orderid);
        }

    }
    public String finddelieverytimebypartnerid(String partnerid){
        Integer time  = 0;
        if(partnerMap.containsKey(partnerid)){
            List<String> orders = partnerToOrderMap.get(partnerid);
            for (String s : orders){
                if(orderMap.containsKey(s)){
                    Order order = orderMap.get(s);
                    if(time<order.getDeliveryTime()){
                        time = order.getDeliveryTime();
                    }
                }
            }
        }
        Integer hour = time/60;

        Integer min = time%60;
        StringBuilder stringBuilder = new StringBuilder();
        if(hour < 10){
            stringBuilder.append(0).append(hour);
        }else {
            stringBuilder.append(hour);
        }
        stringBuilder.append(":" );
        if (min < 10){
            stringBuilder.append(0).append(min);
        }
        else {
            stringBuilder.append(min);
        }
        return stringBuilder.toString();

    }

}
