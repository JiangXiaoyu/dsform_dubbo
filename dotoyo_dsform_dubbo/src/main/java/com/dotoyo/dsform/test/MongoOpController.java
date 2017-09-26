package com.dotoyo.dsform.test;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mongo")
public class MongoOpController {

    @Resource(name="buzzElementService")
    private IBuzzElementService bes;
    
    @RequestMapping("/save")
//    @ResponseBody
    public String doSave(HttpServletRequest req, HttpServletResponse response){
        
//        String itemName = req.getParameter("itemName");
//        String price = req.getParameter("price");
//        String desc = req.getParameter("desc");
    	String itemName = "name";
    	String price = "18";
    	String desc = "商品价格";
        BuzzElement be = new BuzzElement();
        be.setItemName(itemName);
        be.setDesc(desc);
        be.setPrice(price);
        try {
            bes.save(be);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Save OK";
    }
//    
//    @RequestMapping("/find")
//    @ResponseBody
//    public String doFind(HttpServletRequest req){
//        
//        String itemName = req.getParameter("itemName");        
//        JSONObject json = new JSONObject();
////        json.putOnce("item_name", itemName);
//        List<BuzzElement> res = null;
//        try {
//            res = bes.getItemInfo(json);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        return "Find OK: " + res.size() ;
//    }
//    
//    @RequestMapping("/update")
//    @ResponseBody
//    public String doUpdate(HttpServletRequest req){
//        
//        String itemName = req.getParameter("itemName");
//        String price = req.getParameter("price");
//        String desc = req.getParameter("desc");
//        
//        BuzzElement be = new BuzzElement();
//        be.setItemName(itemName);
//        be.setDesc(desc);
//        be.setPrice(price);
//        
//        try {
//            bes.update(be);
//        } catch (Exception e) {        
//            e.printStackTrace();
//        }
//        
//        return "Update OK";
//    }
}