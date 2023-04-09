package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderMap = new HashMap<>();
    HashMap<String,DeliveryPartner> PartnerMap = new HashMap<>();
    HashMap<String,String> orderToPartnerMap = new HashMap<>();
    HashMap<String, HashSet<String>> partnerToOrderMap= new HashMap<>();


    public void addOrder(Order order){
        if (order == null){
            return;
        }
        String name = order.getId();

        orderMap.put(name,order);
        return;
    }
    public void addpartner(String partnerid){
            DeliveryPartner partner = new DeliveryPartner(partnerid);
            PartnerMap.put(partner.getId(),partner);

    }
    public void addordertopartner(String orderid,String partnerid){
        HashSet<String> orders = new HashSet<>();
        if (orderMap.containsKey(orderid) && PartnerMap.containsKey(partnerid)) {
            orderToPartnerMap.put(orderid, partnerid);
            if (partnerToOrderMap.containsKey(partnerid)) {
                orders = partnerToOrderMap.get(partnerid);

                orders.add(orderid);
                partnerToOrderMap.put(partnerid, orders);

            } else {
                partnerToOrderMap.put(partnerid, orders);
            }
        }
        DeliveryPartner partner = PartnerMap.get(partnerid);
        partner.setNumberOfOrders(orders.size());

        orderToPartnerMap.put(orderid,partnerid);


    }
    public Order getorderbyid(String orderid){
        if(orderMap.containsKey(orderid)){
            Order order = orderMap.get(orderid);
            return order;
        }
        else {
            return null;
        }

    }
    public DeliveryPartner getpartnerbyid(String partnerid){
        if(PartnerMap.containsKey(partnerid)){
            DeliveryPartner partner = PartnerMap.get(partnerid);
            return partner;
        }
        else {
            return null;
        }

    }
    public Integer getcountbypartnerid(String partnerid){
        if(PartnerMap.containsKey(partnerid)){
           Integer cnt = PartnerMap.get(partnerid).getNumberOfOrders();
           return cnt;
        }
        return 0;
    }
    public List<String> getordersbypartnerid(String partnerid){
        HashSet<String> orderlist = new HashSet<>();
        if(partnerToOrderMap.containsKey(partnerid)) {
            orderlist = partnerToOrderMap.get(partnerid);
        }
        return new ArrayList<>(orderlist);
    }
    public List<String> getallorders(){
        List<String> ans = new ArrayList<>();
        for (String s : orderMap.keySet()){
            ans.add(s);
        }
        return ans;

    }
    public Integer getcount(){
        return orderMap.size()-orderToPartnerMap.size();
    }
    public Integer countofordersleftaftergiventime(String time,String partnerid){
        int cnt = 0;
        int hour = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(3));
        int giventime = hour*60+min;

        if(partnerToOrderMap.containsKey(partnerid)){
            HashSet<String> t = partnerToOrderMap.get(partnerid);
            for( String orderid : t){
                if (orderMap.containsKey(orderid)){
                    Order order = orderMap.get(orderid);
                    if (order.getDeliveryTime() > giventime){
                        cnt += 1;
                    }
                }
            }
        }
        return cnt;
    }
    public void deletepartnerbyid(String partnerid){

        if(partnerToOrderMap.containsKey(partnerid)){
            HashSet<String> orders = partnerToOrderMap.get(partnerid);
            for(String s : orders){
                if(orderToPartnerMap.containsKey(orders)){
                    orderToPartnerMap.remove(s);
                }

            }
            partnerToOrderMap.remove(partnerid);
        }
        if (PartnerMap.containsKey(partnerid)){
            PartnerMap.remove(partnerid);
        }

    }
    public void deleteorderbyorderid(String orderid){
        orderMap.remove(orderid);

        if(orderToPartnerMap.containsKey(orderid)){
            String partnerid = orderToPartnerMap.get(orderid);
            HashSet<String> orders = partnerToOrderMap.get(partnerid);
            orders.remove(orderid);
            partnerToOrderMap.put(partnerid,orders);

            DeliveryPartner partner = PartnerMap.get(partnerid);
            partner.setNumberOfOrders(orders.size());
        }
        if (orderMap.containsKey(orderid)){
            orderMap.remove(orderid);
        }

    }
    public String finddelieverytimebypartnerid(String partnerid){
        Integer time  = 0;
        if(partnerToOrderMap.containsKey(partnerid)){
            HashSet<String> orders = partnerToOrderMap.get(partnerid);
            for (String s : orders){
                if(orderMap.containsKey(s)){
                    Order order = orderMap.get(s);
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
