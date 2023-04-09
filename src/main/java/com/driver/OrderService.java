package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    public void addorder(Order order){
        if(order == null){
            return;
        }
        orderRepository.addOrder(order);
        return;
    }
    public void addpartner(String partnerid){
        orderRepository.addpartner(partnerid);
        return;
    }
    public void addordertopartner(String orderid ,String partnerid){
        orderRepository.addordertopartner(orderid,partnerid);
        return;
    }
    public Order getorderbyid(String orderid){
        return orderRepository.getorderbyid(orderid);

    }
    public DeliveryPartner getpartnerbyid(String partnerid){
        return orderRepository.getpartnerbyid(partnerid);
    }
    public Integer getcountbypartnerid(String partnerid){
        return orderRepository.getcountbypartnerid(partnerid);
    }
    public List<String> getordersbypartnerid(String partnerid){
        return orderRepository.getordersbypartnerid(partnerid);
    }
    public List<String> getallorders(){
        return orderRepository.getallorders();
    }
    public Integer getcount(){
        return orderRepository.getcount();
    }
    public Integer countofordersleftaftergiventime(String time,String partnerid){
        return orderRepository.countofordersleftaftergiventime(time,partnerid);
    }
    public void deletepartnerbyid(String partnerid){
        orderRepository.deletepartnerbyid(partnerid);
    }
    public void deleteorderbyorderid(String orderid){
        orderRepository.deleteorderbyorderid(orderid);
    }
    public String finddelieverytimebypartnerid(String partnerid){
       return orderRepository.finddelieverytimebypartnerid(partnerid);
    }
}
