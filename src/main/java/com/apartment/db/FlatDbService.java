package com.apartment.db;

import com.apartment.entity.Flat;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FlatDbService {
    private static Connection connection = DbConnection.getConnection();

    public void createDbTable () throws SQLException {
        String dropTableSql = "DROP TABLE IF EXISTS flat";
        String createTableSql =
                "CREATE TABLE flat(" +
                        "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "district VARCHAR(100) NOT NULL," +
                        "address VARCHAR(100) NOT NULL," +
                        "area DOUBLE NOT NULL," +
                        "room_number TINYINT NOT NULL," +
                        "price INT NOT NULL" +
                        ")";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(dropTableSql);
        stmt.executeUpdate(createTableSql);
        stmt.close();
    }

    public void populateDbTableFromJSON() throws SQLException, ParseException, IOException {
        String insertDataSql= "INSERT INTO flat(district, address, area, room_number, price) VALUES(?,?,?,?,?)";
        PreparedStatement prepStmt = connection.prepareStatement(insertDataSql);

        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(getClass().getClassLoader().getResource("/flats.json").getFile());
        Object obj = parser.parse(reader);
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray jsonArray = (JSONArray) jsonObj.get("flats");
        Iterator<JSONObject> iterator = jsonArray.iterator();

        while (iterator.hasNext()) {
            JSONObject flatJson = iterator.next();
            prepStmt.setString(1, (String) flatJson.get("district"));
            prepStmt.setString(2, (String) flatJson.get("address"));
            prepStmt.setDouble(3, (Double) flatJson.get("area"));
            prepStmt.setLong(4, (Long) flatJson.get("roomNumber"));
            prepStmt.setLong(5, (Long) flatJson.get("price"));
            prepStmt.executeUpdate();
        }
        reader.close();
        prepStmt.close();
    }

    public List<String> getDistricts() throws SQLException {
        List<String> list = new ArrayList<String>();
        String sql = "SELECT DISTINCT district FROM flat";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(rs.getString("district"));
        }
        rs.close();
        stmt.close();
        return list;
    }

    public List<String> getAddresses() throws SQLException {
        List<String> list = new ArrayList<String>();
        String sql = "SELECT DISTINCT address FROM flat";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            list.add(rs.getString("address"));
        }
        rs.close();
        stmt.close();
        return list;
    }

    public List<Flat> searchFlat(String sql) throws SQLException {
        List<Flat> flatList = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Flat flat = new Flat();
            flat.setId(rs.getLong("id"));
            flat.setDistrict(rs.getString("district"));
            flat.setAddress(rs.getString("address"));
            flat.setArea(rs.getDouble("area"));
            flat.setRoomNumber(rs.getByte("room_number"));
            flat.setPrice(rs.getInt("price"));
            flatList.add(flat);
        }
        return flatList;
    }

    public String createSelectSQL(String district, String address, String area, String roomNumber, String price) {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        if(district != null && !district.isEmpty()) {
            builder.append(count>0?" AND ":" ").append("district=\"").append(district.trim()).append("\"");
            count++;
        }
        if(address != null && !address.isEmpty()) {
            builder.append(count>0?" AND ":" ").append("address=\"").append(address.trim()).append("\"");
            count++;
        }
        if(area != null && !area.isEmpty()) {
            String[] arr = area.trim().split("[->]");
            builder.append(count>0?" AND ":" ").append(parse("area", arr));
            count++;
        }
        if(roomNumber != null && !roomNumber.isEmpty()) {
            builder.append(count>0?" AND ":" ").append("room_number=").append(roomNumber.trim()).append("");
            count++;
        }
        if(price != null && !price.isEmpty()) {
            String[] arr = price.trim().split("[->]");
            if(!arr[0].isEmpty()) arr[0] += "00000";
            arr[1] += "00000";
            builder.append(count>0?" AND ":" ").append(parse("price", arr));
            count++;
        }
        return (count>0) ? "SELECT * FROM flat WHERE" + builder.toString() : "SELECT * FROM flat";
    }

    private StringBuilder parse(String columnName, String[] arr) {
        if(arr.length != 2) {
            throw new RuntimeException();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(columnName);
        if(arr[0].isEmpty()) {
            builder.append(">").append(arr[1]);
        } else {
            builder.append(" BETWEEN ").append(arr[0]).append(" AND ").append(arr[1]);
        }
        return builder;
    }
}
