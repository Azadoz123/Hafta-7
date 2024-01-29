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
}
