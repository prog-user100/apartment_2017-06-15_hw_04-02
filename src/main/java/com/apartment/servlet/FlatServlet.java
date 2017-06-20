package com.apartment.servlet;

import com.apartment.db.FlatDbService;
import com.apartment.entity.Flat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/flat", "/flat_search"})
public class FlatServlet extends HttpServlet {
    private FlatDbService flatDbService = new FlatDbService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("flat.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String district = req.getParameter("district");
        String address = req.getParameter("address");
        String area = req.getParameter("area");
        String roomNumber = req.getParameter("room_number");
        String price = req.getParameter("price");
        String sql = flatDbService.createSelectSQL(district, address, area, roomNumber, price);
        System.out.println("sql="+sql);
        List<Flat> flats = null;
        try {
            flats = flatDbService.searchFlat(sql);
        } catch (Exception e) {}
        req.setAttribute("flatList", flats);
        req.getRequestDispatcher("flat.jsp").forward(req, resp);
    }

}
