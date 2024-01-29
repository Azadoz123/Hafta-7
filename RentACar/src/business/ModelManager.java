package business;

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
}
