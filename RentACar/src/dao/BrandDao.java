package dao;

import core.Db;
import entity.Brand;
import entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BrandDao {
    private final Connection connection;

    public BrandDao() {
        this.connection = Db.getInstance();
    }

    public ArrayList<Brand> findAll(){
        ArrayList<Brand> brandList = new ArrayList<>();
        String sql = "SELECT * FROM public.brand";
        Brand user = null;
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                brandList.add(this.match(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brandList;
    }
    public Brand match(ResultSet resultSet) throws SQLException {
        Brand brand = new Brand();
        brand.setId(resultSet.getInt("brand_id"));
        brand.setName(resultSet.getString("brand_name"));

        return brand;
    }
}
