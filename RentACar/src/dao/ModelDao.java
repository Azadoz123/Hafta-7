package dao;

import core.Db;
import entity.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModelDao {
    private final Connection connection;
    private final BrandDao brandDao = new BrandDao();
    public ModelDao() {
        this.connection = Db.getInstance();
    }
    public Model getById(int id){
        Model model = null;
        String query = "SELECT * FROM public.model WHERE model_id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) model = this.match(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return model;
    }
    public ArrayList<Model> findAll(){
        return selectByQuery("SELECT * FROM public.model ORDER BY model_id ASC");
    }
    public ArrayList<Model> getByListBrandId(int brandId){
        return selectByQuery("SELECT * FROM public.model WHERE model_brand_id = " + brandId + " ORDER BY model_id ASC");
    }

    public ArrayList<Model> selectByQuery(String query){
        ArrayList<Model> modelList = new ArrayList<>();
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery(query);
            while (resultSet.next()){
                modelList.add(this.match(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return modelList;
    }
    public boolean save(Model model){
        String query = "INSERT INTO public.model" +
                "(" +
                "model_brand_id," +
                "model_name," +
                "model_type," +
                "model_year," +
                "model_fuel," +
                "model_gear" +
                ")" +
                " VALUES(?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,model.getBrand_id());
            preparedStatement.setString(2,model.getName());
            preparedStatement.setString(3,model.getType().toString());
            preparedStatement.setString(4,model.getYear());
            preparedStatement.setString(5,model.getFuel().toString());
            preparedStatement.setString(6,model.getGear().toString());
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public boolean update(Model model){
        String query = "UPDATE public.model SET " +
                "model_brand_id = ?," +
                "model_name = ?," +
                "model_type = ?," +
                "model_year = ?," +
                "model_fuel = ?," +
                "model_gear = ?" +
                " WHERE model_id = ?";

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,model.getBrand_id());
            preparedStatement.setString(2,model.getName());
            preparedStatement.setString(3,model.getType().toString());
            preparedStatement.setString(4,model.getYear());
            preparedStatement.setString(5,model.getFuel().toString());
            preparedStatement.setString(6,model.getGear().toString());
            preparedStatement.setInt(7,model.getId());
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean delete(int id){
        String query = "DELETE FROM public.model WHERE model_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Model match(ResultSet resultSet) throws SQLException {
        Model model = new Model();
        model.setId(resultSet.getInt("model_id"));
        model.setName(resultSet.getString("model_name"));
        model.setType(Model.Type.valueOf(resultSet.getString("model_type")));
        model.setYear(resultSet.getString("model_type"));
        model.setFuel(Model.Fuel.valueOf(resultSet.getString("model_fuel")));
        model.setGear(Model.Gear.valueOf(resultSet.getString("model_gear")));
        model.setBrand(this.brandDao.getById(resultSet.getInt("model_brand_id")));
        model.setBrand_id(resultSet.getInt("model_brand_id"));
        return model;
    }
}
