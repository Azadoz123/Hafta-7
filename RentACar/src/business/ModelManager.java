package business;

import core.Helper;
import dao.ModelDao;
import entity.Model;

import java.util.ArrayList;

public class ModelManager {
    private final ModelDao modelDao = new ModelDao();
    public Model getById(int id){
        return this.modelDao.getById(id);
    }
    public ArrayList<Model> findAll(){
        return this.modelDao.findAll();
    }
    public ArrayList<Object[]> getForTable(int size, ArrayList<Model> modelList){
        ArrayList<Object[]> modelObjList = new ArrayList<>();
        for (Model model : modelList){
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = model.getId();
            rowObject[i++] = model.getBrand().getName();
            rowObject[i++] = model.getName();
            rowObject[i++] = model.getType();
            rowObject[i++] = model.getYear();
            rowObject[i++] = model.getFuel();
            rowObject[i++] = model.getGear();
            modelObjList.add(rowObject);
        }
        return modelObjList;
    }
    public boolean save (Model model){
        if(this.getById(model.getId()) != null){
            Helper.showMessage("error");
            return false;
        }
        return this.modelDao.save(model);
    }
    public boolean update(Model model){
        if(this.getById(model.getId()) == null){
            Helper.showMessage(model.getId() + " ID kayıtlı model bulunamadı !");
            return false;
        }
        return this.modelDao.update(model);
    }
    public boolean delete(int id){
        if(this.getById(id) == null){
            Helper.showMessage(id + " ID kayıtlı model bulunamadı !");
            return false;
        }
        return this.modelDao.delete(id);
    }
    public ArrayList<Model> getByListBrandId(int brandId){
        return this.modelDao.getByListBrandId(brandId);
    }
    public ArrayList<Model> searchForTable(int brandId,Model.Fuel fuel,Model.Gear gear,Model.Type type){
        String select = "SELECT * FROM public.model";
        ArrayList<String> whereList = new ArrayList<>();

        if (brandId != 0){
            whereList.add(" model_brand_id = " + brandId);
        }

        if(fuel != null){
            whereList.add("model_fuel = '" + fuel.toString() + "'");
        }

        if(gear != null){
            whereList.add("model_gear = '" + gear.toString() + "'");
        }

        if(type != null){
            whereList.add("model_type = '" + type.toString() + "'");
        }

       // System.out.println(whereList);
        String whereStr = String.join(" AND ", whereList );
        System.out.println(whereStr);
        String query = select;
        if (whereStr.length() > 0){
            query += " WHERE " + whereStr;
        }
        System.out.println(query);
        return this.modelDao.selectByQuery(query);
    }
}
