package com.apartment.servlet;

import com.apartment.db.FlatDbService;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebListener
public class ServletContextListenerImpl  implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        FlatDbService flatDbService = new FlatDbService();
        List<String> districtList = new ArrayList<>();
        List<String> addressList = new ArrayList<>();
        try {
            flatDbService.createDbTable();
            flatDbService.populateDbTableFromJSON();
            districtList = flatDbService.getDistricts();
            addressList = flatDbService.getAddresses();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        servletContextEvent.getServletContext().setAttribute("districtList", districtList);
        servletContextEvent.getServletContext().setAttribute("addressList", addressList);
        servletContextEvent.getServletContext().setAttribute("areaList", Arrays.asList("0-30", "30-50", "50-70", "70-100", "100-130", ">130"));
        servletContextEvent.getServletContext().setAttribute("roomNumberList", Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        servletContextEvent.getServletContext().setAttribute("priceList", Arrays.asList("0-30", "30-60", "60-100", "100-500", "500-1000", ">1000"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
