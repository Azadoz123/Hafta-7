package view;

import business.ModelManager;
import entity.Model;

import javax.swing.*;

public class ModelView extends Layout{
    private JPanel container;
    private JLabel lbl_header;
    private Model model;
    private ModelManager modelManager;
    public ModelView(Model model) {
        this.model = model;
        this.modelManager = new ModelManager();
        this.add(container);
        this.guiInitialize(300,500);
    }
}
